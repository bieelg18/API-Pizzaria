package dev.bieelg18.Pizzaria.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import dev.bieelg18.Pizzaria.security.NaoAutenticadoHandler;
import dev.bieelg18.Pizzaria.security.SemPermissaoHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final NaoAutenticadoHandler naoAutenticadoHandler;
    private final SemPermissaoHandler semPermissaoHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
        throws Exception{
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(naoAutenticadoHandler)
                        .accessDeniedHandler(semPermissaoHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        //Rotas POST publicas, todos podem acessar
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/auth/login")
                        .permitAll()

                        //Rotas da documentação
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()

                        //Rotas GET que precisa estar autenticado para acessar
                        .requestMatchers(HttpMethod.GET, "/pedidos/all/me")
                        .authenticated()

                        //Rotas PATCH que precisa estar autenticado
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/me", "/pedidos/cancelar/{id}")
                        .authenticated()

                        //Rotas POST que precisa estar autenticado
                        .requestMatchers(HttpMethod.POST, "/pedidos")
                        .authenticated()

                        //Rotas GET que precisa ser ADMIN para acessar
                        .requestMatchers(HttpMethod.GET, "/usuarios/all/**", "/usuarios/buscar",
                                "/usuarios/{id}", "/produtos/all/**", "/pedidos/all/**")
                        .hasRole("ADMIN")

                        //Rotas POST que precisa ser ADMIN para acessar
                        .requestMatchers(HttpMethod.POST, "/produtos")
                        .hasRole("ADMIN")

                        //Rotas PATCH que precisa ser ADMIN para acessar
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}/permissao",
                                "/produtos/**", "/pedidos/confirmar/{id}", "/pedidos/confirmar/**",
                                "/pedidos/producao/**", "/pedidos/entrega/**", "/pedidos/concluido/**")
                        .hasRole("ADMIN")

                        //Rotas DELETE, somente ADMIN consegue acessar
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}", "/produtos/{id}",
                                "/pedidos/{id}")
                        .hasRole("ADMIN")

                ).addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

}

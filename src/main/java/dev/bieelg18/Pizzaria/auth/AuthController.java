package dev.bieelg18.Pizzaria.auth;

import dev.bieelg18.Pizzaria.repository.usuario.UsuarioRepository;
import dev.bieelg18.Pizzaria.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;


    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.senha()
                )
        );

        UserDetails usuario = usuarioRepository.findByEmail(loginDTO.email())
                .orElseThrow();
        String token = jwtService.gerarToken(usuario);
        return new TokenDTO(token);
    }

}

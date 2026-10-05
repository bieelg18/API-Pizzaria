package dev.bieelg18.Pizzaria.repository.usuario;

import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPermissao(Permissao permissao);

}

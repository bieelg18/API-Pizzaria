package dev.bieelg18.Pizzaria.model.dto.usuario;

import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;

public record ListarUsuarioDTO(
        Integer id,
        String nome,
        String email,
        String endereco,
        Permissao permissao
) {
}

package dev.bieelg18.Pizzaria.model.dto.usuario;

import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;

public record CriarUsuarioDTO(
        String nome,
        String email,
        String senha,
        String endereco,
        Permissao permissao
) {
}

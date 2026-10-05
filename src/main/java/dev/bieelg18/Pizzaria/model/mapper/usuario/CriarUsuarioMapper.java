package dev.bieelg18.Pizzaria.model.mapper.usuario;

import dev.bieelg18.Pizzaria.model.dto.usuario.CriarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CriarUsuarioMapper {

    CriarUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(CriarUsuarioDTO criarUsuarioDTO);

}

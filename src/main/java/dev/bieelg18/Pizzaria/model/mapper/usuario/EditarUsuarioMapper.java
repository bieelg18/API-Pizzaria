package dev.bieelg18.Pizzaria.model.mapper.usuario;

import dev.bieelg18.Pizzaria.model.dto.usuario.EditarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarUsuarioMapper {

    EditarUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(EditarUsuarioDTO editarUsuarioDTO);

}

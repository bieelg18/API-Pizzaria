package dev.bieelg18.Pizzaria.model.mapper.usuario;

import dev.bieelg18.Pizzaria.model.dto.usuario.ListarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListarUsuarioMapper {

    ListarUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(ListarUsuarioDTO listarUsuarioDTO);

}

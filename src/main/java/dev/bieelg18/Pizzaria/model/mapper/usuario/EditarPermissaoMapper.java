package dev.bieelg18.Pizzaria.model.mapper.usuario;

import dev.bieelg18.Pizzaria.model.dto.usuario.EditarPermissaoDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarPermissaoMapper {

    EditarPermissaoDTO toDTO(Usuario usuario);

    Usuario toEntity(EditarPermissaoDTO editarPermissaoDTO);

}

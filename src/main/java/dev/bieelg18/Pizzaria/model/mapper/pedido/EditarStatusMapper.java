package dev.bieelg18.Pizzaria.model.mapper.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.EditarStatusDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarStatusMapper {

    EditarStatusDTO toDTO(Pedido pedido);

    Pedido toEntity(EditarStatusDTO editarStatusDTO);

}

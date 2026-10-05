package dev.bieelg18.Pizzaria.model.mapper.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListarPedidoMapper {

    ListarPedidoDTO toDTO(Pedido pedido);

    Pedido toEntity(ListarPedidoDTO listarPedidoDTO);

}

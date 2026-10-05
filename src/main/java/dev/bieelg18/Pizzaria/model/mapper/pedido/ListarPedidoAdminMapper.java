package dev.bieelg18.Pizzaria.model.mapper.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListarPedidoAdminMapper {

    ListarPedidoAdminDTO toDTO(Pedido pedido);

    Pedido toEntity(ListarPedidoAdminDTO listarPedidoAdminDTO);

}

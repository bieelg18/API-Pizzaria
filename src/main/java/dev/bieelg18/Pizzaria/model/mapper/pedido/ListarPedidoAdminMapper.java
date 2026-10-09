package dev.bieelg18.Pizzaria.model.mapper.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import dev.bieelg18.Pizzaria.model.mapper.itemPedido.CriarItemPedidoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CriarItemPedidoMapper.class)
public interface ListarPedidoAdminMapper {

    @Mapping(source = "id", target = "numeroPedido")
    ListarPedidoAdminDTO toDTO(Pedido pedido);

    Pedido toEntity(ListarPedidoAdminDTO listarPedidoAdminDTO);

}

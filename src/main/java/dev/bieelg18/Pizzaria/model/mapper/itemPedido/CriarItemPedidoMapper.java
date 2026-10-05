package dev.bieelg18.Pizzaria.model.mapper.itemPedido;

import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriarItemPedidoMapper {

    CriarItemPedidoDTO toDTO(ItemPedido itemPedido);

    ItemPedido toEntity(CriarItemPedidoDTO criarItemPedidoDTO);

}

package dev.bieelg18.Pizzaria.model.mapper.itemPedido;

import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CriarItemPedidoMapper {

    @Mapping(source = "produto.id", target = "idProduto")
    CriarItemPedidoDTO toDTO(ItemPedido itemPedido);

    ItemPedido toEntity(CriarItemPedidoDTO criarItemPedidoDTO);

}

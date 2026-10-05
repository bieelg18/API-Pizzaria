package dev.bieelg18.Pizzaria.model.mapper.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.CriarPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriarPedidoMapper {

    CriarPedidoDTO toDTO(Pedido pedido);

    Pedido toEntity(CriarPedidoDTO criarPedidoDTO);

}

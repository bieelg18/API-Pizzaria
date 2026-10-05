package dev.bieelg18.Pizzaria.model.dto.pedido;

import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public record ListarPedidoDTO(
        Integer numeroPedido,
        List<CriarItemPedidoDTO> itens,
        BigDecimal total,
        StatusPedido status
) {
}

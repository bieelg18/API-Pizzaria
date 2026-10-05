package dev.bieelg18.Pizzaria.model.dto.pedido;

import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;

public record EditarStatusDTO(
        StatusPedido status
) {
}

package dev.bieelg18.Pizzaria.model.dto.itemPedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarItemPedidoDTO(
        @NotNull
        Integer idProduto,

        @NotNull
        @Positive
        Integer quantidade
) {
}

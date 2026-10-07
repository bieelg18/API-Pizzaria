package dev.bieelg18.Pizzaria.model.dto.pedido;

import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public record CriarPedidoDTO(

        @NotEmpty
        @Valid
        List<CriarItemPedidoDTO> itens
) {
}

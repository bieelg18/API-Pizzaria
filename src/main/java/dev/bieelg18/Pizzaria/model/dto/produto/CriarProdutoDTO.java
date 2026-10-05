package dev.bieelg18.Pizzaria.model.dto.produto;

import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;

import java.math.BigDecimal;

public record CriarProdutoDTO(
        String nome,
        TipoProduto tipoProduto,
        BigDecimal preco
) {
}

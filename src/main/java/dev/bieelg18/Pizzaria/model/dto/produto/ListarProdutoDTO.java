package dev.bieelg18.Pizzaria.model.dto.produto;

import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;

import java.math.BigDecimal;

public record ListarProdutoDTO(
        Integer id,
        String nome,
        TipoProduto tipoProduto,
        BigDecimal preco
) {
}

package dev.bieelg18.Pizzaria.repository.produto;

import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findByTipoProduto(TipoProduto tipoProduto);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

}

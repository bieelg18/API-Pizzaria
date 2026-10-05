package dev.bieelg18.Pizzaria.model.entity.produto;

import dev.bieelg18.Pizzaria.model.entity.pedido.ItemPedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "tipo_produto", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @Column(nullable = false)
    private BigDecimal preco;

    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itens;
}

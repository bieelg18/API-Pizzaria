package dev.bieelg18.Pizzaria.model.repository.pedido;

import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByStatus(StatusPedido statusPedido);

}

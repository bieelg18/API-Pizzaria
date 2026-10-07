package dev.bieelg18.Pizzaria.repository.pedido;

import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByStatus(StatusPedido statusPedido);

    List<Pedido> findByCliente(Usuario usuario);



}

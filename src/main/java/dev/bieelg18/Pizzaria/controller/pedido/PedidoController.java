package dev.bieelg18.Pizzaria.controller.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.CriarPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import dev.bieelg18.Pizzaria.service.pedido.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;


    //Endpoint para fazer um novo pedido
    @PostMapping
    public ListarPedidoDTO criarPedido(@RequestBody CriarPedidoDTO criarDTO, Authentication authentication){
        return service.criarPedido(criarDTO, authentication);
    }

    //Endpoint para listar pedidos do usuário que fizer a requisição
    @GetMapping("/all/me")
    public List<ListarPedidoDTO> listarPedidosUsuario(Authentication authentication){
        return service.listarPedidosUsuario(authentication);
    }

    //Endpoint para listar todos os pedidos existentes
    @GetMapping("/all")
    public List<ListarPedidoAdminDTO> listarTodosOsPedidos(){
        return service.listarTodosOsPedidos();
    }

    //Endpoint para deletar um pedido
    @DeleteMapping("/{id}")
    public void deletaPedido(@PathVariable Integer id){
        service.deletarPedido(id);
    }

    //Endpoint para cancelar um pedido
    @PatchMapping("/cancelar/{id}")
    public ListarPedidoDTO cancelarPedido(@PathVariable Integer id, Authentication authentication){
        return service.cancelarPedido(id, authentication);
    }

    //Endpoint para listar todos os pedidos por status
    @GetMapping("/all/")
    public List<ListarPedidoDTO> listarStatus(@RequestParam StatusPedido statusPedido){
        return service.listarStatus(statusPedido);
    }

    //Endpoint para alterar o status para confirmado
    @PatchMapping("/confirmar/{id}")
    public ListarPedidoDTO confirmado(@PathVariable Integer id){
        return service.confirmado(id);
    }

    //Endpoint para alterar o status para em produção
    @PatchMapping("/producao/{id}")
    public ListarPedidoDTO producao(@PathVariable Integer id){
        return service.producao(id);
    }

    //Endpoint para alterar o status para saiu para entrega
    @PatchMapping("/entrega/{id}")
    public ListarPedidoDTO entrega(@PathVariable Integer id){
        return service.entrega(id);
    }

    //Endpoint para alterar o status para concluido
    @PatchMapping("/concluido/{id}")
    public ListarPedidoDTO concluido(@PathVariable Integer id){
        return service.concluido(id);
    }

}

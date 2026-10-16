package dev.bieelg18.Pizzaria.controller.pedido;

import dev.bieelg18.Pizzaria.documentation.pedido.PedidoControllerDocs;
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
public class PedidoController implements PedidoControllerDocs {

    private final PedidoService service;


    //Endpoint para fazer um novo pedido
    @Override
    @PostMapping
    public ListarPedidoDTO criarPedido(@RequestBody CriarPedidoDTO criarDTO, Authentication authentication){
        return service.criarPedido(criarDTO, authentication);
    }

    //Endpoint para listar pedidos do usuário que fizer a requisição
    @Override
    @GetMapping("/all/me")
    public List<ListarPedidoDTO> listarPedidosUsuario(Authentication authentication){
        return service.listarPedidosUsuario(authentication);
    }

    //Endpoint para listar todos os pedidos existentes
    @Override
    @GetMapping("/all")
    public List<ListarPedidoAdminDTO> listarTodosOsPedidos(){
        return service.listarTodosOsPedidos();
    }

    //Endpoint para deletar um pedido
    @Override
    @DeleteMapping("/{id}")
    public void deletaPedido(@PathVariable Integer id){
        service.deletarPedido(id);
    }

    //Endpoint para cancelar um pedido
    @Override
    @PatchMapping("/cancelar/{id}")
    public ListarPedidoDTO cancelarPedido(@PathVariable Integer id, Authentication authentication){
        return service.cancelarPedido(id, authentication);
    }

    //Endpoint para listar todos os pedidos por status
    @Override
    @GetMapping("/all/")
    public List<ListarPedidoDTO> listarStatus(@RequestParam StatusPedido statusPedido){
        return service.listarStatus(statusPedido);
    }

    //Endpoint para alterar o status para confirmado
    @Override
    @PatchMapping("/confirmar/{id}")
    public ListarPedidoDTO confirmado(@PathVariable Integer id){
        return service.confirmado(id);
    }

    //Endpoint para alterar o status para em produção
    @Override
    @PatchMapping("/producao/{id}")
    public ListarPedidoDTO producao(@PathVariable Integer id){
        return service.producao(id);
    }

    //Endpoint para alterar o status para saiu para entrega
    @Override
    @PatchMapping("/entrega/{id}")
    public ListarPedidoDTO entrega(@PathVariable Integer id){
        return service.entrega(id);
    }

    //Endpoint para alterar o status para concluido
    @Override
    @PatchMapping("/concluido/{id}")
    public ListarPedidoDTO concluido(@PathVariable Integer id){
        return service.concluido(id);
    }

}

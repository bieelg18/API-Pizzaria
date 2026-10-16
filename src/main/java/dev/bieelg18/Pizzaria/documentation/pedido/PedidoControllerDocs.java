package dev.bieelg18.Pizzaria.documentation.pedido;

import dev.bieelg18.Pizzaria.model.dto.pedido.CriarPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PedidoControllerDocs {

    @Operation(summary = "Cria um novo pedido",
    description = "Qualquer usuário autenticado consegue acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado"),
            @ApiResponse(responseCode = "400", description = "Pedido não criado"),
            @ApiResponse(responseCode = "404", description = "Pedido não criado, usuário autenticado não encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não criado, produto não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    ListarPedidoDTO criarPedido(CriarPedidoDTO criarPedidoDTO, Authentication authentication);

    @Operation(summary = "Lista os pedidos do usuário que chamar a requisição",
    description = "Qualquer usuário autenticado consegue acessar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos do usuário"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    List<ListarPedidoDTO> listarPedidosUsuario(Authentication authentication);

    @Operation(summary = "Lista todos os pedidos existentes",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os pedidos existentes"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarPedidoAdminDTO> listarTodosOsPedidos();

    @Operation(summary = "Deleta um pedido existente",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido deletado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado, exclusão não realizada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    void deletaPedido(Integer id);

    @Operation(summary = "Altera o status de um pedido para CANCELADO",
    description = "Só é possível alterar o status de um pedido atribuido ao usuário que chamar a requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Usuário autenticado não encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado, cancelamento não realizado"),
            @ApiResponse(responseCode = "409", description = "Cancelamento não realizado, pedido pertence a outro cliente"),
            @ApiResponse(responseCode = "409", description = "Cancelamento não realizado, pedido está com status incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    ListarPedidoDTO cancelarPedido(Integer id, Authentication authentication);

    @Operation(summary = "Lista pedidos filtrados pelo status",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados pelo status"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarPedidoDTO> listarStatus(StatusPedido statusPedido);

    @Operation(summary = "Altera o status de um pedido para CONFIRMADO",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado pra CONFIRMADO"),
            @ApiResponse(responseCode = "404", description = "Status não alterado, pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Status não alterado, pedido está com status incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarPedidoDTO confirmado(Integer id);

    @Operation(summary = "Altera o status de um pedido para EM_PRODUCAO",
            description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado pra EM_PRODUCAO"),
            @ApiResponse(responseCode = "404", description = "Status não alterado, pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Status não alterado, pedido está com status incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarPedidoDTO producao(Integer id);

    @Operation(summary = "Altera o status de um pedido para SAIU_PARA_ENTREGA",
            description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado pra SAIU_PARA_ENTREGA"),
            @ApiResponse(responseCode = "404", description = "Status não alterado, pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Status não alterado, pedido está com status incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarPedidoDTO entrega(Integer id);

    @Operation(summary = "Altera o status de um pedido para CONCLUIDO",
            description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado pra CONCLUIDO"),
            @ApiResponse(responseCode = "404", description = "Status não alterado, pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Status não alterado, pedido está com status incorreto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarPedidoDTO concluido(Integer id);

}

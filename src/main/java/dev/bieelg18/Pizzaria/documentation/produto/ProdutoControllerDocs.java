package dev.bieelg18.Pizzaria.documentation.produto;

import dev.bieelg18.Pizzaria.model.dto.produto.CriarProdutoDTO;
import dev.bieelg18.Pizzaria.model.dto.produto.ListarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface ProdutoControllerDocs {

    @Operation(summary = "Cria um novo produto no banco de dados",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Produto não criado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para este recurso")
    })
    ListarProdutoDTO criarProduto(CriarProdutoDTO criarProdutoDTO);

    @Operation(summary = "Lista todos os produtos cadastrados",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os produtos existentes"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarProdutoDTO> listarProdutos();

    @Operation(summary = "Edita um produto já cadastrado",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto editado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarProdutoDTO editarProduto(Integer id, CriarProdutoDTO editarDTO);

    @Operation(summary = "Deleta um produto existente",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado, exclusão não realizada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    void deletarProduto(Integer id);

    @Operation(summary = "Lista produtos pelo tipo do produto",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos filtrados pelo tipo do produto"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarProdutoDTO> listarTipos(TipoProduto tipoProduto);

    @Operation(summary = "Lista produtos pelo nome",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos filtrados pelo nome"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarProdutoDTO> listarNome(String nome);

}

package dev.bieelg18.Pizzaria.documentation.usuario;

import dev.bieelg18.Pizzaria.model.dto.usuario.CriarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarPermissaoDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.ListarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsuarioControllerDocs {

    @Operation(summary = "Cria um novo usuário no banco de dados",
    description = "Não é necessário estar autenticado para acessar esse endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Usuário não criado")
    })
    ListarUsuarioDTO criarUsuario(CriarUsuarioDTO criarDTO);

    @Operation(summary = "Lista todos os usuários existentes no banco de dados",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os usuário existentes"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarUsuarioDTO> listarUsuarios();

    @Operation(summary = "Busca um usuário através do e-mail fornecido",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário atribuido ao e-mail usado para a busca"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarUsuarioDTO buscarEmail(String email);

    @Operation(summary = "Busca um usuário através do ID fornecido",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário atribuido ao ID usado para a busca"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarUsuarioDTO buscarId(Integer id);

    @Operation(summary = "Permite alterar os dados de cadastro do usuário",
    description = "Edita os dados somente do usuário que chamou a requisição e é necessário estar autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados de cadastro editados"),
            @ApiResponse(responseCode = "404", description = "Usuário autenticado não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    ListarUsuarioDTO editarDadosCadastro(EditarUsuarioDTO editarUsuarioDTO, Authentication authentication);

    @Operation(summary = "Edita a permissão de um usuário já cadastrado",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão alterada"),
            @ApiResponse(responseCode = "404", description = "Permissão não alterada pois o usuário não foi encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarUsuarioDTO editarPermissao(Integer id, EditarPermissaoDTO permissaoDTO);

    @Operation(summary = "Deleta um usuário existente",
    description = "Somente usuários com permissão ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado, exclusão não realizada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    void deletarUsuario(Integer id);

    @Operation(summary = "Lista usuários filtrados pela permissão",
    description = "Somente usuários com permissão de ADMIN podem acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com usuários filtrados pela permissão"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarUsuarioDTO> listarPermissao(Permissao permissao);

}

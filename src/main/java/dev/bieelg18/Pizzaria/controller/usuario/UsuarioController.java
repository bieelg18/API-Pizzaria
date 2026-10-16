package dev.bieelg18.Pizzaria.controller.usuario;

import dev.bieelg18.Pizzaria.documentation.usuario.UsuarioControllerDocs;
import dev.bieelg18.Pizzaria.model.dto.usuario.CriarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarPermissaoDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.ListarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;
import dev.bieelg18.Pizzaria.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerDocs {

    private final UsuarioService service;

    //Endpoint para cadastrar usuário
    @Override
    @PostMapping
    public ListarUsuarioDTO criarUsuario(@RequestBody CriarUsuarioDTO criarDTO){
        return service.criarUsuario(criarDTO);
    }

    //Endpoint para listar todos os usuários cadastrados
    @Override
    @GetMapping("/all")
    public List<ListarUsuarioDTO> listarUsuarios(){
        return service.listarUsuarios();
    }

    //Endpoint para buscar um usuário por e-mail
    @Override
    @GetMapping("/buscar")
    public ListarUsuarioDTO buscarEmail(@RequestParam String email){
        return service.buscarEmail(email);
    }

    //Endpoint para buscar um usuário por id
    @Override
    @GetMapping("/{id}")
    public ListarUsuarioDTO buscarId(@PathVariable Integer id){
        return service.buscarId(id);
    }

    //Endpoint para editar os dados de cadastro do usuário que chamar a requisição
    @Override
    @PatchMapping("/me")
    public ListarUsuarioDTO editarDadosCadastro(@RequestBody EditarUsuarioDTO editarDTO, Authentication authentication){
        return service.editarDadosCadastro(editarDTO, authentication);
    }

    //Endpoint para alterar a permissão de um usuário
    @Override
    @PatchMapping("/{id}/permissao")
    public ListarUsuarioDTO editarPermissao(@PathVariable Integer id, @RequestBody EditarPermissaoDTO permissao){
        return service.editarPermissao(id, permissao);
    }

    //Endpoint para deletar um usuário
    @Override
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id){
        service.deletarUsuario(id);
    }

    //Endpoint para listar usuários pela permissão
    @Override
    @GetMapping("/all/permissao")
    public List<ListarUsuarioDTO> listarPermissao(@RequestParam Permissao permissao){
        return service.listarPermissao(permissao);
    }

}

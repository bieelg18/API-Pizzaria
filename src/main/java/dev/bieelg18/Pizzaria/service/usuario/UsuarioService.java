package dev.bieelg18.Pizzaria.service.usuario;

import dev.bieelg18.Pizzaria.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Pizzaria.model.dto.usuario.CriarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarPermissaoDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.EditarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.ListarUsuarioDTO;
import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import dev.bieelg18.Pizzaria.model.mapper.usuario.CriarUsuarioMapper;
import dev.bieelg18.Pizzaria.model.mapper.usuario.EditarPermissaoMapper;
import dev.bieelg18.Pizzaria.model.mapper.usuario.EditarUsuarioMapper;
import dev.bieelg18.Pizzaria.model.mapper.usuario.ListarUsuarioMapper;
import dev.bieelg18.Pizzaria.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CriarUsuarioMapper criarUsuarioMapper;
    private final EditarUsuarioMapper editarUsuarioMapper;
    private final ListarUsuarioMapper listarUsuarioMapper;
    private final EditarPermissaoMapper editarPermissaoMapper;
    private final PasswordEncoder passwordEncoder;


    //Método para criar um novo usuário
    @Transactional
    public ListarUsuarioDTO criarUsuario(CriarUsuarioDTO criarDTO){

        Usuario usuario = criarUsuarioMapper.toEntity(criarDTO);
        usuario.setSenha(passwordEncoder.encode(criarDTO.senha()));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return listarUsuarioMapper.toDTO(usuarioSalvo);

    }

    //Método para listar todos os usuários existentes
    @Transactional(readOnly = true)
    public List<ListarUsuarioDTO> listarUsuarios(){

        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(listarUsuarioMapper::toDTO)
                .toList();

    }

    //Método para buscar um usuário por e-mail
    @Transactional(readOnly = true)
    public ListarUsuarioDTO buscarEmail(String email){

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o e-mail " + email + " não encontrado"
                ));

        return listarUsuarioMapper.toDTO(usuario);

    }

    //Método para editar dados de cadastro do usuário que chamar a requisição
    @Transactional
    public ListarUsuarioDTO editarDadosCadastro(EditarUsuarioDTO editarDTO, Authentication authentication){

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));

        if (editarDTO.nome() != null){
            usuario.setNome(editarDTO.nome());
        }
        if (editarDTO.email() != null){
            usuario.setEmail(editarDTO.email());
        }
        if (editarDTO.senha() != null){
            usuario.setSenha(passwordEncoder.encode(editarDTO.senha()));
        }
        if (editarDTO.endereco() != null){
            usuario.setEndereco(editarDTO.endereco());
        }

        return listarUsuarioMapper.toDTO(usuario);

    }

    //Método para alterar a permissão de um usuário
    @Transactional
    public ListarUsuarioDTO editarPermissao(Integer id, EditarPermissaoDTO editarPermissaoDTO){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o ID " + id + " não encontrado"
                ));

        usuario.setPermissao(editarPermissaoDTO.permissao());

        return listarUsuarioMapper.toDTO(usuario);

    }

    //Método para deletar um usuário do banco de dados
    @Transactional
    public void deletarUsuario(Integer id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o ID " + id + " não encontrado"
                ));

        usuarioRepository.delete(usuario);
    }

    //Método para listar usuários pela permissão
    @Transactional(readOnly = true)
    public List<ListarUsuarioDTO> listarPermissao(Permissao permissao){

        List<Usuario> usuarios = usuarioRepository.findByPermissao(permissao);

        return usuarios.stream()
                .map(listarUsuarioMapper::toDTO)
                .toList();

    }

    //Método para buscar um usuário por id
    @Transactional(readOnly = true)
    public ListarUsuarioDTO buscarId(Integer id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o ID " + id + " não encontrado"
                ));

        return listarUsuarioMapper.toDTO(usuario);

    }

}

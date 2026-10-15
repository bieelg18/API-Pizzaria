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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private CriarUsuarioMapper criarUsuarioMapper;

    @Mock
    private EditarUsuarioMapper editarUsuarioMapper;

    @Mock
    private ListarUsuarioMapper listarUsuarioMapper;

    @Mock
    private EditarPermissaoMapper editarPermissaoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;

    @BeforeEach
    void setUp(){

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@email.com");
        usuario.setEndereco("Rua Teste 10");
        usuario.setPermissao(Permissao.CLIENTE);

    }

    //Teste para criar um novo usuário
    @Test
    void deveCriarUmNovoUsuario(){

        CriarUsuarioDTO criarDTO = new CriarUsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                "Senha123",
                usuario.getEndereco(),
                usuario.getPermissao()
        );

        when(criarUsuarioMapper.toEntity(criarDTO))
                .thenReturn(usuario);

        when(passwordEncoder.encode("Senha123"))
                .thenReturn("SenhaCriptografada");

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                criarDTO.nome(),
                criarDTO.email(),
                criarDTO.endereco(),
                criarDTO.permissao()
        );

        when(repository.save(usuario))
                .thenReturn(usuario);

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarUsuarioDTO resultado =
                service.criarUsuario(criarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals("SenhaCriptografada", usuario.getSenha());

        verify(criarUsuarioMapper).toEntity(criarDTO);
        verify(passwordEncoder).encode("Senha123");
        verify(repository).save(usuario);

    }

    //Teste para listar todos os usuários existentes
    @Test
    void deveListarTodosOsUsuarios(){

        when(repository.findAll())
                .thenReturn(List.of(usuario));

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPermissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        List<ListarUsuarioDTO> resultado =
                service.listarUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findAll();
        verify(listarUsuarioMapper).toDTO(usuario);
    }

    //Retornando uma lista vazia de usuários
    @Test
    void deveRetornarListaVaziaDeUsuarios(){

        when(repository.findAll())
                .thenReturn(List.of());

        List<ListarUsuarioDTO> resultado =
                service.listarUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAll();
        verify(listarUsuarioMapper, never()).toDTO(any());

    }

    //Teste para buscar um usuário por e-mail
    @Test
    void deveRetornarUmUsuarioPorEmail(){

        when(repository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPermissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarUsuarioDTO resultado =
                service.buscarEmail(usuario.getEmail());

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        verify(repository).findByEmail(usuario.getEmail());
        verify(listarUsuarioMapper).toDTO(usuario);

    }

    //Teste para não retornar um usuário e lançar uma exceção quando o e-mail não for encontrado
    @Test
    void naoDeveRetornarUsuarioEDeveLancarExcecao(){

        when(repository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarEmail(usuario.getEmail())
        );

        assertEquals(
                "Usuário com o e-mail " + usuario.getEmail() + " não encontrado",
                exception.getMessage()
        );

        verify(repository).findByEmail(usuario.getEmail());
        verify(listarUsuarioMapper, never()).toDTO(any());

    }

    //Teste para editar dados de cadastro do usuário que chamar a requisição
    @Test
    void deveEditarDadosCadastro(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        EditarUsuarioDTO editarDTO = new EditarUsuarioDTO(
                "Cliente Teste Editar",
                "clienteEditar@email.com",
                "Senha456",
                "Rua Editar 20"
        );

        when(passwordEncoder.encode("Senha456"))
                .thenReturn("SenhaEditada");


        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                editarDTO.nome(),
                editarDTO.email(),
                editarDTO.endereco(),
                usuario.getPermissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarUsuarioDTO resultado =
                service.editarDadosCadastro(editarDTO, authentication);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(dtoEsperado.nome(), usuario.getNome());
        assertEquals(dtoEsperado.email(), usuario.getEmail());
        assertEquals("SenhaEditada", usuario.getSenha());
        assertEquals(dtoEsperado.endereco(), usuario.getEndereco());

        verify(authentication).getName();
        verify(repository).findByEmail("usuario@email.com");
        verify(repository, never()).save(any());
        verify(listarUsuarioMapper).toDTO(usuario);

    }

    //Teste para não alterar dados de cadastro do usuário quando ele não for encontrado
    @Test
    void naoDeveEditarDadosDeCadastro(){

        when(authentication.getName())
                .thenReturn("usuario@email.com");

        when(repository.findByEmail("usuario@email.com"))
                .thenReturn(Optional.empty());

        EditarUsuarioDTO editarDTO = new EditarUsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getEndereco()
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.editarDadosCadastro(editarDTO, authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        verify(authentication).getName();
        verify(repository).findByEmail("usuario@email.com");
        verify(repository, never()).save(any());
        verify(listarUsuarioMapper, never()).toDTO(any());



    }

    //Teste para alterar a permissão de um usuário
    @Test
    void deveAlterarPermissaoUsuario(){

        when(repository.findById(1))
                .thenReturn(Optional.of(usuario));

        EditarPermissaoDTO editarDTO = new EditarPermissaoDTO(
                Permissao.ADMIN
        );

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                editarDTO.permissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarUsuarioDTO resultado =
                service.editarPermissao(1, editarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);
        assertEquals(dtoEsperado.permissao(), usuario.getPermissao());

        verify(repository).findById(1);
        verify(listarUsuarioMapper).toDTO(usuario);

    }

    //Teste para não alterar a permissão do usuário quando ele não for encontrado pelo ID
    @Test
    void naoDeveAlterarPermissaoSeOUsuarioNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        EditarPermissaoDTO editarDTO = new EditarPermissaoDTO(
                Permissao.ADMIN
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.editarPermissao(1, editarDTO)
        );

        assertEquals(
                "Usuário com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(listarUsuarioMapper, never()).toDTO(any());

    }

    //Teste para deletar um usuário do banco de dados
    @Test
    void deveDeletarUsuario(){

        when(repository.findById(1))
                .thenReturn(Optional.of(usuario));

        service.deletarUsuario(1);

        verify(repository).findById(1);
        verify(repository).delete(usuario);

    }

    //Não deve deletar usuário quando ele não for encontrado pelo ID
    @Test
    void naoDeveDeletarUsuario(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.deletarUsuario(1)
        );

        assertEquals(
                "Usuário com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(repository, never()).delete(any());

    }

    //Teste para retornar lista de usuários filtrado pela permissão
    @Test
    void deveRetornarUsuariosPelaPermissao(){

        when(repository.findByPermissao(Permissao.CLIENTE))
                .thenReturn(List.of(usuario));

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPermissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        List<ListarUsuarioDTO> resultado =
                service.listarPermissao(Permissao.CLIENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findByPermissao(Permissao.CLIENTE);
        verify(listarUsuarioMapper).toDTO(usuario);

    }

    //Teste para retornar uma lista vazia de usuarios pela permissao
    @Test
    void deveRetornarListaVaziaDeUsuariosPelaPermissao(){

        when(repository.findByPermissao(Permissao.CLIENTE))
                .thenReturn(List.of());

        List<ListarUsuarioDTO> resultado =
                service.listarPermissao(Permissao.CLIENTE);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findByPermissao(Permissao.CLIENTE);
        verify(listarUsuarioMapper, never()).toDTO(any());

    }

    //Teste para buscar um usuário por ID
    @Test
    void deveRetornarUsuarioPorId(){

        when(repository.findById(1))
                .thenReturn(Optional.of(usuario));

        ListarUsuarioDTO dtoEsperado = new ListarUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPermissao()
        );

        when(listarUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarUsuarioDTO resultado =
                service.buscarId(1);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        verify(repository).findById(1);
        verify(listarUsuarioMapper).toDTO(usuario);

    }

    //Teste para não retornar usuário quando ID não for encontrado
    @Test
    void naoDeveRetornarUsuarioQuandoIDNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarId(1)
        );

        assertEquals(
                "Usuário com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(listarUsuarioMapper, never()).toDTO(any());

    }

}

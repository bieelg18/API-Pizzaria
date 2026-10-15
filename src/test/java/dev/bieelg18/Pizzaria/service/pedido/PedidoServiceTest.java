package dev.bieelg18.Pizzaria.service.pedido;

import dev.bieelg18.Pizzaria.exception.ClienteInvalidoException;
import dev.bieelg18.Pizzaria.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Pizzaria.exception.StatusIncorretoException;
import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.CriarPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.usuario.ClientePedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.ItemPedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import dev.bieelg18.Pizzaria.model.entity.usuario.Permissao;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import dev.bieelg18.Pizzaria.model.mapper.pedido.ListarPedidoAdminMapper;
import dev.bieelg18.Pizzaria.model.mapper.pedido.ListarPedidoMapper;
import dev.bieelg18.Pizzaria.repository.pedido.PedidoRepository;
import dev.bieelg18.Pizzaria.repository.produto.ProdutoRepository;
import dev.bieelg18.Pizzaria.repository.usuario.UsuarioRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListarPedidoMapper listarPedidoMapper;

    @Mock
    private ListarPedidoAdminMapper listarPedidoAdminMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PedidoService service;

    private Produto produto;

    private Pedido pedido;

    private Usuario usuario;

    private ItemPedido itemPedido;

    @BeforeEach
    void setUp(){

        produto = new Produto();
        produto.setId(1);
        produto.setNome("Marguerita");
        produto.setTipoProduto(TipoProduto.PIZZA);
        produto.setPreco(new BigDecimal("29.99"));

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Cliente Teste");
        usuario.setEmail("cliente@email.com");
        usuario.setSenha("Senha123");
        usuario.setEndereco("Rua Teste 10");

        pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(usuario);
        pedido.setStatus(StatusPedido.RECEBIDO);

        itemPedido = new ItemPedido();
        itemPedido.setId(1);
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);
        itemPedido.setPrecoUnitario(produto.getPreco());

        pedido.setItens(List.of(itemPedido));
        pedido.setTotal(new BigDecimal("59.98"));

    }

    //Teste para criar um novo pedido
    @Test
    void deveCriarUmNovoPedido(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        CriarPedidoDTO criarDTO = new CriarPedidoDTO(
                List.of(new CriarItemPedidoDTO(1,2))
        );

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        when(repository.save(any(Pedido.class)))
                .thenReturn(pedido);

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.criarPedido(criarDTO, authentication);

        ArgumentCaptor<Pedido> captor =
                ArgumentCaptor.forClass(Pedido.class);

        verify(repository).save(captor.capture());

        Pedido pedidoCapturado = captor.getValue();

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);
        assertEquals(
                new BigDecimal("59.98"),
                pedidoCapturado.getTotal()
        );

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(listarPedidoMapper).toDTO(pedido);
    }

    //Teste para não criar pedido quando usuário não for encontrado
    @Test
    void naoDeveCriarPedidoQuandoUsuarioNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.empty());

        CriarPedidoDTO criarDTO = new CriarPedidoDTO(
                List.of(new CriarItemPedidoDTO(1,2))
        );

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criarPedido(criarDTO, authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(produtoRepository, never()).findById(anyInt());
        verify(repository, never()).save(any());
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não criar pedido quando o produto não for encontrado
    @Test
    void naoDeveCriarPedidoQuandoProdutoNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        CriarPedidoDTO criarDTO = new CriarPedidoDTO(
                List.of(new CriarItemPedidoDTO(1,2))
        );

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        when(produtoRepository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criarPedido(criarDTO, authentication)
        );

        assertEquals(
                "Produto não encontrado",
                exception.getMessage()
        );

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(produtoRepository).findById(1);
        verify(repository, never()).save(any());
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Método para listar pedidos do usuário que fez a requisição
    @Test
    void deveRetornarPedidosDoUsuarioQueChamouARequisicao(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        when(repository.findByCliente(usuario))
                .thenReturn(List.of(pedido));

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        List<ListarPedidoDTO> resultado =
                service.listarPedidosUsuario(authentication);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository).findByCliente(usuario);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para não retornar lista de pedidos quando o usuário não for encontrado
    @Test
    void naoDeveRetornarListaDePedidosQuandoUsuarioNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.listarPedidosUsuario(authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository, never()).findByCliente(any());
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para listar todos os pedidos existentes
    @Test
    void deveListarPedidosExistentes(){

        when(repository.findAll())
                .thenReturn(List.of(pedido));

        ClientePedidoDTO clientePedidoDTO = new ClientePedidoDTO(
                usuario.getNome(),
                usuario.getEmail()
        );

        ListarPedidoAdminDTO dtoEsperado = new ListarPedidoAdminDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus(),
                clientePedidoDTO
        );

        when(listarPedidoAdminMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        List<ListarPedidoAdminDTO> resultado =
                service.listarTodosOsPedidos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findAll();
        verify(listarPedidoAdminMapper).toDTO(pedido);

    }

    //Teste para retornar uma lista vazia de todos os pedidos existentes
    @Test
    void deveRetornarListaVaziaDePedidosExistentes(){

        when(repository.findAll())
                .thenReturn(List.of());

        List<ListarPedidoAdminDTO> resultado =
                service.listarTodosOsPedidos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAll();
        verify(listarPedidoAdminMapper, never()).toDTO(any());

    }

    //Método para deletar um pedido
    @Test
    void deveDeletarUmPedido(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        service.deletarPedido(1);

        verify(repository).findById(1);
        verify(repository).delete(pedido);

    }

    //Teste para não deletar pedido quando não encontrar por ID
    @Test
    void naoDeveDeletarPedidoQuandoIDNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.deletarPedido(1)
        );

        assertEquals(
                "Pedido com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(repository, never()).delete(any());

    }

    //Teste para cancelar um pedido
    @Test
    void deveCancelarUmPedido(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                StatusPedido.CANCELADO
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.cancelarPedido(1, authentication);

        assertNotNull(resultado);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository).findById(1);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para não cancelar pedido caso ele pertença a outro cliente
    @Test
    void naoDeveCancelarPedidoDeOutroCliente(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        Usuario cliente2 = new Usuario();
        cliente2.setId(2);
        cliente2.setNome("Cliente Teste 2");
        cliente2.setEmail("cliente2@email.com");
        cliente2.setSenha("Senha456");
        cliente2.setEndereco("Rua Brasil, 20");
        cliente2.setPermissao(Permissao.CLIENTE);

        pedido.setCliente(cliente2);

        ClienteInvalidoException exception = assertThrows(
                ClienteInvalidoException.class,
                () -> service.cancelarPedido(1, authentication)
        );

        assertEquals(
                "Não é possível cancelar esse pedido pois ele pertence a outro cliente",
                exception.getMessage()
        );

        assertEquals(cliente2, pedido.getCliente());
        assertNotEquals(StatusPedido.CANCELADO, pedido.getStatus());

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não cancelar pedido caso o status seja diferente de RECEBIDO e CONFIRMADO
    @Test
    void naoDeveCancelarPedidoComStatusIncorreto(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.EM_PRODUCAO);

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> service.cancelarPedido(1, authentication)
        );

        assertEquals(
                "Não é possível cancelar este pedido",
                exception.getMessage()
        );

        assertEquals(StatusPedido.EM_PRODUCAO, pedido.getStatus());
        assertNotEquals(StatusPedido.CANCELADO, pedido.getStatus());

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não cancelar pedido quando usuário não for encontrado
    @Test
    void naoDeveCancelarPedidoQuandoUsuarioNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.cancelarPedido(1, authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CANCELADO, pedido.getStatus());

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository, never()).findById(anyInt());
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não cancelar um pedido quando o pedido não for encontrado
    @Test
    void naoDeveCancelarPedidoQuandoNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn(usuario.getEmail());

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.cancelarPedido(1, authentication)
        );

        assertEquals(
                "Pedido com o número 1 não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CANCELADO, pedido.getStatus());

        verify(authentication).getName();
        verify(usuarioRepository).findByEmail(usuario.getEmail());
        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para listar pedidos por status
    @Test
    void deveListarPedidosPorStatus(){

        when(repository.findByStatus(pedido.getStatus()))
                .thenReturn(List.of(pedido));

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        List<ListarPedidoDTO> resultado =
                service.listarStatus(pedido.getStatus());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findByStatus(pedido.getStatus());
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para retornar uma lista vazia de pedidos por status
    @Test
    void deveRetornarListaVaziaDePedidosPorStatus(){

        when(repository.findByStatus(pedido.getStatus()))
                .thenReturn(List.of());

        List<ListarPedidoDTO> resultado =
                service.listarStatus(pedido.getStatus());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findByStatus(pedido.getStatus());
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para alterar o status de um pedido para confirmado
    @Test
    void deveAlterarStatusParaConfirmado(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                StatusPedido.CONFIRMADO
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.confirmado(1);

        assertNotNull(resultado);
        assertEquals(StatusPedido.CONFIRMADO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para não altera status para confirmado quando pedido não for encontrado
    @Test
    void naoDeveAlterarStatusQuandoPedidoNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.confirmado(1)
        );

        assertEquals(
                "Pedido com o ID 1 não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CONFIRMADO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não alterar status para confirmado
    @Test
    void naoDeveAlterarStatusParaConfirmado(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.EM_PRODUCAO);

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> service.confirmado(1)
        );

        assertEquals(
                "Não é possível alterar o status desse pedido",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CONFIRMADO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para alterar status para em produção
    @Test
    void deveAlterarStatusParaEmProducao(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.CONFIRMADO);

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                pedido.getStatus()
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.producao(1);

        assertNotNull(resultado);
        assertEquals(StatusPedido.EM_PRODUCAO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para nao alterar status do pedido para em producao quando o pedido nao for encontrado
    @Test
    void naoDeveAlterarStatusEmProducaoPedidoNaoEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.producao(1)
        );

        assertEquals(
                "Pedido com o ID 1 não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.EM_PRODUCAO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());
    }

    //Teste para nao alterar o status para em produção quando o status estiver incorreto
    @Test
    void naoDeveAlterarStatusParaEmProducaoComStatusIncorreto(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> service.producao(1)
        );

        assertEquals(
                "Não é possível alterar o status desse pedido",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.EM_PRODUCAO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para alterar status do pedido para saiu para entrega
    @Test
    void deveAlterarStatusDoPedidoParaSaiuParaEntrega(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.EM_PRODUCAO);

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                StatusPedido.SAIU_PARA_ENTREGA
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.entrega(1);

        assertNotNull(resultado);
        assertEquals(StatusPedido.SAIU_PARA_ENTREGA, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para não alterar status do pedido para saiu para entrega quando o pedido não for encontrado
    @Test
    void naoDeveAlterarStatusParaEntregaQuandoPedidoNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.entrega(1)
        );

        assertEquals(
                "Pedido com o ID 1 não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.SAIU_PARA_ENTREGA, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não altera status para entrega quando status for incorreto
    @Test
    void naoDeveAlterarStatusEntregaQuandoEstiverIncorreto(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> service.entrega(1)
        );

        assertEquals(
                "Não é possível alterar o status desse pedido",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.SAIU_PARA_ENTREGA, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para alterar o status para concluido
    @Test
    void deveAlterarStatusParaConcluido(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);

        ListarPedidoDTO dtoEsperado = new ListarPedidoDTO(
                1,
                List.of(new CriarItemPedidoDTO(1,2)),
                pedido.getTotal(),
                StatusPedido.CONCLUIDO
        );

        when(listarPedidoMapper.toDTO(pedido))
                .thenReturn(dtoEsperado);

        ListarPedidoDTO resultado =
                service.concluido(1);

        assertNotNull(resultado);
        assertEquals(StatusPedido.CONCLUIDO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper).toDTO(pedido);

    }

    //Teste para não alterar status para concluido quando pedido não for encontrado
    @Test
    void naoDeveAlterarStatusConcluidoQuandoPedidoNaoForEncontrado(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.concluido(1)
        );

        assertEquals(
                "Pedido com o ID 1 não encontrado",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CONCLUIDO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

    //Teste para não alterar o status para concluido quando estiver incorreto
    @Test
    void naoDeveAlterarStatusConcluidoQuandoEstiverIncorreto(){

        when(repository.findById(1))
                .thenReturn(Optional.of(pedido));

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> service.concluido(1)
        );

        assertEquals(
                "Não é possível alterar o status desse pedido",
                exception.getMessage()
        );

        assertNotEquals(StatusPedido.CONCLUIDO, pedido.getStatus());

        verify(repository).findById(1);
        verify(listarPedidoMapper, never()).toDTO(any());

    }

}

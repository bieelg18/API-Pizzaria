package dev.bieelg18.Pizzaria.service.produto;

import dev.bieelg18.Pizzaria.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Pizzaria.model.dto.produto.CriarProdutoDTO;
import dev.bieelg18.Pizzaria.model.dto.produto.ListarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import dev.bieelg18.Pizzaria.model.mapper.produto.CriarProdutoMapper;
import dev.bieelg18.Pizzaria.model.mapper.produto.ListarProdutoMapper;
import dev.bieelg18.Pizzaria.repository.produto.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private CriarProdutoMapper criarProdutoMapper;

    @Mock
    private ListarProdutoMapper listarProdutoMapper;

    @InjectMocks
    private ProdutoService service;

    private Produto produto;

    @BeforeEach
    void setUp(){

        produto = new Produto();
        produto.setId(1);
        produto.setNome("Marguerita");
        produto.setTipoProduto(TipoProduto.PIZZA);
        produto.setPreco(new BigDecimal("39.99"));

    }

    //Teste para cadastrar um novo produto
    @Test
    void deveCriarNovoProduto(){

        CriarProdutoDTO criarDTO = new CriarProdutoDTO(
                produto.getNome(),
                produto.getTipoProduto(),
                produto.getPreco()
        );

        when(criarProdutoMapper.toEntity(criarDTO))
                .thenReturn(produto);

        ListarProdutoDTO dtoEsperado = new ListarProdutoDTO(
                1,
                criarDTO.nome(),
                criarDTO.tipoProduto(),
                criarDTO.preco()
        );

        when(repository.save(produto))
                .thenReturn(produto);

        when(listarProdutoMapper.toDTO(produto))
                .thenReturn(dtoEsperado);

        ListarProdutoDTO resultado =
                service.criarProduto(criarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        verify(criarProdutoMapper).toEntity(criarDTO);
        verify(repository).save(produto);
        verify(listarProdutoMapper).toDTO(produto);

    }

    //Teste para listar todos os produtos existentes no banco de dados
    @Test
    void deveListarTodosOsProdutos(){

        when(repository.findAll())
                .thenReturn(List.of(produto));

        ListarProdutoDTO dtoEsperdo = new ListarProdutoDTO(
                1,
                produto.getNome(),
                produto.getTipoProduto(),
                produto.getPreco()
        );

        when(listarProdutoMapper.toDTO(produto))
                .thenReturn(dtoEsperdo);


        List<ListarProdutoDTO> resultado =
                service.listarProdutos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperdo, resultado.get(0));

        verify(repository).findAll();
        verify(listarProdutoMapper).toDTO(produto);

    }

    //Teste para retornar uma lista vazia de produtos
    @Test
    void deveRetornarListaVaziaDeProdutos(){

        when(repository.findAll())
                .thenReturn(List.of());

        List<ListarProdutoDTO> resultado =
                service.listarProdutos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAll();
        verify(listarProdutoMapper, never()).toDTO(any());

    }

    //Teste para editar um produto já cadastrado
    @Test
    void deveEditarProduto(){

        when(repository.findById(1))
                .thenReturn(Optional.of(produto));

        CriarProdutoDTO editarDTO = new CriarProdutoDTO(
                "Cinco Queijos",
                TipoProduto.PIZZA,
                new BigDecimal("49.99")
        );

        ListarProdutoDTO dtoEsperado = new ListarProdutoDTO(
                1,
                editarDTO.nome(),
                editarDTO.tipoProduto(),
                editarDTO.preco()
        );

        when(listarProdutoMapper.toDTO(produto))
                .thenReturn(dtoEsperado);

        ListarProdutoDTO resultado =
                service.editarProduto(1, editarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(dtoEsperado.nome(), produto.getNome());
        assertEquals(dtoEsperado.preco(), produto.getPreco());

        verify(repository).findById(1);
        verify(repository, never()).save(any());
        verify(listarProdutoMapper).toDTO(produto);

    }

    //Teste para não editar um produto quando ele não for encontrado pelo ID
    @Test
    void naoDeveEditarUmProduto(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        CriarProdutoDTO editarDTO = new CriarProdutoDTO(
                produto.getNome(),
                produto.getTipoProduto(),
                produto.getPreco()
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.editarProduto(1, editarDTO)
        );

        assertEquals(
                "Produto com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(listarProdutoMapper, never()).toDTO(any());

    }

    //Teste para deletar um produto
    @Test
    void deveDeletarUmProduto(){

        when(repository.findById(1))
                .thenReturn(Optional.of(produto));

        service.deletarProduto(1);

        verify(repository).findById(1);
        verify(repository).delete(produto);

    }

    //Teste para não deletar um produto quando o ID não for encontrado
    @Test
    void naoDeveDeletarUsuario(){

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.deletarProduto(1)
        );

        assertEquals(
                "Produto com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(repository).findById(1);
        verify(repository, never()).delete(any());
    }

    //Teste para listar produtos filtrado pelo tipo
    @Test
    void deveListarProdutosPeloTipo(){

        when(repository.findByTipoProduto(TipoProduto.PIZZA))
                .thenReturn(List.of(produto));

        ListarProdutoDTO dtoEsperado = new ListarProdutoDTO(
                1,
                produto.getNome(),
                produto.getTipoProduto(),
                produto.getPreco()
        );

        when(listarProdutoMapper.toDTO(produto))
                .thenReturn(dtoEsperado);

        List<ListarProdutoDTO> resultado =
                service.listarTipos(TipoProduto.PIZZA);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findByTipoProduto(TipoProduto.PIZZA);
        verify(listarProdutoMapper).toDTO(produto);
    }

    //Teste para retornar uma lista vazia de produtos por tipo do produto
    @Test
    void deveRetornarListaVaziaDeProdutosPorTipo(){

        when(repository.findByTipoProduto(TipoProduto.PIZZA))
                .thenReturn(List.of());

        List<ListarProdutoDTO> resultado =
                service.listarTipos(TipoProduto.PIZZA);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findByTipoProduto(TipoProduto.PIZZA);
        verify(listarProdutoMapper, never()).toDTO(any());

    }

    //Teste para retornar lista de produtos pelo nome
    @Test
    void deveRetornarProdutosPeloNome(){

        when(repository.findByNomeContainingIgnoreCase(produto.getNome()))
                .thenReturn(List.of(produto));

        ListarProdutoDTO dtoEsperado = new ListarProdutoDTO(
                1,
                produto.getNome(),
                produto.getTipoProduto(),
                produto.getPreco()
        );

        when(listarProdutoMapper.toDTO(produto))
                .thenReturn(dtoEsperado);

        List<ListarProdutoDTO> resultado =
                service.listarNome(produto.getNome());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(repository).findByNomeContainingIgnoreCase(produto.getNome());
        verify(listarProdutoMapper).toDTO(produto);

    }

    //Teste para retornar uma lista vazia de produtos pelo nome
    @Test
    void deveRetornarListaVaziaDeProdutosPeloNome(){

        when(repository.findByNomeContainingIgnoreCase(produto.getNome()))
                .thenReturn(List.of());

        List<ListarProdutoDTO> resultado =
                service.listarNome(produto.getNome());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findByNomeContainingIgnoreCase(produto.getNome());
        verify(listarProdutoMapper, never()).toDTO(any());

    }

}

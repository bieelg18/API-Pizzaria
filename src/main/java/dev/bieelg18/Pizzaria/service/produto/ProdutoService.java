package dev.bieelg18.Pizzaria.service.produto;

import dev.bieelg18.Pizzaria.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Pizzaria.model.dto.produto.CriarProdutoDTO;
import dev.bieelg18.Pizzaria.model.dto.produto.ListarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import dev.bieelg18.Pizzaria.model.mapper.produto.CriarProdutoMapper;
import dev.bieelg18.Pizzaria.model.mapper.produto.ListarProdutoMapper;
import dev.bieelg18.Pizzaria.repository.produto.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CriarProdutoMapper criarProdutoMapper;
    private final ListarProdutoMapper listarProdutoMapper;

    //Método para criar um novo produto no banco de dados
    public ListarProdutoDTO criarProduto(CriarProdutoDTO criarDTO){

        Produto produto = criarProdutoMapper.toEntity(criarDTO);
        Produto produtoSalvo = produtoRepository.save(produto);

        return listarProdutoMapper.toDTO(produtoSalvo);

    }

    //Método para listar todos os produtos cadastrados
    public List<ListarProdutoDTO> listarProdutos(){

        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(listarProdutoMapper::toDTO)
                .toList();

    }

    //Método para editar um produto já cadastrado
    public ListarProdutoDTO editarProduto(Integer id, CriarProdutoDTO editarDTO){

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto com o ID " + id + " não encontrado"
                ));

        if (editarDTO.nome() != null){
            produto.setNome(editarDTO.nome());
        }
        if (editarDTO.tipoProduto() != null){
            produto.setTipoProduto(editarDTO.tipoProduto());
        }
        if (editarDTO.preco() != null){
            produto.setPreco(editarDTO.preco());
        }

        Produto produtoSalvo = produtoRepository.save(produto);

        return listarProdutoMapper.toDTO(produtoSalvo);

    }

    //Método para deletar um produto cadastrado
    public void deletarProduto(Integer id){

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto com o ID " + id + " não encontrado"
                ));

        produtoRepository.delete(produto);

    }

    //Método para listar os produtos pelo tipo do produto
    public List<ListarProdutoDTO> listarTipos(TipoProduto tipoProduto){

        List<Produto> produtos = produtoRepository.findByTipoProduto(tipoProduto);

        return produtos.stream()
                .map(listarProdutoMapper::toDTO)
                .toList();

    }

    //Método para listar produtos pelo nome
    public List<ListarProdutoDTO> listarNome(String nome){

        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);

        return produtos.stream()
                .map(listarProdutoMapper::toDTO)
                .toList();

    }

}

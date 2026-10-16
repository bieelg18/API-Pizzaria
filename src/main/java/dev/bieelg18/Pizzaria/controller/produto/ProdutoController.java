package dev.bieelg18.Pizzaria.controller.produto;

import dev.bieelg18.Pizzaria.documentation.produto.ProdutoControllerDocs;
import dev.bieelg18.Pizzaria.model.dto.produto.CriarProdutoDTO;
import dev.bieelg18.Pizzaria.model.dto.produto.ListarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.TipoProduto;
import dev.bieelg18.Pizzaria.service.produto.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController implements ProdutoControllerDocs {

    private final ProdutoService service;


    //Endpoint para cadastrar um novo produto
    @Override
    @PostMapping
    public ListarProdutoDTO criarProduto(@RequestBody CriarProdutoDTO criarDTO){
        return service.criarProduto(criarDTO);
    }

    //Endpoint para listar todos os produtos cadastrados
    @Override
    @GetMapping("/all")
    public List<ListarProdutoDTO> listarProdutos(){
        return service.listarProdutos();
    }

    //Endpoint para editar um produto já cadastrado
    @Override
    @PatchMapping("/{id}")
    public ListarProdutoDTO editarProduto(@PathVariable Integer id, @RequestBody CriarProdutoDTO editarDTO){
        return service.editarProduto(id, editarDTO);
    }

    //Método para deletar um produto cadastrado
    @Override
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Integer id){
        service.deletarProduto(id);
    }

    //Endpoint para listar os produtos pelo tipo do produto
    @Override
    @GetMapping("/all/{tipoProduto}")
    public List<ListarProdutoDTO> listarTipos(@PathVariable TipoProduto tipoProduto){
        return service.listarTipos(tipoProduto);
    }

    //Endpoint para listar produto pelo nome
    @Override
    @GetMapping("/all/")
    public List<ListarProdutoDTO> listarNome(@RequestParam String nome){
        return service.listarNome(nome);
    }

}

package dev.bieelg18.Pizzaria.controller.produto;

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
public class ProdutoController {

    private final ProdutoService service;


    //Endpoint para cadastrar um novo produto
    @PostMapping
    public ListarProdutoDTO criarProduto(@RequestBody CriarProdutoDTO criarDTO){
        return service.criarProduto(criarDTO);
    }

    //Endpoint para listar todos os produtos cadastrados
    @GetMapping("/all")
    public List<ListarProdutoDTO> listarProdutos(){
        return service.listarProdutos();
    }

    //Endpoint para editar um produto já cadastrado
    @PatchMapping("/{id}")
    public ListarProdutoDTO editarProduto(@PathVariable Integer id, @RequestBody CriarProdutoDTO editarDTO){
        return service.editarProduto(id, editarDTO);
    }

    //Método para deletar um produto cadastrado
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Integer id){
        service.deletarProduto(id);
    }

    //Endpoint para listar os produtos pelo tipo do produto
    @GetMapping("/all/{tipoProduto}")
    public List<ListarProdutoDTO> listarTipos(@PathVariable TipoProduto tipoProduto){
        return service.listarTipos(tipoProduto);
    }

    //Endpoint para listar produto pelo nome
    @GetMapping("/all/")
    public List<ListarProdutoDTO> listarNome(@RequestParam String nome){
        return service.listarNome(nome);
    }

}

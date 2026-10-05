package dev.bieelg18.Pizzaria.model.mapper.produto;

import dev.bieelg18.Pizzaria.model.dto.produto.CriarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriarProdutoMapper {

    CriarProdutoDTO toDTO(Produto produto);

    Produto toEntity(CriarProdutoDTO criarProdutoDTO);

}

package dev.bieelg18.Pizzaria.model.mapper.produto;

import dev.bieelg18.Pizzaria.model.dto.produto.ListarProdutoDTO;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListarProdutoMapper {

    ListarProdutoDTO toDTO(Produto produto);

    Produto toEntity(ListarProdutoDTO listarProdutoDTO);

}

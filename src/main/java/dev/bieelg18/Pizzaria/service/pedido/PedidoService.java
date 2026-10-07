package dev.bieelg18.Pizzaria.service.pedido;

import dev.bieelg18.Pizzaria.exception.ClienteInvalidoException;
import dev.bieelg18.Pizzaria.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Pizzaria.exception.StatusIncorretoException;
import dev.bieelg18.Pizzaria.model.dto.itemPedido.CriarItemPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.CriarPedidoDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoAdminDTO;
import dev.bieelg18.Pizzaria.model.dto.pedido.ListarPedidoDTO;
import dev.bieelg18.Pizzaria.model.entity.pedido.ItemPedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.Pedido;
import dev.bieelg18.Pizzaria.model.entity.pedido.StatusPedido;
import dev.bieelg18.Pizzaria.model.entity.produto.Produto;
import dev.bieelg18.Pizzaria.model.entity.usuario.Usuario;
import dev.bieelg18.Pizzaria.model.mapper.pedido.ListarPedidoAdminMapper;
import dev.bieelg18.Pizzaria.model.mapper.pedido.ListarPedidoMapper;
import dev.bieelg18.Pizzaria.repository.pedido.PedidoRepository;
import dev.bieelg18.Pizzaria.repository.produto.ProdutoRepository;
import dev.bieelg18.Pizzaria.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ListarPedidoMapper listarPedidoMapper;
    private final ListarPedidoAdminMapper listarPedidoAdminMapper;
    private final ProdutoRepository produtoRepository;

    //Método para criar um pedido
    public ListarPedidoDTO criarPedido(CriarPedidoDTO criarPedidoDTO, Authentication authentication){

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));



        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setStatus(StatusPedido.RECEBIDO);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CriarItemPedidoDTO itemDTO : criarPedidoDTO.itens()){

            Produto produto = produtoRepository.findById(itemDTO.idProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto não encontrado"
                    ));

            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.quantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());

            itens.add(itemPedido);

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemDTO.quantidade()));

            total = total.add(subtotal);

        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);


    }

    //Método de listar pedidos do usuário que fez a requisição
    public List<ListarPedidoDTO> listarPedidosUsuario(Authentication authentication){

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));

        List<Pedido> pedidos;

        pedidos = pedidoRepository.findByCliente(usuario);

        return pedidos.stream()
                .map(listarPedidoMapper::toDTO)
                .toList();

    }

    //Método para listar todos os pedidos existentes
    public List<ListarPedidoAdminDTO> listarTodosOsPedidos(){

        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream()
                .map(listarPedidoAdminMapper::toDTO)
                .toList();

    }

    //Método para deletar um pedido
    public void deletarPedido(Integer id){

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o ID " + id + " não encontrado"
                ));

        pedidoRepository.delete(pedido);

    }

    //Método para cancelar um pedido
    public ListarPedidoDTO cancelarPedido(Integer numeroPedido, Authentication authentication){

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));

        Pedido pedido = pedidoRepository.findById(numeroPedido)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o número " + numeroPedido + " não encontrado"
                ));

        if (!pedido.getCliente().getId().equals(usuario.getId())){
            throw new ClienteInvalidoException(
                    "Não é possível cancelar esse pedido pois ele pertence a outro cliente"
            );
        }

        if (pedido.getStatus() != StatusPedido.RECEBIDO
        && pedido.getStatus() != StatusPedido.CONFIRMADO){
            throw new StatusIncorretoException(
                    "Não é possível cancelar este pedido"
            );
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);

    }

    //Método para listar pedidos por status
    public List<ListarPedidoDTO> listarStatus(StatusPedido statusPedido){

        List<Pedido> pedidos = pedidoRepository.findByStatus(statusPedido);

        return pedidos.stream()
                .map(listarPedidoMapper::toDTO)
                .toList();

    }

    //Método para alterar o status de um pedido para confirmado
    public ListarPedidoDTO confirmado(Integer id){

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o ID " + id + " não encontrado"
                ));

        if (pedido.getStatus() != StatusPedido.RECEBIDO){
            throw new StatusIncorretoException(
                    "Não é possível alterar o status desse pedido"
            );
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);

    }

    //Método para alterar o status de um pedido para em produção
    public ListarPedidoDTO producao(Integer id){

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o ID " + id + " não encontrado"
                ));

        if (pedido.getStatus() != StatusPedido.CONFIRMADO){
            throw new StatusIncorretoException(
                    "Não é possível alterar o status desse pedido"
            );
        }

        pedido.setStatus(StatusPedido.EM_PRODUCAO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);

    }

    //Método para alterar o status de um pedido para saiu para entrega
    public ListarPedidoDTO entrega(Integer id){

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o ID " + id + " não encontrado"
                ));

        if (pedido.getStatus() != StatusPedido.EM_PRODUCAO){
            throw new StatusIncorretoException(
                    "Não é possível alterar o status desse pedido"
            );
        }

        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);

    }

    //Método para alterar o status de um pedido para concluido
    public ListarPedidoDTO concluido(Integer id){

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido com o ID " + id + " não encontrado"
                ));

        if (pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA){
            throw new StatusIncorretoException(
                    "Não é possível alterar o status desse pedido"
            );
        }

        pedido.setStatus(StatusPedido.CONCLUIDO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return listarPedidoMapper.toDTO(pedidoSalvo);

    }



}

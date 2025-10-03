package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.models.movimentacao.Movimentacao;
import com.estoqueige.estoqueige.models.movimentacao.RequestAtualizaMovimentacaoDTO;
import com.estoqueige.estoqueige.models.movimentacao.RequestMovimentacaoDTO;
import com.estoqueige.estoqueige.models.movimentacao.ResponseMovimentacaoDTO;
import com.estoqueige.estoqueige.models.movimentacao.events.MovimentacaoCanceladaEvent;
import com.estoqueige.estoqueige.models.movimentacao.events.MovimentacaoFinalizadaEvent;
import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.requisitante.Requisitante;
import com.estoqueige.estoqueige.models.usuario.Usuario;
import com.estoqueige.estoqueige.repositories.MovimentacaoRepository;
import com.estoqueige.estoqueige.security.UserSpringSecurity;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroAutorizacao;
import com.estoqueige.estoqueige.services.exceptions.ErroMovimentacaoCancelada;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimentacaoServices {
    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoServices produtoServices;
    private final UsuarioServices usuarioServices;
    private final RequisitanteServices requisitanteServices;
    private final ApplicationEventPublisher eventPublisher; 


    /* Método services */

    public ResponseMovimentacaoDTO retornarMovimentacaoDto(Long id){
        Movimentacao movimentacao = this.buscarMovimentacaoPorId(id);

        return ResponseMovimentacaoDTO.fromEntity(movimentacao);
    }

    public Movimentacao buscarMovimentacaoPorId(Long id){
        Movimentacao movimentacao = this.movimentacaoRepository.findById(id)
            .orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar Movimentacao de código: "+ id));

        return movimentacao;
    }

    public List<ResponseMovimentacaoDTO> buscarTodasMovimentacoes(String tipo, String status){
        MovTipo movTipo = MovTipo.movTipo(tipo);
        MovStatus movStatus = MovStatus.movStatus(status);
        List<Movimentacao> movimentacoes = this.movimentacaoRepository.buscarMovimentacaosPorTipo(movTipo.name(), movStatus.name());

        return ResponseMovimentacaoDTO.fromEntityList(movimentacoes);
    }

    //Método para salvar a movimentação no Banco de Dados
    @Transactional
    public ResponseMovimentacaoDTO salvarMovimentacao(RequestMovimentacaoDTO dto) {

        //Puxar Usuario do Context
        UserSpringSecurity userSpringSecurity = UsuarioServices.autenticado();
        if(Objects.isNull(userSpringSecurity)){
            throw new ErroAutorizacao("Acesso negado!");
        }
        Usuario usuario = this.usuarioServices.buscarUsuarioPorId(userSpringSecurity.getId());

        Requisitante requisitante = this.requisitanteServices.buscarRequisitantePorId(dto.movRequisitanteId());

        List<Long> idsProdutos = dto.produtosMov().stream()
            .map(item -> item.proMovProduto())
            .toList();

        if (idsProdutos.isEmpty()) {
            throw new ErroValidacaoLogica("A movimentação deve conter pelo menos um item.");
        }

        List<Produto> produtos = this.produtoServices.buscarProdutosPorIds(idsProdutos);

        if (produtos.size() != idsProdutos.size()) {
            throw new ErroAoBuscarObjetos("Um ou mais IDs de produto fornecidos são inválidos.");
        }

        Map<Long, Produto> produtosMap = produtos.stream()
            .collect(Collectors.toMap(Produto::getProId, produto -> produto));

        List<Movimentacao.ItemParaMovimentacao> itensParaMovimentacao = dto.produtosMov().stream()
            .map(itemDto -> {
                // Para cada item do comando, pegamos o objeto Produto correspondente no mapa.
                Produto produtoCompleto = produtosMap.get(itemDto.proMovProduto());
                
                // Criamos o objeto auxiliar com a entidade `Produto` instanciada.
                return new Movimentacao.ItemParaMovimentacao(
                    produtoCompleto, 
                    itemDto.proMovQtdProduto(), 
                    itemDto.proMovCustoProduto()
                );
            })
            .collect(Collectors.toList());
        
        Movimentacao movimentacao = Movimentacao.criar(usuario, requisitante, dto, itensParaMovimentacao);
     
        movimentacao = this.movimentacaoRepository.save(movimentacao);

        //Anunciando a criação da movimentação para todos as minhas classes dependentes
        this.eventPublisher.publishEvent(new MovimentacaoFinalizadaEvent(movimentacao.getMovId()));
        return ResponseMovimentacaoDTO.fromEntity(movimentacao);
    }

    @Transactional
    public Movimentacao atualizarMovimentacao(Long id, RequestAtualizaMovimentacaoDTO dto){
        Movimentacao newMovimentacao = this.buscarMovimentacaoPorId(id);
        newMovimentacao.setMovNf(dto.movNf());
        newMovimentacao.setMovNumRequisicao(dto.movNumRequisicao());
        newMovimentacao.setMovObservacao(dto.movObservacao());

        return this.movimentacaoRepository.save(newMovimentacao);
    }

    @Transactional
    public void cancelarMovimentacao(Long idMovimentacao){
        if(!usuarioServices.validarUsuario("Usuário não tem permissão para cancelar movimentacao.")){
            return;
        }

        Movimentacao movimentacao = this.buscarMovimentacaoPorId(idMovimentacao);
        if(movimentacao.getMovStatus() == MovStatus.CANCELADO){
            throw new ErroMovimentacaoCancelada("Não é possível cancelar a movimentação pois a mesma já está cancelada.");
        }
        movimentacao.setMovDataCancelamento(LocalDate.now());
        movimentacao.setMovHorarioCancelamento(LocalTime.now());
        movimentacao.setMovStatus(MovStatus.CANCELADO);
        this.movimentacaoRepository.save(movimentacao);
        
        //Anunciando o cancelamento da movimentação para todos as minhas classes dependentes
        this.eventPublisher.publishEvent(new MovimentacaoCanceladaEvent(movimentacao.getMovId()));
    }
}
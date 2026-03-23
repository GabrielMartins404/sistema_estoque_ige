package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.models.logMovimentacaoEstoque.LogMovimentacaoEstoque;
import com.estoqueige.estoqueige.models.movimentacao.Movimentacao;
import com.estoqueige.estoqueige.models.movimentacao.events.MovimentacaoCanceladaEvent;
import com.estoqueige.estoqueige.models.movimentacao.events.MovimentacaoFinalizadaEvent;
import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.produtoMovimentacao.ProdutoMovimentacao;
import com.estoqueige.estoqueige.repositories.MovimentacaoEstoqueRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EstoqueListener {
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final MovimentacaoServices movimentacaoServices;

    /* Métodos services */

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMovimentacaoFinalizada(MovimentacaoFinalizadaEvent event){
        Movimentacao movimentacao = this.movimentacaoServices.buscarMovimentacaoPorId(event.getMovimentacaoId());
  
        for(var item : movimentacao.getProdutosMov()){
            Produto produto = item.getProMovProduto();
            Float qtdAnterior = produto.getProQtd();
            if(movimentacao.getMovTipo() == MovTipo.ENTRADA){
                produto.darEntrada(item.getProMovQtdProduto());
            }else{
                produto.darSaida(item.getProMovQtdProduto());
            }
            this.criarLogMovimentacaoEstoque(item, qtdAnterior);
        }
        
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMovimentacaoCancelada(MovimentacaoCanceladaEvent event){
        Movimentacao movimentacao = this.movimentacaoServices.buscarMovimentacaoPorId(event.getMovimentacaoId());

        for(var item : movimentacao.getProdutosMov()){
            Produto produto = item.getProMovProduto();
            Float qtdAnterior = produto.getProQtd();
            if(movimentacao.getMovTipo() == MovTipo.ENTRADA){
                produto.darSaida(item.getProMovQtdProduto());
                item.getProMovMovimentacao().setMovTipo(MovTipo.SAIDA);
            }else{
                produto.darEntrada(item.getProMovQtdProduto());
                item.getProMovMovimentacao().setMovTipo(MovTipo.ENTRADA);
            }

            this.criarLogMovimentacaoEstoque(item, qtdAnterior);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void criarLogMovimentacaoEstoque(ProdutoMovimentacao produtoMovimentacao, Float qtdAnterior){
        LogMovimentacaoEstoque logMovimentacaoEstoque = new LogMovimentacaoEstoque();
        logMovimentacaoEstoque.setMovEstData(LocalDate.now());
        logMovimentacaoEstoque.setMovEstHorario(LocalTime.now());
        logMovimentacaoEstoque.setMovEstQtd(produtoMovimentacao.getProMovQtdProduto());
        logMovimentacaoEstoque.setMovEstTipo(produtoMovimentacao.getProMovMovimentacao().getMovTipo());
        logMovimentacaoEstoque.setMovEstStatus(produtoMovimentacao.getProMovMovimentacao().getMovStatus());
        logMovimentacaoEstoque.setMovEstMovimentacao(produtoMovimentacao.getProMovMovimentacao());
        logMovimentacaoEstoque.setMovEstProduto(produtoMovimentacao.getProMovProduto());
        logMovimentacaoEstoque.setMovEstQtdAnterior(qtdAnterior);
        logMovimentacaoEstoque.setMovEstQtdPosterior(produtoMovimentacao.getProMovProduto().getProQtd());

        this.movimentacaoEstoqueRepository.save(logMovimentacaoEstoque);
    }

}

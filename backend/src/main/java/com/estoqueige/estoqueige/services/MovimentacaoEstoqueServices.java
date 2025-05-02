package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.models.Movimentacao;
import com.estoqueige.estoqueige.models.MovimentacaoEstoque;
import com.estoqueige.estoqueige.models.Produto;
import com.estoqueige.estoqueige.models.ProdutoMovimentacao;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.repositories.MovimentacaoEstoqueRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroCamposFixos;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MovimentacaoEstoqueServices {
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    private final ProdutoServices produtoServices;

    public MovimentacaoEstoqueServices(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, ProdutoServices produtoServices) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.produtoServices = produtoServices;
    }

    /* Métodos services */

    public Float calcularQtdPosterior(Movimentacao movimentacao, Float qtdAtual, Float qtdMovimentada){
        Float qtdPosterior = 0f;

        if(movimentacao.getMovTipo() == MovTipo.ENTRADA && movimentacao.getMovStatus() == MovStatus.FINALIZADO){
            qtdPosterior = qtdAtual + qtdMovimentada;
        }else if(movimentacao.getMovTipo() == MovTipo.SAIDA && movimentacao.getMovStatus()== MovStatus.FINALIZADO){
            qtdPosterior = qtdAtual - qtdMovimentada;
        }else if(movimentacao.getMovStatus() == MovStatus.CANCELADO){
            if(movimentacao.getMovTipo() == MovTipo.ENTRADA){
                qtdPosterior = qtdAtual - qtdMovimentada;
            }else{
                qtdPosterior = qtdAtual + qtdMovimentada;
            }
        }
        return qtdPosterior;
    }

    @Transactional
    public void salvarMovimentacaoEstoque(Movimentacao movimentacao, ProdutoMovimentacao produtoMovimentacao) {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();

        movimentacaoEstoque.setMovEstData(LocalDate.now());
        movimentacaoEstoque.setMovEstHorario(LocalTime.now());
        movimentacaoEstoque.setMovEstQtd(produtoMovimentacao.getProMovQtdProduto());
        movimentacaoEstoque.setMovEstTipo(movimentacao.getMovTipo());
        movimentacaoEstoque.setMovEstStatus(MovStatus.FINALIZADO);
        movimentacaoEstoque.setMovEstMovimentacao(movimentacao);
        movimentacaoEstoque.setMovEstProduto(produtoMovimentacao.getProMovProduto());

        Produto produto = this.produtoServices.buscarProdutoPorId(produtoMovimentacao.getProMovProduto().getProId());

        movimentacaoEstoque.setMovEstQtdAnterior(produto.getProQtd());
        Float qtdPosterior = calcularQtdPosterior(movimentacao, produto.getProQtd(), produtoMovimentacao.getProMovQtdProduto());

        produto.setProQtd(qtdPosterior);
        movimentacaoEstoque.setMovEstQtdPosterior(qtdPosterior);


        if(movimentacao.getMovStatus() == MovStatus.FINALIZADO || movimentacao.getMovStatus() == MovStatus.CANCELADO){
            if(movimentacao.getMovTipo() != MovTipo.ENTRADA && movimentacao.getMovTipo() != MovTipo.SAIDA){
                throw new ErroCamposFixos("Tipo de movimentação inválido. Deve ser 'E' (Entrada) ou 'S' (Saida).");
            }else{
                if(movimentacao.getMovStatus() == MovStatus.CANCELADO){
                    movimentacaoEstoque.setMovEstStatus(MovStatus.CANCELADO);
                    if(movimentacao.getMovTipo() == MovTipo.ENTRADA){
                        movimentacaoEstoque.setMovEstTipo(MovTipo.SAIDA);
                    }else{
                        movimentacaoEstoque.setMovEstTipo(MovTipo.ENTRADA);
                    }
                }
                if(produto.getProQtd() <=    produto.getProEstoqueMin()){
                    produto.setIsAbaixoMin(true);
                }else{
                    produto.setIsAbaixoMin(false);
                }
                this.produtoServices.atualizarProduto(produto);
            }
            
        }else{
            throw new ErroCamposFixos("Tipo de status inválido. Deve ser 'F' (finalizado) ou 'C' (cancelado).");
        }
        this.movimentacaoEstoqueRepository.save(movimentacaoEstoque);
    }

}

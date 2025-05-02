package com.estoqueige.estoqueige.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.repositories.MovimentacaoRepository;
import com.estoqueige.estoqueige.repositories.ProdutoRepository;
import com.estoqueige.estoqueige.repositories.projections.ResumoQtdProdutoMovimentadoProjection;
import com.estoqueige.estoqueige.repositories.projections.ResumoRequisitantesProdutosProjection;

@Service
public class RelatorioServices {
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public Long buscarQtdMov(String tipo, String status){
        MovTipo movTipo = MovTipo.movTipo(tipo);
        MovStatus movStatus = MovStatus.movStatus(status);

        return this.movimentacaoRepository.buscarQtdMov(movTipo.name(), movStatus.name());
    }

    public Long buscarQtdProdutosAtivos(){
        return this.produtoRepository.buscarQtdProdutosAtivos();
    }

    public Long buscarProdutosAbaixoMin(){
        return this.produtoRepository.buscarProdutosAbaixoMin();
    }

    public List<ResumoQtdProdutoMovimentadoProjection> buscarProdutosMaisMovimentados(){
        return this.produtoRepository.buscarProdutosMaisMovimentados();
    }

    public List<ResumoRequisitantesProdutosProjection> buscarRequisitantesComMaisProdutos(){
        return this.movimentacaoRepository.buscarRequisitantesComMaisProdutos();
    }
}

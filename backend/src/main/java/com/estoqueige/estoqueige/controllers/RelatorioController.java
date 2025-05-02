package com.estoqueige.estoqueige.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estoqueige.estoqueige.repositories.projections.ResumoQtdProdutoMovimentadoProjection;
import com.estoqueige.estoqueige.repositories.projections.ResumoRequisitantesProdutosProjection;
import com.estoqueige.estoqueige.services.RelatorioServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/relatorios")
public class RelatorioController {
    @Autowired
    private RelatorioServices relatorioServices;

    @GetMapping("/buscarQtdMov")
    public ResponseEntity<Long> buscarQtdMov(@RequestParam String tipo, @RequestParam String status) {
        Long qtdMov = this.relatorioServices.buscarQtdMov(tipo, status);
        return ResponseEntity.ok().body(qtdMov);
    }

    @GetMapping("/buscarQtdProdutosAtivos")
    public ResponseEntity<Long> buscarQtdProdutosAtivos() {
        Long qtdProduto = this.relatorioServices.buscarQtdProdutosAtivos();
        return ResponseEntity.ok().body(qtdProduto);
    }
    
    @GetMapping("/buscarProdutosAbaixoMin")
    public ResponseEntity<Long> buscarProdutosAbaixoMin() {
        Long qtdProduto = this.relatorioServices.buscarProdutosAbaixoMin();
        return ResponseEntity.ok().body(qtdProduto);
    }

    @GetMapping("/buscarProdutosMaisMovimentados")
    public ResponseEntity<List<ResumoQtdProdutoMovimentadoProjection>> buscarProdutosMaisMovimentados() {
        List<ResumoQtdProdutoMovimentadoProjection> resumo = this.relatorioServices.buscarProdutosMaisMovimentados();
        return ResponseEntity.ok().body(resumo);
    }

    @GetMapping("/buscarRequisitantesComMaisProdutos")
    public ResponseEntity<List<ResumoRequisitantesProdutosProjection>> buscarRequisitantesComMaisProdutos() {
        List<ResumoRequisitantesProdutosProjection> resumo = this.relatorioServices.buscarRequisitantesComMaisProdutos();
        return ResponseEntity.ok().body(resumo);
    }
}

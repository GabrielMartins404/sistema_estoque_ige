package com.estoqueige.estoqueige.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estoqueige.estoqueige.dto.ProdutoDto;
import com.estoqueige.estoqueige.models.Produto;
import com.estoqueige.estoqueige.services.ProdutoServices;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/produto")
public class ProdutoController {
    private final ProdutoServices produtoServices;

    public ProdutoController(ProdutoServices produtoServices) {
        this.produtoServices = produtoServices;
    }

    
    @GetMapping("/{idProduto}")
    public ResponseEntity<Produto> buscarProdutosPorId(@PathVariable Long idProduto) {
        Produto produto = this.produtoServices.buscarProdutoPorId(idProduto);
        return ResponseEntity.ok().body(produto);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProdutoDto>> buscarProdutos(@RequestParam Boolean status) {
        List<ProdutoDto> produtos = this.produtoServices.buscarTodosProdutos(status);
        return ResponseEntity.ok().body(produtos);
    }

    @PostMapping(value = "/",  consumes = "application/json")
    public ResponseEntity<ProdutoDto> criarProduto(@Valid @RequestBody Produto produto){
        ProdutoDto produtoCriado = this.produtoServices.cadastrarProduto(produto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/{idProduto}")
        .buildAndExpand(produto.getProId())
        .toUri();
        return ResponseEntity.created(uri).body(produtoCriado);
    }

    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoDto> atualizarProduto(@Valid @RequestBody Produto produto, @PathVariable Long idProduto){
        produto.setProId(idProduto);
        return ResponseEntity.ok(this.produtoServices.atualizarProduto(produto));
    }

    @PutMapping("/inativar/{idProduto}")
    public ResponseEntity<Void> inativarProduto(@Valid @PathVariable Long idProduto){
        this.produtoServices.alterarStatusAtivoProduto(idProduto);
        return ResponseEntity.noContent().build();
    }
}

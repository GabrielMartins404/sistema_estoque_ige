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

import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.produto.RequestProdutoDTO;
import com.estoqueige.estoqueige.models.produto.ResponseProdutoDTO;
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
    public ResponseEntity<ResponseProdutoDTO> buscarProdutosPorId(@PathVariable Long idProduto) {
        Produto produto = this.produtoServices.buscarProdutoPorId(idProduto);
        return ResponseEntity.ok().body(ResponseProdutoDTO.fromEntity(produto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseProdutoDTO>> buscarProdutos(@RequestParam Boolean status) {
        List<ResponseProdutoDTO> produtos = this.produtoServices.buscarTodosProdutos(status);
        return ResponseEntity.ok().body(produtos);
    }

    @PostMapping(value = "/",  consumes = "application/json")
    public ResponseEntity<ResponseProdutoDTO> criarProduto(@Valid @RequestBody RequestProdutoDTO produtoDTO){
        ResponseProdutoDTO produtoCriado = this.produtoServices.cadastrarProduto(produtoDTO);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/{idProduto}")
        .buildAndExpand(produtoCriado.proId())
        .toUri();
        return ResponseEntity.created(uri).body(produtoCriado);
    }

    @PutMapping("/{idProduto}")
    public ResponseEntity<ResponseProdutoDTO> atualizarProduto(@Valid @RequestBody RequestProdutoDTO produtoDTO, @PathVariable Long idProduto){
        return ResponseEntity.ok(this.produtoServices.atualizarProduto(idProduto, produtoDTO));
    }

    @PutMapping("/inativar/{idProduto}")
    public ResponseEntity<ResponseProdutoDTO> inativarProduto(@Valid @PathVariable Long idProduto){
        ResponseProdutoDTO produto = this.produtoServices.alterarStatusAtivoProduto(idProduto);
        return ResponseEntity.ok().body(produto);
    }
}

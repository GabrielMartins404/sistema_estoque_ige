package com.estoqueige.estoqueige.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estoqueige.estoqueige.models.CategoriaProduto;
import com.estoqueige.estoqueige.services.CategoriaProdutoServices;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping("/categoriaProduto")
public class CategoriaProdutoController {
    private final CategoriaProdutoServices categoriaProdutoServices;

    public CategoriaProdutoController(CategoriaProdutoServices categoriaProdutoServices) {
        this.categoriaProdutoServices = categoriaProdutoServices;
    }

    @GetMapping("/{idCategoriaProduto}")
    public ResponseEntity<CategoriaProduto> buscarCategoriaProdutosPorId(@PathVariable Long idCategoriaProduto) {
        CategoriaProduto categoriaProduto = this.categoriaProdutoServices.buscarCategoriaProdutoPorId(idCategoriaProduto);
        return ResponseEntity.ok().body(categoriaProduto);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoriaProduto>> buscarCategoriaProdutos(@RequestParam Boolean status) {
        List<CategoriaProduto> categoriaProduto = this.categoriaProdutoServices.buscarTodasCategoriaProdutos(status);
        return ResponseEntity.ok().body(categoriaProduto);
    }

    @PostMapping("/")
    public ResponseEntity<CategoriaProduto> criarCategoriaProduto(@Valid @RequestBody CategoriaProduto categoriaProduto){
        CategoriaProduto categoria = this.categoriaProdutoServices.cadastrarCategoriaProduto(categoriaProduto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idCategoriaProduto}").buildAndExpand(categoriaProduto.getCatProId()).toUri();
        return ResponseEntity.created(uri).body(categoria);
    }

    @PutMapping("/{idCategoriaProduto}")
    public ResponseEntity<CategoriaProduto> atualizarCategoriaProduto(@Valid @RequestBody CategoriaProduto categoriaProduto, @PathVariable Long idCategoriaProduto){
        categoriaProduto.setCatProId(idCategoriaProduto);
        return ResponseEntity.ok(this.categoriaProdutoServices.atualizarCategoriaProduto(categoriaProduto));
    }

    @PutMapping("/inativar/{idCategoriaProduto}")
    public ResponseEntity<Void> inativarCategoriaProduto(@Valid @PathVariable Long idCategoriaProduto){
        this.categoriaProdutoServices.alterarStatusAtivoCategoriaProduto(idCategoriaProduto);
        return ResponseEntity.noContent().build();
    }
    
}

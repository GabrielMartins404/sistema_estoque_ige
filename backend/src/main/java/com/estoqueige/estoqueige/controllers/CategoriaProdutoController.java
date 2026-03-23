package com.estoqueige.estoqueige.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.estoqueige.estoqueige.models.categoriaProduto.CategoriaProduto;
import com.estoqueige.estoqueige.models.categoriaProduto.RequestCategoriaProdutoDTO;
import com.estoqueige.estoqueige.models.categoriaProduto.ResponseCategoriaDTO;
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
    public ResponseEntity<ResponseCategoriaDTO> buscarCategoriaProdutosPorId(@PathVariable Long idCategoriaProduto) {
        CategoriaProduto categoriaProduto = this.categoriaProdutoServices.buscarCategoriaProdutoPorId(idCategoriaProduto);
        return ResponseEntity.ok().body(ResponseCategoriaDTO.fromEntity(categoriaProduto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseCategoriaDTO>> buscarCategoriaProdutos(@RequestParam Boolean status) {
        List<ResponseCategoriaDTO> categoriaProduto = this.categoriaProdutoServices.buscarTodasCategoriaProdutos(status);
        return ResponseEntity.ok().body(categoriaProduto);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseCategoriaDTO> criarCategoriaProduto(@Valid @RequestBody RequestCategoriaProdutoDTO categoriaProduto){
        ResponseCategoriaDTO categoria = this.categoriaProdutoServices.cadastrarCategoriaProduto(categoriaProduto);
        return ResponseEntity.ok().body(categoria);
    }

    @PutMapping("/{idCategoriaProduto}")
    public ResponseEntity<ResponseCategoriaDTO> atualizarCategoriaProduto(@Valid @RequestBody RequestCategoriaProdutoDTO categoriaProduto, @PathVariable Long idCategoriaProduto){
        return ResponseEntity.ok(this.categoriaProdutoServices.atualizarCategoriaProduto(idCategoriaProduto, categoriaProduto));
    }

    @PutMapping("/inativar/{idCategoriaProduto}")
    public ResponseEntity<Void> inativarCategoriaProduto(@Valid @PathVariable Long idCategoriaProduto){
        this.categoriaProdutoServices.alterarStatusAtivoCategoriaProduto(idCategoriaProduto);
        return ResponseEntity.noContent().build();
    }
    
}

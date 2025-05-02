package com.estoqueige.estoqueige.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estoqueige.estoqueige.models.UnidadeProduto;
import com.estoqueige.estoqueige.services.UnidadeProdutoServices;

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
@RequestMapping("/unidadeProduto")
public class UnidadeProdutoController {
    private final UnidadeProdutoServices unidadeProdutoServices;

    public UnidadeProdutoController(UnidadeProdutoServices unidadeProdutoServices) {
        this.unidadeProdutoServices = unidadeProdutoServices;
    }

    @GetMapping("/{idUnidadeProduto}")
    public ResponseEntity<UnidadeProduto> buscarUnidadeProdutosPorId(@PathVariable Long idUnidadeProduto) {
        UnidadeProduto unidadeProduto = this.unidadeProdutoServices.buscarUnidadeProdutoPorId(idUnidadeProduto);
        return ResponseEntity.ok().body(unidadeProduto);
    }

    @GetMapping("/")
    public ResponseEntity<List<UnidadeProduto>> buscarUnidadeProdutos(@RequestParam Boolean status) {
        List<UnidadeProduto> unidadeProdutos = this.unidadeProdutoServices.buscarTodasUnidadeProdutos(status);
        return ResponseEntity.ok().body(unidadeProdutos);
    }

    @PostMapping("/")
    public ResponseEntity<UnidadeProduto> criarUnidadeProduto(@Valid @RequestBody UnidadeProduto unidadeProduto){
        UnidadeProduto unidadeSalva = this.unidadeProdutoServices.cadastrarUnidadeProduto(unidadeProduto);
        
        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{idUnidadeProduto}")
            .buildAndExpand(unidadeSalva.getUnId())
            .toUri();
        return ResponseEntity.created(uri).body(unidadeSalva);
    }

    @PutMapping("/{idUnidadeProduto}")
    public ResponseEntity<UnidadeProduto> atualizarUnidadeProduto(@Valid @RequestBody UnidadeProduto unidadeProduto, @PathVariable Long idUnidadeProduto){
        unidadeProduto.setUnId(idUnidadeProduto);
        return ResponseEntity.ok(this.unidadeProdutoServices.atualizarUnidadeProduto(unidadeProduto));
    }

    @PutMapping("/inativar/{idUnidadeProduto}")
    public ResponseEntity<Void> inativarUnidadeProduto(@Valid @PathVariable Long idUnidadeProduto){
        this.unidadeProdutoServices.alterarStatusAtivoUnidadeProduto(idUnidadeProduto);
        return ResponseEntity.noContent().build();
    }
    
}

package com.estoqueige.estoqueige.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estoqueige.estoqueige.models.unidadeProduto.RequestUnidadeProdutoDTO;
import com.estoqueige.estoqueige.models.unidadeProduto.ResponseUnidadeProdutoDTO;
import com.estoqueige.estoqueige.models.unidadeProduto.UnidadeProduto;
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
    public ResponseEntity<ResponseUnidadeProdutoDTO> buscarUnidadeProdutosPorId(@PathVariable Long idUnidadeProduto) {
        UnidadeProduto unidadeProduto = this.unidadeProdutoServices.buscarUnidadeProdutoPorId(idUnidadeProduto);
        return ResponseEntity.ok().body(ResponseUnidadeProdutoDTO.fromEntity(unidadeProduto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseUnidadeProdutoDTO>> buscarUnidadeProdutos(@RequestParam Boolean status) {
        List<ResponseUnidadeProdutoDTO> unidadeProdutos = this.unidadeProdutoServices.buscarTodasUnidadeProdutos(status);
        return ResponseEntity.ok().body(unidadeProdutos);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseUnidadeProdutoDTO> criarUnidadeProduto(@Valid @RequestBody RequestUnidadeProdutoDTO unidadeProdutoDTO){
        ResponseUnidadeProdutoDTO unidadeSalva = this.unidadeProdutoServices.cadastrarUnidadeProduto(unidadeProdutoDTO);
        
        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{idUnidadeProduto}")
            .buildAndExpand(unidadeSalva.unId())
            .toUri();
        return ResponseEntity.created(uri).body(unidadeSalva);
    }

    @PutMapping("/{idUnidadeProduto}")
    public ResponseEntity<ResponseUnidadeProdutoDTO> atualizarUnidadeProduto(@Valid @RequestBody RequestUnidadeProdutoDTO unidadeProdutoDTO, @PathVariable Long idUnidadeProduto){
        return ResponseEntity.ok(this.unidadeProdutoServices.atualizarUnidadeProduto(idUnidadeProduto, unidadeProdutoDTO));
    }

    @PutMapping("/inativar/{idUnidadeProduto}")
    public ResponseEntity<ResponseUnidadeProdutoDTO> inativarUnidadeProduto(@Valid @PathVariable Long idUnidadeProduto){
        ResponseUnidadeProdutoDTO unidadeProduto = this.unidadeProdutoServices.alterarStatusAtivoUnidadeProduto(idUnidadeProduto);
        return ResponseEntity.ok().body(unidadeProduto);
    }
    
}

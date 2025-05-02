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

import com.estoqueige.estoqueige.dto.RequisitanteDto;
import com.estoqueige.estoqueige.models.Requisitante;
import com.estoqueige.estoqueige.services.RequisitanteServices;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/requisitante")
public class RequisitanteController {

    private final RequisitanteServices requisitanteServices;
    
    public RequisitanteController(RequisitanteServices requisitanteServices) {
        this.requisitanteServices = requisitanteServices;
    }

    
    @GetMapping("/{idRequisitante}")
    public ResponseEntity<Requisitante> buscarRequisitantesPorId(@PathVariable Long idRequisitante) {
        Requisitante requisitante = this.requisitanteServices.buscarRequisitantePorId(idRequisitante);
        return ResponseEntity.ok().body(requisitante);
    }

    @GetMapping("/")
    public ResponseEntity<List<RequisitanteDto>> buscarRequisitantes(@RequestParam Boolean status) {
        List<RequisitanteDto> requisitantes = this.requisitanteServices.buscarTodosRequisitantes(status);
        return ResponseEntity.ok().body(requisitantes);
    }

    @PostMapping("/")
    public ResponseEntity<RequisitanteDto> criarRequisitante(@Valid @RequestBody Requisitante requisitante){
        RequisitanteDto requisitanteCriado = this.requisitanteServices.cadastrarRequisitante(requisitante);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idRequisitante}").buildAndExpand(requisitante.getReqId()).toUri();
        return ResponseEntity.created(uri).body(requisitanteCriado);
    }

    @PutMapping("/{idRequisitante}")
    public ResponseEntity<RequisitanteDto> atualizarRequisitante(@Valid @RequestBody Requisitante requisitante, @PathVariable Long idRequisitante){
        requisitante.setReqId(idRequisitante);
        return ResponseEntity.ok(this.requisitanteServices.atualizarRequisitante(requisitante));
    }

    @PutMapping("/inativar/{idRequisitante}")
    public ResponseEntity<Void> inativarRequisitante(@Valid @PathVariable Long idRequisitante){
        this.requisitanteServices.alterarStatusAtivoRequisitante(idRequisitante);
        return ResponseEntity.noContent().build();
    }
    
}

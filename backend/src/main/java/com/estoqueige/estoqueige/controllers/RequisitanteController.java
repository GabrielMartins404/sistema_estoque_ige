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

import com.estoqueige.estoqueige.models.requisitante.Requisitante;
import com.estoqueige.estoqueige.models.requisitante.RequestRequisitanteDTO;
import com.estoqueige.estoqueige.models.requisitante.ResponseRequisitanteDTO;
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
    public ResponseEntity<ResponseRequisitanteDTO> buscarRequisitantesPorId(@PathVariable Long idRequisitante) {
        Requisitante requisitante = this.requisitanteServices.buscarRequisitantePorId(idRequisitante);
        return ResponseEntity.ok().body(ResponseRequisitanteDTO.fromEntity(requisitante));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseRequisitanteDTO>> buscarRequisitantes(@RequestParam Boolean status) {
        List<ResponseRequisitanteDTO> requisitantes = this.requisitanteServices.buscarTodosRequisitantes(status);
        return ResponseEntity.ok().body(requisitantes);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseRequisitanteDTO> criarRequisitante(@Valid @RequestBody RequestRequisitanteDTO requisitanteDTO){
        ResponseRequisitanteDTO requisitanteCriado = this.requisitanteServices.cadastrarRequisitante(requisitanteDTO);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idRequisitante}").buildAndExpand(requisitanteCriado.reqId()).toUri();
        return ResponseEntity.created(uri).body(requisitanteCriado);
    }

    @PutMapping("/{idRequisitante}")
    public ResponseEntity<ResponseRequisitanteDTO> atualizarRequisitante(@Valid @RequestBody RequestRequisitanteDTO requisitanteDTO, @PathVariable Long idRequisitante){
        return ResponseEntity.ok(this.requisitanteServices.atualizarRequisitante(idRequisitante, requisitanteDTO));
    }

    @PutMapping("/inativar/{idRequisitante}")
    public ResponseEntity<ResponseRequisitanteDTO> inativarRequisitante(@Valid @PathVariable Long idRequisitante){
        ResponseRequisitanteDTO requisitante = this.requisitanteServices.alterarStatusAtivoRequisitante(idRequisitante);
        return ResponseEntity.ok().body(requisitante);
    }
    
}

package com.estoqueige.estoqueige.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.estoqueige.estoqueige.models.faculdade.Faculdade;
import com.estoqueige.estoqueige.models.faculdade.RequestFaculdadeDTO;
import com.estoqueige.estoqueige.models.faculdade.ResponseFaculdadeDTO;
import com.estoqueige.estoqueige.services.FaculdadeServices;

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
@RequestMapping("/faculdade")
public class FaculdadeController {
    private final FaculdadeServices faculdadeServices;

    public FaculdadeController(FaculdadeServices faculdadeServices) {
        this.faculdadeServices = faculdadeServices;
    }

    @GetMapping("/{idFaculdade}")
    public ResponseEntity<ResponseFaculdadeDTO> buscarFaculdadesPorId(@PathVariable Long idFaculdade) {
        Faculdade faculdade = this.faculdadeServices.buscarFaculdadePorId(idFaculdade);
        return ResponseEntity.ok().body(ResponseFaculdadeDTO.fromEntity(faculdade));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseFaculdadeDTO>> buscarFaculdades(@RequestParam Boolean status) {
        List<ResponseFaculdadeDTO> faculdades = this.faculdadeServices.buscarTodasFaculdades(status);
        return ResponseEntity.ok().body(faculdades);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseFaculdadeDTO> criarFaculdade(@Valid @RequestBody RequestFaculdadeDTO faculdade){
        ResponseFaculdadeDTO faculdadeCriada = this.faculdadeServices.cadastrarFaculdade(faculdade);
        return ResponseEntity.ok().body(faculdadeCriada);
    }

    @PutMapping("/{idFaculdade}")
    public ResponseEntity<ResponseFaculdadeDTO> atualizarFaculdade(@Valid @RequestBody RequestFaculdadeDTO faculdade, @PathVariable Long idFaculdade){
        return ResponseEntity.ok(this.faculdadeServices.atualizarFaculdade(idFaculdade,faculdade));
    }

    @PutMapping("/inativar/{idFaculdade}")
    public ResponseEntity<Void> inativarFaculdade(@Valid @PathVariable Long idFaculdade){
        this.faculdadeServices.alterarStatusAtivoFaculdade(idFaculdade);
        return ResponseEntity.noContent().build();
    }
    
}

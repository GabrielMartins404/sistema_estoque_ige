package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.Faculdade;
import com.estoqueige.estoqueige.repositories.FaculdadeRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
public class FaculdadeServices {
    private final FaculdadeRepository faculdadeRepository;
    
    public FaculdadeServices(FaculdadeRepository faculdadeRepository) {
        this.faculdadeRepository = faculdadeRepository;
    }

    /* Métodos dos services */

    public Faculdade buscarFaculdadePorId(Long id){
        Optional<Faculdade> faculdade = this.faculdadeRepository.findById(id);
        return faculdade.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar faculdade por ID: "+ id));
    }

    public List<Faculdade> buscarTodasFaculdades(Boolean status){
        List<Faculdade> faculdades = this.faculdadeRepository.buscarFaculdades(status);
        return faculdades;
    }

    @Transactional
    public Faculdade cadastrarFaculdade(Faculdade faculdade){
        faculdade.setFacId(null);
        return this.faculdadeRepository.save(faculdade);
    }

    @Transactional
    public Faculdade atualizarFaculdade(Faculdade faculdade){
        Faculdade newFaculdade = this.buscarFaculdadePorId(faculdade.getFacId());

        BeanUtils.copyProperties(faculdade, newFaculdade, "facId");
        this.faculdadeRepository.save(newFaculdade);
        return newFaculdade;
    }

     public Faculdade alterarStatusAtivoFaculdade(Long id) {
        Faculdade faculdade = this.buscarFaculdadePorId(id);

        faculdade.setIsAtivo(!faculdade.getIsAtivo());
        return this.atualizarFaculdade(faculdade);
    }
}

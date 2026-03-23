package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.faculdade.Faculdade;
import com.estoqueige.estoqueige.models.faculdade.RequestFaculdadeDTO;
import com.estoqueige.estoqueige.models.faculdade.ResponseFaculdadeDTO;
import com.estoqueige.estoqueige.repositories.FaculdadeRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
@RequiredArgsConstructor
public class FaculdadeServices {
    private final FaculdadeRepository faculdadeRepository;

    /* Métodos dos services */

    public Faculdade buscarFaculdadePorId(Long id){
        Optional<Faculdade> faculdade = this.faculdadeRepository.findById(id);
        return faculdade.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar faculdade por ID: "+ id));
    }

    public List<ResponseFaculdadeDTO> buscarTodasFaculdades(Boolean status){
        List<Faculdade> faculdades = this.faculdadeRepository.buscarFaculdades(status);
        return ResponseFaculdadeDTO.fromEntityList(faculdades);
    }

    @Transactional
    public ResponseFaculdadeDTO cadastrarFaculdade(RequestFaculdadeDTO dto){
        Faculdade faculdade = new Faculdade(
            null,
            dto.facNome(),
            dto.facSigla(),
            true,
            null
        );
        return ResponseFaculdadeDTO.fromEntity(this.faculdadeRepository.save(faculdade));
    }

    @Transactional
    public ResponseFaculdadeDTO atualizarFaculdade(Long id, RequestFaculdadeDTO dto){
        Faculdade newFaculdade = this.buscarFaculdadePorId(id);
        newFaculdade.setFacNome(dto.facNome());
        newFaculdade.setFacSigla(dto.facSigla());
        return ResponseFaculdadeDTO.fromEntity(this.faculdadeRepository.save(newFaculdade));
    }

     public ResponseFaculdadeDTO alterarStatusAtivoFaculdade(Long id) {
        Faculdade faculdade = this.buscarFaculdadePorId(id);
        faculdade.setIsAtivo(!faculdade.getIsAtivo());
        return ResponseFaculdadeDTO.fromEntity(this.faculdadeRepository.save(faculdade));
    }
}

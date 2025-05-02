package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.dto.RequisitanteDto;
import com.estoqueige.estoqueige.models.Faculdade;
import com.estoqueige.estoqueige.models.Requisitante;
import com.estoqueige.estoqueige.repositories.RequisitanteRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequisitanteServices {
    private final RequisitanteRepository requisitanteRepository;
    private final FaculdadeServices faculdadeServices;
    public RequisitanteServices(RequisitanteRepository requisitanteRepository, FaculdadeServices faculdadeServices) {
        this.requisitanteRepository = requisitanteRepository;
        this.faculdadeServices = faculdadeServices;
    }

    /* Método services */
    public RequisitanteDto gerarRequisitanteDto(Requisitante requisitante){

        RequisitanteDto requisitanteDto = new RequisitanteDto();

        requisitanteDto.setReqNome(requisitante.getReqNome());
        requisitanteDto.setIsAtivo(requisitante.getIsAtivo());
        requisitanteDto.setReqId(requisitante.getReqId());

        if(requisitante.getFacRequisitante() != null){
            Faculdade faculdade = this.faculdadeServices.buscarFaculdadePorId(requisitante.getFacRequisitante().getFacId());
            requisitanteDto.setReqFacNome(faculdade.getFacNome());
            requisitanteDto.setReqFacSigla(faculdade.getFacSigla());
            requisitanteDto.setReqFaqId(requisitante.getFacRequisitante().getFacId());;
        }

        // if(requisitante.getFacRequisitante() != null){
        //     requisitanteDto.setReqFacNome(requisitante.getFacRequisitante().getFacNome());
            
        //     requisitanteDto.setReqFaqId(requisitante.getFacRequisitante().getFacId());;
            
        // }
        return requisitanteDto;
    }

    public Requisitante buscarRequisitantePorId(Long id) {
        Optional<Requisitante> requisitante = this.requisitanteRepository.findById(id);
        return requisitante.orElseThrow(() -> new ErroAoBuscarObjetos("Não foi possivel encontrar o requisitante com id: "+id));
    }

    public List<RequisitanteDto> buscarTodosRequisitantes(Boolean status) {
        List<Requisitante> requisitantes = this.requisitanteRepository.buscarRequisitantes(status);
        
        List<RequisitanteDto> requisitanteDtos = new ArrayList<>();
        for (Requisitante requisitante : requisitantes) {
            requisitanteDtos.add(gerarRequisitanteDto(requisitante));
        }
        return requisitanteDtos;
    }

    @Transactional
    public RequisitanteDto cadastrarRequisitante(Requisitante requisitante) {
        requisitante.setReqId(null);
        if (requisitante.getFacRequisitante() == null || requisitante.getFacRequisitante().getFacId() == null || requisitante.getFacRequisitante().getFacId() == 0) {
            requisitante.setFacRequisitante(null);
        }
        return gerarRequisitanteDto(this.requisitanteRepository.save(requisitante));
    }

    @Transactional
    public RequisitanteDto atualizarRequisitante(Requisitante requisitante) {
        Requisitante newRequisitante = this.buscarRequisitantePorId(requisitante.getReqId());
        //Nem sempre, quando inserir o requisitante haverá os dados 
        
        if (requisitante.getFacRequisitante() == null || requisitante.getFacRequisitante().getFacId() == null || requisitante.getFacRequisitante().getFacId() == 0) {
            requisitante.setFacRequisitante(null);
        }
        //Copio todas as propriedades do requisitante para newRequisitante, menos o ID
        BeanUtils.copyProperties(requisitante, newRequisitante, "reqId");
        
        this.requisitanteRepository.save(newRequisitante);

        return gerarRequisitanteDto(newRequisitante);
    }

    @Transactional
    public Requisitante alterarStatusAtivoRequisitante(Long id) {
        Requisitante requisitante = buscarRequisitantePorId(id);

        requisitante.setIsAtivo(!requisitante.getIsAtivo());
        return this.requisitanteRepository.save(requisitante);
    }
}

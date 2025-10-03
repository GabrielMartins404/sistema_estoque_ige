package com.estoqueige.estoqueige.services;
import com.estoqueige.estoqueige.models.faculdade.Faculdade;
import com.estoqueige.estoqueige.models.requisitante.RequestRequisitanteDTO;
import com.estoqueige.estoqueige.models.requisitante.Requisitante;
import com.estoqueige.estoqueige.models.requisitante.ResponseRequisitanteDTO;
import com.estoqueige.estoqueige.repositories.RequisitanteRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequisitanteServices {
    private final RequisitanteRepository requisitanteRepository;
    private final FaculdadeServices faculdadeServices;

    /* Método services */

    public Requisitante buscarRequisitantePorId(Long id) {
        Optional<Requisitante> requisitante = this.requisitanteRepository.findById(id);
        return requisitante
                .orElseThrow(() -> new ErroAoBuscarObjetos("Não foi possivel encontrar o requisitante com id: " + id));
    }

    public List<ResponseRequisitanteDTO> buscarTodosRequisitantes(Boolean status) {
        List<Requisitante> requisitantes = this.requisitanteRepository.buscarRequisitantes(status);
        return ResponseRequisitanteDTO.fromEntityList(requisitantes);
    }

    @Transactional
    public ResponseRequisitanteDTO cadastrarRequisitante(RequestRequisitanteDTO dto) {
        Faculdade faculdade = null;
        if (dto.facRequisitanteId() != null) {
            faculdade = this.faculdadeServices.buscarFaculdadePorId(dto.facRequisitanteId());
        }
        Requisitante requisitante = new Requisitante(
                null,
                dto.reqNome(),
                true,
                faculdade);
        return ResponseRequisitanteDTO.fromEntity(this.requisitanteRepository.save(requisitante));
    }

    @Transactional
    public ResponseRequisitanteDTO atualizarRequisitante(Long id, RequestRequisitanteDTO dto) {
        Requisitante newRequisitante = this.buscarRequisitantePorId(id);
        Faculdade faculdade = null;
        if (dto.facRequisitanteId() != null) {
            faculdade = this.faculdadeServices.buscarFaculdadePorId(dto.facRequisitanteId());
        }
        newRequisitante.setReqNome(dto.reqNome());
        newRequisitante.setFacRequisitante(faculdade);
        return ResponseRequisitanteDTO.fromEntity(newRequisitante);
    }

    @Transactional
    public ResponseRequisitanteDTO alterarStatusAtivoRequisitante(Long id) {
        Requisitante requisitante = buscarRequisitantePorId(id);

        requisitante.setIsAtivo(!requisitante.getIsAtivo());
        return ResponseRequisitanteDTO.fromEntity(this.requisitanteRepository.save(requisitante));
    }
}

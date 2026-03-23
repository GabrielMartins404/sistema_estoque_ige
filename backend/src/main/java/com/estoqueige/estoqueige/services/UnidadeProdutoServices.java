package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.unidadeProduto.RequestUnidadeProdutoDTO;
import com.estoqueige.estoqueige.models.unidadeProduto.ResponseUnidadeProdutoDTO;
import com.estoqueige.estoqueige.models.unidadeProduto.UnidadeProduto;
import com.estoqueige.estoqueige.repositories.UnidadeProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
@RequiredArgsConstructor
public class UnidadeProdutoServices {
    private final UnidadeProdutoRepository unidadeProdutoRepository;

    /* Métodos dos services */

    public UnidadeProduto buscarUnidadeProdutoPorId(Long id){
        Optional<UnidadeProduto> unidadeProduto = this.unidadeProdutoRepository.findById(id);
        return unidadeProduto.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar Unidade do Produto por ID: "+ id));
    }

    public List<ResponseUnidadeProdutoDTO> buscarTodasUnidadeProdutos(Boolean status){
        List<UnidadeProduto> unidadeProdutos = this.unidadeProdutoRepository.buscarUnidades(status);
        return ResponseUnidadeProdutoDTO.fromEntityList(unidadeProdutos);
    }

    @Transactional
    public ResponseUnidadeProdutoDTO cadastrarUnidadeProduto(RequestUnidadeProdutoDTO dto){
        UnidadeProduto unidadeProduto = new UnidadeProduto(
            null,
            dto.unNome(),
            dto.unSigla(),
            true,
            null
        );
        return ResponseUnidadeProdutoDTO.fromEntity(this.unidadeProdutoRepository.save(unidadeProduto));
    }

    @Transactional
    public ResponseUnidadeProdutoDTO atualizarUnidadeProduto(Long id, RequestUnidadeProdutoDTO dto){
        UnidadeProduto newUnidadeProduto = this.buscarUnidadeProdutoPorId(id);
        newUnidadeProduto.setUnNome(dto.unNome());
        newUnidadeProduto.setUnSigla(dto.unSigla());
        return ResponseUnidadeProdutoDTO.fromEntity(this.unidadeProdutoRepository.save(newUnidadeProduto));
    }

     public ResponseUnidadeProdutoDTO alterarStatusAtivoUnidadeProduto(Long id) {
        UnidadeProduto unidadeProduto = this.buscarUnidadeProdutoPorId(id);

        unidadeProduto.setIsAtivo(!unidadeProduto.getIsAtivo());
        return ResponseUnidadeProdutoDTO.fromEntity(this.unidadeProdutoRepository.save(unidadeProduto));
    }
}

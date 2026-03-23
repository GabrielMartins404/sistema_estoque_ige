package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.categoriaProduto.CategoriaProduto;
import com.estoqueige.estoqueige.models.categoriaProduto.RequestCategoriaProdutoDTO;
import com.estoqueige.estoqueige.models.categoriaProduto.ResponseCategoriaDTO;
import com.estoqueige.estoqueige.repositories.CategoriaProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
@RequiredArgsConstructor
public class CategoriaProdutoServices {
    private final CategoriaProdutoRepository categoriaProdutoRepository;
    /* Métodos dos services */

    public CategoriaProduto buscarCategoriaProdutoPorId(Long id){
        Optional<CategoriaProduto> categoriaProduto = this.categoriaProdutoRepository.findById(id);
        return categoriaProduto.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar cagoria por ID: "+ id));
    }

    public List<ResponseCategoriaDTO> buscarTodasCategoriaProdutos(Boolean status){
        List<CategoriaProduto> categoriaProduto = this.categoriaProdutoRepository.buscarCategorias(status);
        return ResponseCategoriaDTO.fromEntityList(categoriaProduto);
    }

    @Transactional
    public ResponseCategoriaDTO cadastrarCategoriaProduto(RequestCategoriaProdutoDTO dto){
        CategoriaProduto categoriaProduto = new CategoriaProduto(
            null,
            dto.catProNome(),
            true,
            null

        );
        return ResponseCategoriaDTO.fromEntity(this.categoriaProdutoRepository.save(categoriaProduto));
    }

    @Transactional
    public ResponseCategoriaDTO atualizarCategoriaProduto(Long id, RequestCategoriaProdutoDTO dto){
        CategoriaProduto newCategoriaProduto = this.buscarCategoriaProdutoPorId(id);
        newCategoriaProduto.setCatProNome(dto.catProNome());
        return ResponseCategoriaDTO.fromEntity(this.categoriaProdutoRepository.save(newCategoriaProduto));
    }

    @Transactional
    public ResponseCategoriaDTO alterarStatusAtivoCategoriaProduto(Long id) {
        CategoriaProduto categoriaProduto = this.buscarCategoriaProdutoPorId(id);
        categoriaProduto.setIsAtivo(!categoriaProduto.getIsAtivo());
        return ResponseCategoriaDTO.fromEntity(this.categoriaProdutoRepository.save(categoriaProduto));
    }
}

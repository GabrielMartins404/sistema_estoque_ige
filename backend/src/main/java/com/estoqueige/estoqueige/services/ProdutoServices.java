package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.models.categoriaProduto.CategoriaProduto;
import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.produto.RequestProdutoDTO;
import com.estoqueige.estoqueige.models.produto.ResponseProdutoDTO;
import com.estoqueige.estoqueige.models.unidadeProduto.UnidadeProduto;
import com.estoqueige.estoqueige.repositories.ProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServices {

    private final CategoriaProdutoServices categoriaProdutoServices;
    private final ProdutoRepository produtoRepository;
    private final UnidadeProdutoServices unidadeProdutoServices;

    /* Método dos services */
    public Produto buscarProdutoPorId(Long id) {
        Produto produto = this.produtoRepository.findById(id).
                orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar produto com id: "+id));
        return produto;
    }

    public List<Produto> buscarProdutosPorIds(List<Long> ids) {
        return this.produtoRepository.findAllById(ids);
    }

    public List<ResponseProdutoDTO> buscarTodosProdutos(Boolean status) {
        List<Produto> produtos = this.produtoRepository.buscarProdutos(status);
        return ResponseProdutoDTO.fromEntityList(produtos);
    }

    public ResponseProdutoDTO cadastrarProduto(RequestProdutoDTO dto) {
        CategoriaProduto categoria = this.categoriaProdutoServices.buscarCategoriaProdutoPorId(dto.proCategoriaId());
        UnidadeProduto unidade = this.unidadeProdutoServices.buscarUnidadeProdutoPorId(dto.proUnId());
        //Por regra de negócio, é preciso bloquear o cadastro de um produto com descrição repetida
        if(this.produtoRepository.existsByProDescricaoAndIsAtivoTrue(dto.proDescricao())){
            throw new ErroValidacaoLogica("Não é possivel atualizar produto pois já existe outro com a mesma descrição");
        }
        Produto produto = new Produto(
            null,
            dto.proNome(),
            dto.proSipac(),
            dto.proDescricao(),
            dto.proCusto() == null ? 0F : dto.proCusto(),
            0F,
            dto.proEstoqueMin() == null ? 0F : dto.proEstoqueMin(),
            false,
            true,
            unidade,
            categoria,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        //Verificar sempre que for atualizar, se o estoque ficou abaixo do minimo

        produto.verificarStatusEstoqueMinimo(produto.getProQtd());
        return ResponseProdutoDTO.fromEntity(this.produtoRepository.save(produto));
    }

    public ResponseProdutoDTO atualizarProduto(Long id, RequestProdutoDTO produto) {
        Produto newProduto = this.buscarProdutoPorId(id);
        UnidadeProduto unidade = this.unidadeProdutoServices.buscarUnidadeProdutoPorId(produto.proUnId());
        CategoriaProduto categoria = this.categoriaProdutoServices.buscarCategoriaProdutoPorId(produto.proCategoriaId());

        newProduto.setProCategoria(categoria);
        newProduto.setProUn(unidade);
        newProduto.setProCusto(produto.proCusto());
        newProduto.setProDescricao(produto.proDescricao());
        newProduto.setProNome(produto.proNome());
        newProduto.setProSipac(produto.proSipac());
        newProduto.setProEstoqueMin(produto.proEstoqueMin());

        newProduto.verificarStatusEstoqueMinimo(newProduto.getProQtd());
        return ResponseProdutoDTO.fromEntity(this.produtoRepository.save(newProduto));
    }

    public ResponseProdutoDTO alterarStatusAtivoProduto(Long id) {
        Produto produto = this.buscarProdutoPorId(id);

        produto.setIsAtivo(!produto.getIsAtivo());
        return(ResponseProdutoDTO.fromEntity(this.produtoRepository.save(produto)));
    }
}

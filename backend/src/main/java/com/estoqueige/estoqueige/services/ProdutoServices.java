package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.dto.MovEstoqueDto;
import com.estoqueige.estoqueige.dto.ProdutoDto;
import com.estoqueige.estoqueige.models.CategoriaProduto;
import com.estoqueige.estoqueige.models.MovimentacaoEstoque;
import com.estoqueige.estoqueige.models.Produto;
import com.estoqueige.estoqueige.models.UnidadeProduto;
import com.estoqueige.estoqueige.repositories.CategoriaProdutoRepository;
import com.estoqueige.estoqueige.repositories.ProdutoRepository;
import com.estoqueige.estoqueige.repositories.UnidadeProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServices {

    private final CategoriaProdutoServices categoriaProdutoServices;
    private final ProdutoRepository produtoRepository;
    private final UnidadeProdutoServices unidadeProdutoServices;
    
    public ProdutoServices(ProdutoRepository produtoRepository, UnidadeProdutoServices unidadeProdutoServices, CategoriaProdutoServices categoriaProdutoServices) {
        this.produtoRepository = produtoRepository;
        this.unidadeProdutoServices = unidadeProdutoServices;
        this.categoriaProdutoServices = categoriaProdutoServices;
    }


    /* Método dos services */

    public MovEstoqueDto gerarMovEstoqueDto(MovimentacaoEstoque movimentacaoEstoque){
        MovEstoqueDto movEstoqueDto = new MovEstoqueDto();
        
        movEstoqueDto.setMovEstId(movimentacaoEstoque.getMovEstId());
        movEstoqueDto.setMovEstData(movimentacaoEstoque.getMovEstData());
        movEstoqueDto.setMovEstQtd(movimentacaoEstoque.getMovEstQtd());
        movEstoqueDto.setMovEstQtdAnterior(movimentacaoEstoque.getMovEstQtdAnterior());
        movEstoqueDto.setMovEstQtdPosterior(movimentacaoEstoque.getMovEstQtdPosterior());
        movEstoqueDto.setMovEstTipo(movimentacaoEstoque.getMovEstTipo());
        movEstoqueDto.setMovEstMovimentacao(movimentacaoEstoque.getMovEstMovimentacao());

        return movEstoqueDto;
    }

    public ProdutoDto gerarProdutoDto(Produto produto){
        //Preciso criar a movimentacaoSemProdutoDto e inserir em EstoqueMovimentacao, e após isso, inserir no Produto
        
        
        // List<MovEstoqueDto> movEstoqueDtos = new ArrayList<>();
        // for (MovimentacaoEstoque movimentacaoEstoque : produto.getMovimentacaoEstoque()) {
            
        //     movEstoqueDtos.add(this.gerarMovEstoqueDto(movimentacaoEstoque));
        // }

        //Há vezes em que a categoria e unidade não vem no produto. Desse modo, é preciso busca-las
        // Busca a categoria no banco se existir ID
        String catProNome = null;
        Long catProId = null;
        if (produto.getProCategoria() != null && produto.getProCategoria().getCatProId() != null) {
            CategoriaProduto categoria = categoriaProdutoServices.buscarCategoriaProdutoPorId(produto.getProCategoria().getCatProId());
                catProNome = categoria.getCatProNome();
                catProId = categoria.getCatProId();
        }

        // Busca a unidade no banco se existir ID
        String unSigla = null;
        Long unId = null;
        if (produto.getProUn() != null && produto.getProUn().getUnId() != null) {
            UnidadeProduto unidadeOpt = unidadeProdutoServices.buscarUnidadeProdutoPorId(produto.getProUn().getUnId());
            unSigla = unidadeOpt.getUnSigla();
            unId = unidadeOpt.getUnId();
        }

        // Constrói o DTO
        return new ProdutoDto(
            produto.getProId(),
            produto.getProNome(),
            produto.getProSipac(),
            produto.getProDescricao(),
            unSigla,
            unId,
            catProNome,
            catProId,
            produto.getProQtd(),
            produto.getProEstoqueMin(),
            produto.getProCusto(),
            produto.getIsAbaixoMin(),
            produto.getIsAtivo()
        );
    }

    public Produto buscarProdutoPorId(Long id) {
        Produto produto = this.produtoRepository.findById(id).
                orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar produto com id: "+id));
        return produto;
    }

    public List<ProdutoDto> buscarTodosProdutos(Boolean status) {
        List<Produto> produtos = this.produtoRepository.buscarProdutos(status);
        
        List<ProdutoDto> produtoDtos = new ArrayList<>();
        for (Produto produto : produtos) {
            produtoDtos.add(gerarProdutoDto(produto));
        }   
        return produtoDtos;
    }

    public ProdutoDto cadastrarProduto(Produto produto) {
        produto.setProId(null);
        
        //Por regra de negócio, é preciso bloquear o cadastro de um produto com descrição repetida
        if(this.produtoRepository.existsByProDescricaoAndIsAtivoTrue(produto.getProDescricao())){
            throw new ErroValidacaoLogica("Não é possivel atualizar produto pois já existe outro com a mesma descrição");
        }

        //Verifico se o produto tem categoria inserido
        if (produto.getProCategoria() == null 
            || produto.getProCategoria().getCatProId() == null 
            || produto.getProCategoria().getCatProId() <= 0) {
            produto.setProCategoria(null);
        }
        //Verificar sempre que for atualizar, se o estoque ficou abaixo do minimo
        if(produto.getProQtd() < produto.getProEstoqueMin()){
            produto.setIsAbaixoMin(true);
        }
        Produto ProdutoCadastrado = this.produtoRepository.save(produto);
        return gerarProdutoDto(ProdutoCadastrado);
    }

    public ProdutoDto atualizarProduto(Produto produto) {
        Produto newProduto = this.buscarProdutoPorId(produto.getProId());

        BeanUtils.copyProperties(produto, newProduto, "proId");
        if (produto.getProCategoria() == null 
            || produto.getProCategoria().getCatProId() == null 
            || produto.getProCategoria().getCatProId() <= 0) {
            produto.setProCategoria(null);
        }

        if(produto.getProEstoqueMin() < 0){
            produto.setProEstoqueMin(0f);
        }

        if(produto.getProQtd() < produto.getProEstoqueMin()){
            newProduto.setIsAbaixoMin(true);
        }
        newProduto.setProUn(produto.getProUn());
        newProduto.setProCategoria(produto.getProCategoria());
        this.produtoRepository.save(newProduto);
        return gerarProdutoDto(newProduto);
    }

    public ProdutoDto alterarStatusAtivoProduto(Long id) {
        Produto produto = this.buscarProdutoPorId(id);

        produto.setIsAtivo(!produto.getIsAtivo());
        this.atualizarProduto(produto);
        return(gerarProdutoDto(produto));
    }
}

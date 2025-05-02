package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.dto.MovimentacaoDto;
import com.estoqueige.estoqueige.dto.ProdutoMovimentacaoDto;
import com.estoqueige.estoqueige.models.Movimentacao;
import com.estoqueige.estoqueige.models.Produto;
import com.estoqueige.estoqueige.models.ProdutoMovimentacao;
import com.estoqueige.estoqueige.models.Requisitante;
import com.estoqueige.estoqueige.models.Usuario;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.repositories.MovimentacaoRepository;
import com.estoqueige.estoqueige.security.UserSpringSecurity;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroAutorizacao;
import com.estoqueige.estoqueige.services.exceptions.ErroMovimentacaoCancelada;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MovimentacaoServices {
    private final MovimentacaoRepository movimentacaoRepository;

    private final MovimentacaoEstoqueServices movimentacaoEstoqueServices;

    private final ProdutoServices produtoServices;

    private final UsuarioServices usuarioServices;

    private final RequisitanteServices requisitanteServices;


    public MovimentacaoServices(MovimentacaoRepository movimentacaoRepository, MovimentacaoEstoqueServices movimentacaoEstoqueServices, ProdutoServices produtoServices, UsuarioServices usuarioServices, RequisitanteServices requisitanteServices) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.movimentacaoEstoqueServices = movimentacaoEstoqueServices;
        this.produtoServices = produtoServices;
        this.usuarioServices = usuarioServices;
        this.requisitanteServices = requisitanteServices;
    }


    /* Método services */
    //Como ProdutoMovimentacao é uma classe que depende de Movimentacao, esse método é implementado aqui. Mas pode ser alterado mais para frente
    public ProdutoMovimentacaoDto gerarProdutoMovimentacaoDto(ProdutoMovimentacao produtoMovimentacao){
        ProdutoMovimentacaoDto produtoMovimentacaoDto = new ProdutoMovimentacaoDto();

        //Aqui converto o produtoMovimentacao em produtoDto, para simplificar a apresentação das movimentacoes
        produtoMovimentacaoDto.setProduto(
            this.produtoServices.gerarProdutoDto(produtoMovimentacao.getProMovProduto()
        ));

        produtoMovimentacaoDto.setQtdProduto(produtoMovimentacao.getProMovQtdProduto());

        return produtoMovimentacaoDto;
    }

    public MovimentacaoDto gerarMovimentacaoDto(Movimentacao movimentacao){
        MovimentacaoDto movimentacaoDto = new MovimentacaoDto();

        List<ProdutoMovimentacaoDto> produtoMovimentacaoDtos = new ArrayList<>();
        for (ProdutoMovimentacao produtoMovimentacao : movimentacao.getProdutosMov()) {
            //Aqui, chamo a função acima que insere um array de ProdutoMovimentacao com ProdutoDto
            produtoMovimentacaoDtos.add(gerarProdutoMovimentacaoDto(produtoMovimentacao));
        }
        Requisitante requisitante = this.requisitanteServices.buscarRequisitantePorId(movimentacao.getMovRequisitante().getReqId());

        movimentacaoDto.setMovId(movimentacao.getMovId());
        movimentacaoDto.setMovData(movimentacao.getMovData());
        movimentacaoDto.setMovHorario(movimentacao.getMovHorario());
        movimentacaoDto.setMovDataCancelamento(movimentacao.getMovDataCancelamento());
        movimentacaoDto.setMovHorarioCancelamento(movimentacao.getMovHorarioCancelamento());
        movimentacaoDto.setMovNf(movimentacao.getMovNf());
        movimentacaoDto.setMovObservacao(movimentacao.getMovObservacao());
        movimentacaoDto.setMovNumRequisicao(movimentacao.getMovNumRequisicao());
        movimentacaoDto.setMovOrigem(movimentacao.getMovOrigem());
        movimentacaoDto.setMovTipo(movimentacao.getMovTipo());
        movimentacaoDto.setMovStatus(movimentacao.getMovStatus());
        movimentacaoDto.setMovRequisitante(requisitante.getReqNome());
        movimentacaoDto.setMovUsuario(movimentacao.getMovUsuario().getUsuNome());
        movimentacaoDto.setProMovProduto(produtoMovimentacaoDtos);
        return movimentacaoDto;
        
    }

    //Esse método irá ser usado no frontEnd onde preciso que retorne somente uma movimentacao DTO
    //Verifica se vai ser realmente necessário?
    public MovimentacaoDto retornarMovimentacaoDto(Long id){
        Movimentacao movimentacao = this.buscarMovimentacaoPorId(id);
        MovimentacaoDto movimentacaoDto = gerarMovimentacaoDto(movimentacao);

        return movimentacaoDto;
    }

    public Movimentacao buscarMovimentacaoPorId(Long id){
        Movimentacao movimentacao = this.movimentacaoRepository.findById(id)
            .orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar Movimentacao de código: "+ id));

        return movimentacao;
    }

    public List<MovimentacaoDto> buscarTodasMovimentacoes(String tipo, String status){
        MovTipo movTipo = MovTipo.movTipo(tipo);
        MovStatus movStatus = MovStatus.movStatus(status);
        List<Movimentacao> movimentacoes = this.movimentacaoRepository.buscarMovimentacaosPorTipo(movTipo.name(), movStatus.name());
        List<MovimentacaoDto> movimentacaoDtos = new ArrayList<>();
        
        for (Movimentacao movimentacao : movimentacoes) {
            movimentacaoDtos.add(gerarMovimentacaoDto(movimentacao));
        }

        return movimentacaoDtos;
    }

    //Método para salvar a movimentação no Banco de Dados
    @Transactional
    public MovimentacaoDto salvarMovimentacao(Movimentacao movimentacao) {

        //Puxar Usuario do Context
        UserSpringSecurity userSpringSecurity = UsuarioServices.autenticado();
        if(Objects.isNull(userSpringSecurity)){
            throw new ErroAutorizacao("Acesso negado!");
        }
        Usuario usuario = this.usuarioServices.buscarUsuarioPorId(userSpringSecurity.getId());

        //Salvar a movimentação no banco de dados
        movimentacao.setMovId(null);
        movimentacao.setMovStatus(MovStatus.FINALIZADO);
        movimentacao.setMovData(LocalDate.now());
        movimentacao.setMovHorario(LocalTime.now());
        movimentacao.setMovUsuario(usuario);

        //É preciso fazer o vinculo da movimentação para os ProdutosMovimentações. Desse modo, é preciso fazer o loop abaixo
        for (ProdutoMovimentacao produtoMovimentacao : movimentacao.getProdutosMov()) {
            //Valido se a quantidade inserida é maior que zero
            if(produtoMovimentacao.getProMovQtdProduto() < 0){
                throw new ErroValidacaoLogica("Não é possível realizar movimentação com quantidade negativa.");
            }else{    
                
                //O loop não incrementa diretamente os produtos e sim a clase ProdutoMovimentacao, que além do produto, possui a informação da quantidade
                Produto produto = this.produtoServices.buscarProdutoPorId(produtoMovimentacao.getProMovProduto().getProId()); 
                //Verifico se o produto está ativo
                if(produto.getIsAtivo()){
                    //Verifico aqui se o produto tem estoque disponível para dar saída 
                    if((produtoMovimentacao.getProMovQtdProduto() > produto.getProQtd()) && (movimentacao.getMovTipo() == MovTipo.SAIDA)){
                        throw new ErroValidacaoLogica("O produto '" +produto.getProNome()+ "' não possui estoque suficiente para concluir a saida");
                    }else if(!produtoMovimentacao.getProMovProduto().getIsAtivo()){ //Verifico se o produto está ativo
                        throw new ErroValidacaoLogica("O produto '" +produto.getProNome()+ "' está inativo e não pode ser movimentado");
                    }else{
                        produtoMovimentacao.setProMovProduto(produto); //Seto as informações do produto
                        produtoMovimentacao.setProMovMovimentacao(movimentacao); //Seto a informação da movimentacao
                    }    
                }else{
                    throw new ErroValidacaoLogica("Produto "+produto.getProNome()+" está inativo e não pode ser movimentado!");
                }
                
            }
        }

        movimentacao = movimentacaoRepository.save(movimentacao);

        for (ProdutoMovimentacao produtoMovimentacao: movimentacao.getProdutosMov()){
            movimentacaoEstoqueServices.salvarMovimentacaoEstoque(movimentacao, produtoMovimentacao);
        }

        return gerarMovimentacaoDto(movimentacao);
    }

    @Transactional
    public Movimentacao atualizarMovimentacao(Movimentacao movimentacao){
        Movimentacao newMovimentacao = this.buscarMovimentacaoPorId(movimentacao.getMovId());
        BeanUtils.copyProperties(movimentacao, newMovimentacao, "getMovId");

        return this.movimentacaoRepository.save(newMovimentacao);
    }

    @Transactional
    public void cancelarMovimentacao(Long idMovimentacao){
        if(!usuarioServices.validarUsuario("Usuário não tem permissão para cancelar movimentacao.")){
            return;
        }

        Movimentacao movimentacao = this.buscarMovimentacaoPorId(idMovimentacao);
        if(movimentacao.getMovStatus() == MovStatus.CANCELADO){
            throw new ErroMovimentacaoCancelada("Não é possível cancelar a movimentação pois a mesma já está cancelada.");
        }
        movimentacao.setMovDataCancelamento(LocalDate.now());
        movimentacao.setMovHorarioCancelamento(LocalTime.now());
        movimentacao.setMovStatus(MovStatus.CANCELADO);
        this.atualizarMovimentacao(movimentacao);
        
        for (ProdutoMovimentacao produtoMovimentacao : movimentacao.getProdutosMov()) {
            this.movimentacaoEstoqueServices.salvarMovimentacaoEstoque(movimentacao, produtoMovimentacao);
        }
    }
}
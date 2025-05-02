package com.estoqueige.estoqueige.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDto {
    private Long proId;
    private String proNome;
    private String proSipac;
    private String proDescricao;
    private String proUn;
    private Long proUnId;
    private String proCategoria;
    private Long proCategoriaId;
    private Float proQtd;
    private Float proEstoqueMin;
    private Float proCusto;
    private Boolean isAbaixoMin;
    private Boolean isAtivo;
    
}

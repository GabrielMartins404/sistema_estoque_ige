export type RequestMovimentacaoType = {
    movNf: string,
    movNumRequisicao: string,
    movObservacao: string,
    movStatus: string,
    movTipo: string,
    movOrigem: string,
    movRequisitanteId: number,
    produtosMov: RequestProdutoMovimentacaoType[]
}

export type RequestAtualizaMovimentacaoType = {
    movNf: string,
    movNumRequisicao: string,
    movObservacao: string
}

export type RequestProdutoMovimentacaoType = {
    proMovProduto?: number,
    proMovQtdProduto?: number,
    proMovCustoProduto?: number
}
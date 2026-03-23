import { ResponseProdutoType } from "@/hooks/produto/types/ResponseProduto.type"

export type ResponseMovimentacaoType = {
    movId: number,
    movNf: string,
    movNumRequisicao: string,
    movObservacao: string,
    movData: string,
    movHorario: string,
    movDataCancelamento: string,
    movHorarioCancelamento: string,
    movStatus: string,
    movTipo: string,
    movOrigem: string,
    movUsuarioId: number,
    movUsuarioNome: string,
    movRequisitanteId: number,
    movRequisitanteNome: string,
    produtos: ResponseProdutoMovimentacaoType[]
}

export type ResponseProdutoMovimentacaoType = {
    produto: ResponseProdutoType,
    proMovQtdProduto: number,
    proMovCustoProduto: number
}
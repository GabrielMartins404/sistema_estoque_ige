import apiClient from './api/apiCllient'
import type { ProdutoMaisMovimentadoType, ProdutosPorRequisitanteType } from '@/hooks/relatorios/types'

export const RelatorioServices = {
    async produtoMaisMovimentado(): Promise<ProdutoMaisMovimentadoType[]> {
        const response = await apiClient.get('/relatorios/buscarProdutosMaisMovimentados')
        console.log("Produtos Mais Movimentados: ", response.data)
        return response.data
    },

    async produtosPorRequisitante(): Promise<ProdutosPorRequisitanteType[]> {
        const response = await apiClient.get('/relatorios/buscarRequisitantesComMaisProdutos')
        console.log("Produtos por Requisitante: ", response.data)
        return response.data
    },

    async buscarQtdMov(tipo: string, status: string): Promise<number> {
        if(tipo != "E" && tipo != "S"){
            tipo = "S"
        }
        if(status != "F" && status != "C"){
            status = "F"
        }

        const response = await apiClient.get('/relatorios/buscarQtdMov', {
            params: {
                tipo: tipo,
                status: status
              }
        })
        console.log("Qtd Movimentação: ", response.data)
        return response.data
    },

    async buscarQtdProdutosAtivos(): Promise<number> {
        const response = await apiClient.get('/relatorios/buscarQtdProdutosAtivos')
        return response.data
    },

    async buscarProdutosAbaixoMin(): Promise<number> {
        const response = await apiClient.get('/relatorios/buscarProdutosAbaixoMin')
        return response.data
    }

}
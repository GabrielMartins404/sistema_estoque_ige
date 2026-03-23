import { RequisitanteType } from "@/types/requisitanteType";
import apiClient from "./api/apiCllient";
import { RequestMovimentacaoType, ResponseMovimentacaoType } from "@/hooks/movimentacao/types";

export const MovimentacaoServices = {
    async listarMovimentacoes(tipo: string, status: string): Promise<ResponseMovimentacaoType[]> {
        const response = await apiClient.get('/movimentacao/', {
            params: {
                tipo: tipo,
                status: status
              }
        })
        //console.log(response.data)
        return response.data
    },

    async criar(dados: RequestMovimentacaoType): Promise<ResponseMovimentacaoType>{
        //console.log(dados.produtosMov)
        const response = await apiClient.post('/movimentacao/', dados)
        return response.data
    },

    async cancelar(idMovimentacao: number): Promise<void>{
        const response = await apiClient.put(`/movimentacao/cancelarMovimentacao/${idMovimentacao}`)
        return response.data
    }
}
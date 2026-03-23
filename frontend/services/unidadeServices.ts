import { RequestUnidadeType, ResponseUnidadeType } from "@/hooks/unidade/types";
import apiClient from "./api/apiCllient";
import type { UnidadeType } from "@/types/unidadeType";


export const UnidadeServices = {
    async listarTodos(status: boolean): Promise<ResponseUnidadeType[]> {
        const response = await apiClient.get('/unidadeProduto/',{
            params: {
                status: status
            }
        })
        return response.data
            
    },

    async criar(dados: RequestUnidadeType): Promise<ResponseUnidadeType>{
        const response = await apiClient.post('/unidadeProduto/', dados)
        return response.data
    },

    async atualizar(id: number, dados: RequestUnidadeType): Promise<ResponseUnidadeType>{
        const response = await apiClient.put(`/unidadeProduto/${id}`, dados)
        return response.data
    },

    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/unidadeProduto/inativar/${id}`)
        return response.data
    }
}
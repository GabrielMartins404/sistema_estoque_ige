import { RequestProdutoType, ResponseProdutoType } from '@/hooks/produto/types'
import apiClient from "./api/apiCllient";

export const ProdutoServices = {
    async listarTodos(status: boolean): Promise<ResponseProdutoType[]> {
        const response = await apiClient.get('/produto/', {
            params: {
                status: status
            }
        })
        return response.data
    },

    async criar(dados: RequestProdutoType): Promise<ResponseProdutoType>{
        const response = await apiClient.post('/produto/',dados)
        return response.data
       
    },

    async atualizar(id: number, dados: RequestProdutoType): Promise<ResponseProdutoType>{
        const response = await apiClient.put(`/produto/${id}`,dados)
        return response.data
       
    },

    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/produto/inativar/${id}`)
        return response.data
       
    }
}
import { RequestCategoriaType, ResponseCategoriaType } from "@/hooks/categoria/types";
import apiClient from "./api/apiCllient";


export const CategoriaServices = {
    async listarTodas(status: boolean): Promise<ResponseCategoriaType[]> {
        
        const response = await apiClient.get('/categoriaProduto/', {
            params: {
                status: status
            }
        })
        return response.data
    },

    async criar(dados: RequestCategoriaType): Promise<ResponseCategoriaType>{
        const response = await apiClient.post('/categoriaProduto/', dados)
        return response.data
    
    },

    async atualizar(id: number, dados: RequestCategoriaType): Promise<ResponseCategoriaType>{
        const response = await apiClient.put(`/categoriaProduto/${id}`, dados)
        return response.data
    },

    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/categoriaProduto/inativar/${id}`)
        return response.data
    }
}
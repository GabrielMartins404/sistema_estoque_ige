import apiClient from "./api/apiCllient";
import type { RequestFaculdadeType } from "@/hooks/faculdade/types/RequestFaculdade.type";
import type { ResponseFaculdadeType } from "@/hooks/faculdade/types/ResponseFaculdade.type";


export const FaculdadeServices = {
    async listarTodas(status: boolean): Promise<ResponseFaculdadeType[]> {
        const response = await apiClient.get('/faculdade/', {
            params: {
                status: status
            }
        })
        return response.data
        
    },

    async criar(dados: RequestFaculdadeType): Promise<ResponseFaculdadeType>{
        
            const response = await apiClient.post('/faculdade/', dados)
            return response.data
    },

    async atualizar(id: number, dados: RequestFaculdadeType): Promise<ResponseFaculdadeType>{
        const response = await apiClient.put(`/faculdade/${id}`, dados)
        return response.data
        
    },

    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/faculdade/inativar/${id}`)
        return response.data
        
    }
}
import { RequestRequisitanteType, ResponseRequisitanteType } from '@/hooks/requisitante/types';
import apiClient from './api/apiCllient'
import type { RequisitanteType } from '@/types/requisitanteType';

export const RequisitanteServices = {
    async listarTodos(status: boolean): Promise<ResponseRequisitanteType[]> {
        const response = await apiClient.get('/requisitante/',{
            params: {
                status: status
            }
        })
        return response.data
            
    },

    async criar(dados: RequestRequisitanteType): Promise<ResponseRequisitanteType>{
        if(dados.facRequisitanteId === 0){
            dados.facRequisitanteId = undefined
        }
        const response = await apiClient.post('/requisitante/', dados)
        return response.data
        
    },

    async atualizar(id: number, dados: RequestRequisitanteType): Promise<ResponseRequisitanteType>{
        const response = await apiClient.put(`/requisitante/${id}`, dados)
        return response.data
    },

    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/requisitante/inativar/${id}`)
        return response.data
    }
}
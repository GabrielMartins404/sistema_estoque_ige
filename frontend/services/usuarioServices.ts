import { RequestAlteraSenhaUsuarioType, RequestAtualizaUsuarioType, RequestCadastroUsuarioType, ResponseUsuarioType } from "@/hooks/usuario/types";
import apiClient from "./api/apiCllient";


export const UsuarioServices = {
    async listarTodos(status: boolean): Promise<ResponseUsuarioType[]> {
        const response = await apiClient.get('/usuario/', {
            params: {
                status: status
            }
        })
        return response.data
    },

    async criar(dados: RequestCadastroUsuarioType): Promise<ResponseUsuarioType>{
        const response = await apiClient.post('/usuario/', dados)
        return response.data
    },

    async atualizar(id: number, dados: RequestAtualizaUsuarioType): Promise<ResponseUsuarioType>{
        const response = await apiClient.put(`/usuario/${id}`, dados)
        return response.data
    },

    async alterarSenhaUsuario(id: number, dados: RequestAlteraSenhaUsuarioType): Promise<void>{
        const response = await apiClient.put(`/usuario/alterarSenha/${id}`, dados)
        return response.data
    },

    async resetarSenhaDeUsuario(id: number): Promise<void>{
        const response = await apiClient.put(`/usuario/resetarSenha/${id}`)
        return response.data
    },


    async inativar(id: number): Promise<void>{
        const response = await apiClient.put(`/usuario/inativar/${id}`)
        return response.data
    }
}
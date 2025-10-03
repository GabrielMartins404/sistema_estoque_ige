import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { UsuarioServices } from "@/services/usuarioServices";
import { RequestAlteraSenhaUsuarioType, RequestAtualizaUsuarioType, RequestCadastroUsuarioType, ResponseUsuarioType } from "./types";
import { useError } from "@/contexts/NotificationContext";
import { ApiError } from '@/services/api/errorsApi';

const QUERY_KEY = 'usuario'

export function useGetUsuarios(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => UsuarioServices.listarTodos(status),
    })
}

export function useCreateUsuario(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()
    return useMutation({
        mutationFn: (dados: RequestCadastroUsuarioType) => UsuarioServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Usuário criado com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao criar Usuario",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Usuario",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdateUsuario(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestAtualizaUsuarioType}) => UsuarioServices.atualizar(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Usuário atualizado com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar Usuário",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Usuário",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdatePasswordUsuario(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestAlteraSenhaUsuarioType}) => UsuarioServices.alterarSenhaUsuario(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Senha do Usuário atualizada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar senha do Usuário",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar senha do Usuário",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useResetPasswordUsuario(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => UsuarioServices.resetarSenhaDeUsuario(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Senha do Usuário resetada com sucesso para 123..",
                tipo: 'info'
            })
            addNotification({
                titulo: "Atenção",
                mensagem: "Solicite ao usuário a troca imediata para uma senha segura.",
                tipo: 'alerta'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao resetar senha do Usuário",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar senha do Usuário",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateUsuario(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => UsuarioServices.inativar(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Usuário inativado/ativo com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao inativar/ativar Usuário",
                mensagem: err instanceof Error ? err.message : "Erro ao inativar/ativar Usuário",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { RequisitanteServices } from "@/services/requisitanteServices";
import { RequestRequisitanteType, ResponseRequisitanteType } from "./types/index";
import { useError } from "@/contexts/NotificationContext";

const QUERY_KEY = 'requisitante'

export function useGetRequisitantes(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => RequisitanteServices.listarTodos(status),
    })
}

export function useCreateRequisitante(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (dados: RequestRequisitanteType) => RequisitanteServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Requisitante criado com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao criar Requisitante",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Requisitante",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdateRequisitante(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestRequisitanteType}) => RequisitanteServices.atualizar(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Requisitante atualizado com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar Requisitante",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Requisitante",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateRequisitante(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => RequisitanteServices.inativar(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Requisitante inativado com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao inativar Requisitante",
                mensagem: err instanceof Error ? err.message : "Erro ao inativar Requisitante",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
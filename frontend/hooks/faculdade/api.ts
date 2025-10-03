import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { FaculdadeServices } from "@/services/faculdadeServices";
import type { RequestFaculdadeType } from "./types/RequestFaculdade.type";
import { useError } from "@/contexts/NotificationContext";

const QUERY_KEY = 'faculdade'

export function useGetFaculdades(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => FaculdadeServices.listarTodas(status),
    })
}

export function useCreateFaculdade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (dados: RequestFaculdadeType) => FaculdadeServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Faculdade criada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao criar Faculdade",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Faculdade",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdateFaculdade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestFaculdadeType}) => FaculdadeServices.atualizar(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Faculdade atualizada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar Faculdade",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Faculdade",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateFaculdade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => FaculdadeServices.inativar(id),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao inativar Faculdade",
                mensagem: err instanceof Error ? err.message : "Erro ao inativar Faculdade",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
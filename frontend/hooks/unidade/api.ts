import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { UnidadeServices } from "@/services/unidadeServices";
import { RequestUnidadeType, ResponseUnidadeType } from "./types/index";
import { useError } from "@/contexts/NotificationContext";
import { ApiError } from '@/services/api/errorsApi';
import { useToast } from "@/components/ui/use-toast";

const QUERY_KEY = 'unidade'

export function useGetUnidades(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => UnidadeServices.listarTodos(status),
    })
}

export function useCreateUnidade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()
    const {toast} = useToast()

    return useMutation({
        mutationFn: (dados: RequestUnidadeType) => UnidadeServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Unidade criada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err: ApiError) => {
            addNotification({
                titulo: "Erro ao criar Unidade",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Unidade",
                tipo: 'error'
            })
        }
    })
}

export function useUpdateUnidade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestUnidadeType}) => UnidadeServices.atualizar(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Unidade atualizada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar Unidade",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Unidade",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateUnidade(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => UnidadeServices.inativar(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Unidade inativada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao inativar Unidade",
                mensagem: err instanceof Error ? err.message : "Erro ao inativar Unidade",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { CategoriaServices } from "@/services/categoriaServices";
import type { RequestCategoriaType } from "./types";
import { useError } from "@/contexts/NotificationContext";

const QUERY_KEY = 'categoria'

export function useGetCategorias(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => CategoriaServices.listarTodas(status),
    })
}

export function useCreateCategoria(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (dados: RequestCategoriaType) => CategoriaServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Categoria criada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao criar Categoria",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Categoria",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdateCategoria(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestCategoriaType}) => CategoriaServices.atualizar(id, dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Categoria atualizada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao atualizar Categoria",
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Categoria",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateCategoria(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => CategoriaServices.inativar(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Categoria inativada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao inativar Categoria",
                mensagem: err instanceof Error ? err.message : "Erro ao inativar Categoria",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
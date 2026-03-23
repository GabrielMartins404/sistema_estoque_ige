import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { ProdutoServices } from "@/services/produtoServices";
import type { RequestProdutoType } from "./types";
import type { ResponseProdutoType } from "./types";
import { useError } from "@/contexts/NotificationContext";

const QUERY_KEY = 'produto'

export function useGetProdutos(status: boolean){
    return useQuery({
        queryKey: [QUERY_KEY, status],
        queryFn: () => ProdutoServices.listarTodos(status),
    })
}

export function useCreateProduto(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (dados: RequestProdutoType) => ProdutoServices.criar(dados),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                mensagem: err instanceof Error ? err.message : "Erro ao criar Produto",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useUpdateProduto(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: ({id, dados}: {id: number, dados: RequestProdutoType}) => ProdutoServices.atualizar(id, dados),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
        },
        onError: (err) => {
            addNotification({
                mensagem: err instanceof Error ? err.message : "Erro ao atualizar Produto",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useInactivateProduto(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => ProdutoServices.inativar(id),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})            
        },
        onError: (err) => {
            addNotification({
                mensagem: err instanceof Error ? err.message : "Erro ao inativar Produto",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
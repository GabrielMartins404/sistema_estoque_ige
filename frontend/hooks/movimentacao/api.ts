import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { ProdutoServices } from "@/services/produtoServices";
import type { RequestMovimentacaoType } from "./types";
import type { ResponseMovimentacaoType } from "./types";
import { useError } from "@/contexts/NotificationContext";
import { MovimentacaoServices } from "@/services/movimentacaoServices";

const QUERY_KEY = 'movimentacao'

export function useGetMovimentacoes(tipo: string, status: string){
    if(tipo != "E" && tipo != "S"){
        tipo = "S"
    }
    if(status != "F" && status != "C"){
        status = "F"
    }
    return useQuery({
        queryKey: [QUERY_KEY, tipo, status],
        queryFn: () => MovimentacaoServices.listarMovimentacoes(tipo, status),
    })
}

export function useCreateMovimentacao(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()
    
    return useMutation({
        mutationFn: (dados: RequestMovimentacaoType) => MovimentacaoServices.criar(dados),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Movimentação criada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
            queryClient.invalidateQueries({ queryKey: ['produto', true] });
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao criar Movimentação",
                mensagem: err instanceof Error ? err.message : "Erro ao criar Movimentação",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}

export function useCancelMovimentacao(){
    const queryClient = useQueryClient()
    const {addNotification} = useError()

    return useMutation({
        mutationFn: (id: number) => MovimentacaoServices.cancelar(id),
        onSuccess: () => {
            addNotification({
                titulo: "Sucesso!",
                mensagem: "Movimentação cancelada com sucesso.",
                tipo: 'info'
            })
            queryClient.invalidateQueries({queryKey: [QUERY_KEY]})
            queryClient.invalidateQueries({ queryKey: ['produto', true] });       
        },
        onError: (err) => {
            addNotification({
                titulo: "Erro ao cancelar Movimentação",
                mensagem: err instanceof Error ? err.message : "Erro ao cancelar Movimentação",
                tipo: 'error'
            })
            console.log(err)
        }
    })
}
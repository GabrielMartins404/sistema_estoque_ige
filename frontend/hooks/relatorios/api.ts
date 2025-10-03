import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { RelatorioServices } from "@/services/relatorioServices";
import { ProdutosPorRequisitanteType, ProdutoMaisMovimentadoType } from "./types";
import { useError } from "@/contexts/NotificationContext";

const QUERY_KEY = 'relatorios'

export function useGetProdutosMaisMovimentados(){
    return useQuery({
        queryKey: [QUERY_KEY, 'produtosMaisMovimentados'],
        queryFn: () => RelatorioServices.produtoMaisMovimentado(),
    })
}

export function useGetProdutosPorRequisitante(){
    return useQuery({
        queryKey: [QUERY_KEY, 'produtosPorRequisitante'],
        queryFn: () => RelatorioServices.produtosPorRequisitante(),
    })
}

export function useGetQtdMov(tipo: string, status: string){
    return useQuery({
        queryKey: [QUERY_KEY, tipo, status],
        queryFn: () => RelatorioServices.buscarQtdMov(tipo, status),
    })
}

export function useGetQtdProdutosAtivos(){
    return useQuery({
        queryKey: [QUERY_KEY, 'qtdProdutosAtivos'],
        queryFn: () => RelatorioServices.buscarQtdProdutosAtivos(),
    })
}

export function useGetQtdProdutosAbaixoMin(){
    return useQuery({
        queryKey: [QUERY_KEY, 'qtdProdutosAbaixoMin'],
        queryFn: () => RelatorioServices.buscarProdutosAbaixoMin(),
    })
}

import { useState } from "react";
import {
    useCreateProduto,
    useGetProdutos,
    useUpdateProduto,
    useInactivateProduto
} from './api'

export function useProdutoManager(status: boolean){

    const {data: produtos = [], isLoading, isError, error} = useGetProdutos(status)
    const {mutate: cadastrarProduto, isPending: isCreating} = useCreateProduto()
    const {mutate: atualizarProduto, isPending: isUpdating} = useUpdateProduto()
    const {mutate: inativarProduto, isPending: isDeleting} = useInactivateProduto()

    const loading = isCreating || isUpdating || isDeleting
    
    return {
        produtos,
        status,
        isLoading,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarProduto,
        atualizarProduto,
        inativarProduto
    }
}
import { useState } from "react";
import {
    useCreateCategoria,
    useGetCategorias,
    useUpdateCategoria,
    useInactivateCategoria,
} from './api'

export function useCategoriaManager(status: boolean){

    const {data: categorias = [], isLoading, isError, error} = useGetCategorias(status)
    const {mutate: cadastrarCategoria, isPending: isCreating} = useCreateCategoria()
    const {mutate: atualizarCategoria, isPending: isUpdating} = useUpdateCategoria()
    const {mutate: inativarCategoria, isPending: isDeleting} = useInactivateCategoria()

    const loading = isCreating || isUpdating || isDeleting
    
    return {
        categorias,
        status,
        isLoading,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarCategoria,
        atualizarCategoria,
        inativarCategoria
    }
}
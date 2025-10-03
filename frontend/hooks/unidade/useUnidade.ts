import { useState } from "react";
import {
    useCreateUnidade,
    useGetUnidades,
    useInactivateUnidade,
    useUpdateUnidade
} from './api'

export function useUnidadeManager(status: boolean){

    const {data: unidades = [], isLoading, isError, error} = useGetUnidades(status)
    const {mutate: cadastrarUnidade, isPending: isCreating} = useCreateUnidade()
    const {mutate: atualizarUnidade, isPending: isUpdating} = useUpdateUnidade()
    const {mutate: inativarUnidade, isPending: isDeleting} = useInactivateUnidade()

    const loading = isCreating || isUpdating || isDeleting
    
    return {
        unidades,
        status,
        isLoading,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarUnidade,
        atualizarUnidade,
        inativarUnidade
    }
}
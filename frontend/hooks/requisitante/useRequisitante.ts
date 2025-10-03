import { useState } from "react";
import {
    useCreateRequisitante,
    useGetRequisitantes,
    useUpdateRequisitante,
    useInactivateRequisitante,
} from './api'

export function useRequisitanteManager(status: boolean){

    const {data: requisitantes = [], isLoading, isError, error} = useGetRequisitantes(status)
    const {mutate: cadastrarRequisitante, isPending: isCreating} = useCreateRequisitante()
    const {mutate: atualizarRequisitante, isPending: isUpdating} = useUpdateRequisitante()
    const {mutate: inativarRequisitante, isPending: isDeleting} = useInactivateRequisitante()

    const loading = isCreating || isUpdating || isDeleting
    
    return {
        requisitantes,
        status,
        isLoading,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarRequisitante,
        atualizarRequisitante,
        inativarRequisitante
    }
}
import { useState } from "react";
import {
    useCreateFaculdade,
    useGetFaculdades,
    useUpdateFaculdade,
    useInactivateFaculdade,
} from './api'

export function useFaculdadeManager(status: boolean){

    const {data: faculdades = [], isLoading, isError, error} = useGetFaculdades(status)
    const {mutate: cadastrarFaculdade, isPending: isCreating} = useCreateFaculdade()
    const {mutate: atualizarFaculdade, isPending: isUpdating} = useUpdateFaculdade()
    const {mutate: inativarFaculdade, isPending: isDeleting} = useInactivateFaculdade()

    const loading = isCreating || isUpdating || isDeleting
    
    return {
        faculdades,
        status,
        isLoading,
        loading,
        isError,
        error,

        //Ações
        cadastrarFaculdade,
        atualizarFaculdade,
        inativarFaculdade,
        isDeleting
    }
}
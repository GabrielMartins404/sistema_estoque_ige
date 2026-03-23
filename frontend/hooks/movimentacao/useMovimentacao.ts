import {
    useGetMovimentacoes,
    useCreateMovimentacao,
    useCancelMovimentacao,
} from './api'

export function useMovimentacaoManager(tipo: string, status: string){

    const {data: movimentacoes = [], isLoading, isError, error} = useGetMovimentacoes(tipo, status)
    const {mutate: cadastrarMovimentacao, isPending: isCreating} = useCreateMovimentacao()
    const {mutate: cancelarMovimentacao, isPending: isDeleting} = useCancelMovimentacao()

    const loading = isCreating || isDeleting
    
    return {
        movimentacoes,
        status,
        isLoading,
        isCreating,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarMovimentacao,
        cancelarMovimentacao
    }
}
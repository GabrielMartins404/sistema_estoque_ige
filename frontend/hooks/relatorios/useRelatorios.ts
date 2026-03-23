import { useState } from "react";
import {
    useGetProdutosMaisMovimentados,
    useGetProdutosPorRequisitante,
    useGetQtdMov,
    useGetQtdProdutosAtivos,
    useGetQtdProdutosAbaixoMin,
} from './api'

export function useRequisitanteManager(status: string, tipo: string){

    const {data: produtoMaisMovimentado = [], isLoading: isLoadingProdutosMaisMovimentados, isError: isErrorProdutosMaisMovimentados, error: errorProdutosMaisMovimentados} = useGetProdutosMaisMovimentados()
    const {data: produtosPorRequisitante = [], isLoading: isLoadingProdutosPorRequisitante, isError: isErrorProdutosPorRequisitante, error: errorProdutosPorRequisitante} = useGetProdutosPorRequisitante()
    const {data: qtdMov = [], isLoading: isLoadingQtdMov, isError: isErrorQtdMov, error: errorQtdMov} = useGetQtdMov(tipo, status)
    const {data: qtdProdutosAtivos = [], isLoading: isLoadingQtdProdutosAtivos, isError: isErrorQtdProdutosAtivos, error: errorQtdProdutosAtivos} = useGetQtdProdutosAtivos()   
    const {data: qtdProdutosAbaixoMin = [], isLoading: isLoadingQtdProdutosAbaixoMin, isError: isErrorQtdProdutosAbaixoMin, error: errorQtdProdutosAbaixoMin} = useGetQtdProdutosAbaixoMin()
    
    const loading = isLoadingProdutosMaisMovimentados || isLoadingProdutosPorRequisitante || isLoadingQtdMov || isLoadingQtdProdutosAtivos || isLoadingQtdProdutosAbaixoMin
    return {
        produtoMaisMovimentado,
        produtosPorRequisitante,
        qtdMov,
        qtdProdutosAtivos,
        qtdProdutosAbaixoMin,
        status,
        isLoading: loading,
        isError: isErrorProdutosMaisMovimentados || isErrorProdutosPorRequisitante || isErrorQtdMov || isErrorQtdProdutosAtivos || isErrorQtdProdutosAbaixoMin,
        error: errorProdutosMaisMovimentados || errorProdutosPorRequisitante || errorQtdMov || errorQtdProdutosAtivos || errorQtdProdutosAbaixoMin
    }
}
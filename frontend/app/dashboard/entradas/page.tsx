"use client"

import type React from "react"

import { useMemo, useState } from "react"
import { PackagePlus, Plus, Search, ShoppingCart, Trash2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {MovimentacaoFormValues} from "./components/MovimentacaoForm"

import { Badge } from "@/components/ui/badge"

import ProtectedRoute from "@/components/ProtectedRoutes"
import { useAuth } from "@/contexts/UsuarioContext"
import Loading from "./loading"
import { formatarDataEHorario } from "@/functions/formatarDataHora"
import { MovimentacaoForm } from "./components/MovimentacaoForm"
import { useMovimentacaoManager } from "@/hooks/movimentacao/useMovimentacao"
import { RequestMovimentacaoType, RequestProdutoMovimentacaoType } from "@/hooks/movimentacao/types"

export default function MovimentacoesPage() {
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [busca, setBusca] = useState("");
    const { movimentacoes, isLoading, cadastrarMovimentacao, cancelarMovimentacao, isCreating } = useMovimentacaoManager("E", "F")

    const handleFormSubmit = (formValues: MovimentacaoFormValues) => {
        // Adicionar dados fixos que não vêm do formulário
        // 1. Mapeia a lista de produtos para o formato que a API espera.
        const produtosParaApi: RequestProdutoMovimentacaoType[] = formValues.produtosMov.map(item => ({
            proMovProduto: item.produto.proId,
            proMovQtdProduto: item.qtdProduto,
            proMovCustoProduto: item.custoProduto,
        }));


        // 2. Monta o objeto final para a requisição, adicionando os campos fixos.
        const apiRequestData: RequestMovimentacaoType = {
            movNf: formValues.movNf || "",
            movNumRequisicao: formValues.movNumRequisicao || "",
            movObservacao: formValues.movObservacao || "",
            movRequisitanteId: Number(1), // Converte para número se necessário
            produtosMov: produtosParaApi,
            // Adiciona os valores fixos que não vêm do formulário
            movStatus: "F", // Ou qualquer outro status inicial
            movTipo: "E",
            movOrigem: "NOR",
        };
        console.log(movimentacoes)
        cadastrarMovimentacao(apiRequestData);
        setIsFormOpen(false); // Fecha o modal no sucesso
    };

    const movimentacoesFiltradas = useMemo(() =>
        movimentacoes.filter((movimentacao) =>
            (movimentacao.movNumRequisicao || '').toLowerCase().includes(busca.toLowerCase()) ||
            (movimentacao.movNf || '').toLowerCase().includes(busca.toLowerCase()) ||
            (movimentacao.movRequisitanteNome || '').toLowerCase().includes(busca.toLowerCase()) ||
            (movimentacao.movUsuarioNome || '').toLowerCase().includes(busca.toLowerCase())
        ), 
    [movimentacoes, busca]);

    const handleCancel = (id: number) => {
        if (confirm("Tem certeza que deseja cancelar esta entrada? \nEste é um processo incancelável!")) {
            cancelarMovimentacao(id)
        }
    }
    if (isLoading) return <Loading />;

    return (
        <ProtectedRoute>
            <div className="p-6">
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-2xl font-bold flex items-center gap-2">
                        <PackagePlus className="h-6 w-6" />
                        Entradas de Estoque
                    </h1>
                    <Button onClick={() => setIsFormOpen(true)} className="bg-[#1e3a8a]">
                        <Plus className="h-4 w-4 mr-2" />
                        Nova Movimentação
                    </Button>
                </div>

                <div className="bg-white rounded-lg shadow-sm p-6">
                    <div className="flex gap-4 mb-6">
                        <div className="relative flex-1">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                            <Input
                                type="text"
                                placeholder="Buscar movimentações..."
                                className="pl-10"
                                value={busca}
                                onChange={(e) => setBusca(e.target.value)}
                            />
                        </div>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                                <tr className="border-b">
                                    <th className="text-left py-3 px-4">Data e Horário</th>
                                    <th className="text-left py-3 px-4">Usuario</th>
                                    <th className="text-left py-3 px-4">Nº NF</th>
                                    <th className="text-left py-3 px-4">Requisitante</th>
                                    <th className="text-left py-3 px-4">Itens</th>

                                </tr>
                            </thead>
                            <tbody>
                                {movimentacoesFiltradas.map((movimentacao, index) => (
                                    <tr key={index} className="border-b hover:bg-gray-50">
                                        <td className="py-3 px-4">{formatarDataEHorario(movimentacao.movData, movimentacao.movHorario)}</td>
                                        <td className="py-3 px-4">{movimentacao.movUsuarioNome}</td>
                                        <td className="py-3 px-4">{movimentacao.movNf}</td>
                                        <td className="py-3 px-4">{movimentacao.movRequisitanteNome}</td>
                                        <td className="py-3 px-4">
                                            <div className="flex flex-wrap gap-1">
                                                {movimentacao.produtos.map((item, index) => {
                                                    return(
                                                        <Badge key={index} variant="outline" className="text-xs">
                                                            {item.produto.proNome} - {item.proMovQtdProduto}
                                                        </Badge>
                                                    )
                                                })}
                                            </div>
                                        </td>
                                        <td className="py-3 px-4">
                                            <Button variant="outline" size="icon" onClick={() => handleCancel(movimentacao.movId)}>
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                                
                <Dialog open={isFormOpen} onOpenChange={setIsFormOpen}>
                    <DialogContent className="max-w-3xl">
                        <DialogHeader><DialogTitle>Nova Movimentação de Estoque</DialogTitle></DialogHeader>
                        <MovimentacaoForm
                            onSubmit={handleFormSubmit}
                            onCancel={() => setIsFormOpen(false)}
                            isSubmitting={isCreating}
                        />
                    </DialogContent>
                </Dialog>
            </div>
        </ProtectedRoute>
    )
}

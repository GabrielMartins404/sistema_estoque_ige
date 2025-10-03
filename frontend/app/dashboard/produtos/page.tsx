"use client"

import type React from "react"

import { useState, useMemo } from "react"
import { Plus, Search, Edit, Trash2, Notebook, Box } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import ProtectedRoute from "@/components/ProtectedRoutes"
import type { ModalState } from "@/types/modalState.type"
import RadioButtonStatus from "@/components/RadioButtonStatus"
import { ProdutoForm } from "./components/ProdutoForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"
import { RequestProdutoType } from "@/hooks/produto/types"
import { useProdutoManager } from "@/hooks/produto/useProduto"
import { useCategoriaManager } from "@/hooks/categoria/useCategoria"
import { useUnidadeManager } from "@/hooks/unidade/useUnidade"

export default function ProdutoPage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestProdutoType>>({ type: 'closed' });
  const { produtos, cadastrarProduto, atualizarProduto, inativarProduto, isLoading, isDeleting } = useProdutoManager(status);
  const { categorias } = useCategoriaManager(true);
  const { unidades } = useUnidadeManager(true);

  // 2. Memoizar a filtragem para otimização
  const produtosFiltradas = useMemo(() =>
    produtos.filter((pro) =>
      pro.proNome?.toLowerCase().includes(busca.toLowerCase()) ||
      pro.proDescricao?.toLowerCase().includes(busca.toLowerCase()) ||
      pro.proCategoriaNome?.toLowerCase().includes(busca.toLowerCase()) ||
      pro.proUnNome?.toLowerCase().includes(busca.toLowerCase()) ||
      pro.proSipac?.toLowerCase().includes(busca.toLowerCase())
    ), [produtos, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: RequestProdutoType) => {
    if (modalState.type === 'edit') {
      atualizarProduto({ id: modalState.id, dados: values });
    } else {
      cadastrarProduto(values);
    }
    setModalState({ type: 'closed' });
  };
  
  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
      if (confirm("Tem certeza que deseja inativar/ativar este produto? ")) {
          inativarProduto(id);
      }
  };
  
  if (isLoading) return <Loading />;

  return (
    <ProtectedRoute>
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2"><Box className="h-5 w-5" /> Gerenciamento de Produtos</h1>
          <Button onClick={() => setModalState({ type: 'new' })} className="bg-[#1e3a8a]"><Plus /> Novo Produto</Button>
        </div>
        
        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Produtos..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
            <RadioButtonStatus status={status ? 1 : 0} onStatusChange={(newStatus) => setStatus(newStatus === 1)} />
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left py-3 px-4">Nome</th>
                  <th className="text-left py-3 px-4">Categoria</th>
                  <th className="text-left py-3 px-4">Unidade</th>
                  <th className="text-left py-3 px-4">SIPAC</th>
                  <th className="text-left py-3 px-4">Est. Min</th>
                  <th className="text-left py-3 px-4">Estoque</th>
                  <th className="text-left py-3 px-4">Vlr. Custo</th>
                  <th className="text-left py-3 px-4">Status</th>
                  <th className="text-left py-3 px-4">Ações</th>
                  

                </tr>
              </thead>
              <tbody>
                {produtosFiltradas.map((produto) => (
                  <tr key={produto.proId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{produto.proNome}</td>
                    <td className="py-3 px-4">{produto.proCategoriaNome}</td>
                    <td className="py-3 px-4">{produto.proUnNome}</td>
                    <td className="py-3 px-4">{produto.proSipac}</td>
                    <td className="py-3 px-4">{produto.proEstoqueMin}</td>
                    <td className="py-3 px-4">{produto.proQtd}</td>
                    <td className="py-3 px-4">R$ {produto.proCusto.toFixed(2)}</td>
                    <td className="py-3 px-4">
                    {produto.isAbaixoMin ? (
                        <span className="px-2 py-1 bg-red-100 text-red-800 rounded-full text-xs">Baixo</span>
                      ) : (
                        <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs">Normal</span>
                      )}
                    </td>

                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: produto, id: produto.proId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(produto.proId)}><Trash2 className="h-4 w-4" /></Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Modal de Formulário (Criar/Editar) */}
        <Dialog open={modalState.type === 'new' || modalState.type === 'edit'} onOpenChange={() => setModalState({ type: 'closed' })}>
          <DialogContent className="sm:max-w-[625px]">
            <DialogHeader>
              <DialogTitle>{modalState.type === 'edit' ? "Editar Produto" : "Novo Produto"}</DialogTitle>
            </DialogHeader>
            {/* 2. Passar os dados dos selects para o formulário */}
            <ProdutoForm
              produtoAtual={modalState.type === 'edit' ? modalState.dados : null}
              categoriaList={categorias}
              unidadeList={unidades}
              onSubmit={handleFormSubmit}
              onCancel={() => setModalState({ type: 'closed' })}
              isSubmitting={isLoading}
            />
          </DialogContent>
        </Dialog>

      </div>
    </ProtectedRoute>
  );
}
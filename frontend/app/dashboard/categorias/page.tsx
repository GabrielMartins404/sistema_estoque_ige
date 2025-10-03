"use client"

import type React from "react"

import { useState, useMemo } from "react"
import { Plus, Search, Edit, Trash2, Notebook } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import ProtectedRoute from "@/components/ProtectedRoutes"
import type { ModalState } from "@/types/modalState.type"
import { useAuth } from "@/contexts/UsuarioContext"
import RadioButtonStatus from "@/components/RadioButtonStatus"
import { RequestCategoriaType } from "@/hooks/categoria/types"
import { useCategoriaManager } from "@/hooks/categoria/useCategoria"
import { CategoriaForm } from "./components/CategoriaForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"

export default function CategoriaPage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestCategoriaType>>({ type: 'closed' });
  const { categorias, cadastrarCategoria, atualizarCategoria, inativarCategoria, isLoading, isDeleting } = useCategoriaManager(status);

  // 2. Memoizar a filtragem para otimização
  const categoriasFiltradas = useMemo(() =>
    categorias.filter((cat) =>
      cat.catProNome?.toLowerCase().includes(busca.toLowerCase())
    ), [categorias, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: { catProNome: string }) => {
    if (modalState.type === 'edit') {
      atualizarCategoria({ id: modalState.id, dados: values });
    } else {
      cadastrarCategoria(values);
    }
    setModalState({ type: 'closed' });
  };

  // 4. Lógica de confirmação de exclusão
  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
      if (confirm("Tem certeza que deseja inativar/ativar esta categoria? ")) {
          inativarCategoria(id);
      }
  };
  
  if (isLoading) return <Loading />;

  return (
    <ProtectedRoute>
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2"><Notebook /> Gerenciamento de Categoria</h1>
          <Button onClick={() => setModalState({ type: 'new' })} className="bg-[#1e3a8a]"><Plus /> Nova categoria</Button>
        </div>
        
        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Categoria..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
            <RadioButtonStatus status={status ? 1 : 0} onStatusChange={(newStatus) => setStatus(newStatus === 1)} />
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead><tr className="border-b"><th className="text-left py-3 px-4">Nome</th><th className="text-left py-3 px-4">Ações</th></tr></thead>
              <tbody>
                {categoriasFiltradas.map((categoria) => (
                  <tr key={categoria.catProId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{categoria.catProNome}</td>
                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: categoria, id: categoria.catProId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(categoria.catProId)}><Trash2 className="h-4 w-4" /></Button>
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
          <DialogContent>
            <DialogHeader>
              <DialogTitle>{modalState.type === 'edit' ? "Editar categoria" : "Nova categoria"}</DialogTitle>
            </DialogHeader>
            <CategoriaForm
              categoriaAtual={modalState.type === 'edit' ? modalState.dados : null}
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
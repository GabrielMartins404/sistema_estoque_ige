"use client"

import type React from "react"

import { useState, useMemo } from "react"
import { Plus, Search, Edit, Trash2, Notebook, UserRoundPlus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import ProtectedRoute from "@/components/ProtectedRoutes"
import type { ModalState } from "@/types/modalState.type"
import RadioButtonStatus from "@/components/RadioButtonStatus"
import { RequisitanteForm } from "./components/RequisitanteForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"
import { useFaculdadeManager } from "@/hooks/faculdade/useFaculdade"
import { useRequisitanteManager } from "@/hooks/requisitante/useRequisitante"
import { RequestRequisitanteType } from "@/hooks/requisitante/types"

export default function CategoriaPage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestRequisitanteType>>({ type: 'closed' });
  const { requisitantes, cadastrarRequisitante, atualizarRequisitante, inativarRequisitante, isLoading, isDeleting } = useRequisitanteManager(status);
  const { faculdades } = useFaculdadeManager(true)
  // 2. Memoizar a filtragem para otimização
  const requisitantesFiltrado = useMemo(() =>
    requisitantes.filter((req) =>
      req.reqNome?.toLowerCase().includes(busca.toLowerCase()) ||
      req.facNome?.toLowerCase().includes(busca.toLowerCase())
      //req.reqFacSigla?.toLowerCase().includes(busca.toLowerCase())

    ), [requisitantes, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: RequestRequisitanteType) => {
    if (modalState.type === 'edit') {
      atualizarRequisitante({ id: modalState.id, dados: values });
    } else {
      cadastrarRequisitante(values);
    }
    setModalState({ type: 'closed' });
  };

  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
      if (confirm("Tem certeza que deseja inativar/ativar este requisitante? ")) {
          inativarRequisitante(id);
      }
  };
  
  if (isLoading) return <Loading />;

  return (
    <ProtectedRoute>
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2"><UserRoundPlus className="h-5 w-5" /> Gerenciamento de Requisitante</h1>
          <Button onClick={() => setModalState({ type: 'new' })} className="bg-[#1e3a8a]"><Plus /> Novo Requisitante</Button>
        </div>
        
        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Requisitante..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
            <RadioButtonStatus status={status ? 1 : 0} onStatusChange={(newStatus) => setStatus(newStatus === 1)} />
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left py-3 px-4">Nome</th>
                  <th className="text-left py-3 px-4">Faculdade</th>
                  <th className="text-left py-3 px-4">Sigla Faculdade</th>
                  <th className="text-left py-3 px-4">Ações</th></tr></thead>
              <tbody>
                {requisitantesFiltrado.map((requisitante) => (
                  <tr key={requisitante.reqId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{requisitante.reqNome}</td>
                    <td className="py-3 px-4">{requisitante.facNome}</td>
                    <td className="py-3 px-4">{requisitante.facSigla}</td>
                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: requisitante, id: requisitante.reqId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(requisitante.reqId)}><Trash2 className="h-4 w-4" /></Button>
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
            <RequisitanteForm
              requisitanteAtual={modalState.type === 'edit' ? modalState.dados : null}
              faculdadeList={faculdades}
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
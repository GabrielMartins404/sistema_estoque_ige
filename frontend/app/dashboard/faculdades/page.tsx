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
import { FaculdadeForm } from "./components/FaculdadeForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"
import { RequestFaculdadeType } from "@/hooks/faculdade/types"
import { useFaculdadeManager } from "@/hooks/faculdade/useFaculdade"

export default function FaculdadePage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestFaculdadeType>>({ type: 'closed' });
  const { faculdades, cadastrarFaculdade, atualizarFaculdade, inativarFaculdade, isLoading, isDeleting } = useFaculdadeManager(status);

  // 2. Memoizar a filtragem para otimização
  const faculdadesFiltradas = useMemo(() =>
    faculdades.filter((fac) =>
      fac.facNome?.toLowerCase().includes(busca.toLowerCase()) ||
      fac.facSigla?.toLowerCase().includes(busca.toLowerCase())
    ), [faculdades, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: RequestFaculdadeType) => {
    if (modalState.type === 'edit') {
      atualizarFaculdade({ id: modalState.id, dados: values });
    } else {
      cadastrarFaculdade(values);
    }
    setModalState({ type: 'closed' });
  };

  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
      if (confirm("Tem certeza que deseja inativar/ativar esta faculdade? ")) {
          inativarFaculdade(id);
      }
  };



  if (isLoading) return <Loading />;

  return (
    <ProtectedRoute>
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2"><Notebook /> Gerenciamento de Faculdade</h1>
          <Button onClick={() => setModalState({ type: 'new' })} className="bg-[#1e3a8a]"><Plus /> Nova faculdade</Button>
        </div>

        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Faculdade..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
            <RadioButtonStatus status={status ? 1 : 0} onStatusChange={(newStatus) => setStatus(newStatus === 1)} />
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left py-3 px-4">Nome</th>
                  <th className="text-left py-3 px-4">Sigla</th>
                  <th className="text-left py-3 px-4">Ações</th>
                </tr>
              </thead>
              <tbody>
                {faculdadesFiltradas.map((faculdade) => (
                  <tr key={faculdade.facId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{faculdade.facNome}</td>
                    <td className="py-3 px-4">{faculdade.facSigla}</td>
                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: faculdade, id: faculdade.facId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(faculdade.facId)}><Trash2 className="h-4 w-4" /></Button>
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
              <DialogTitle>{modalState.type === 'edit' ? "Editar faculdade" : "Nova faculdade"}</DialogTitle>
            </DialogHeader>
            <FaculdadeForm
              faculdadeAtual={modalState.type === 'edit' ? modalState.dados : null}
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
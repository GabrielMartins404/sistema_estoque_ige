"use client"

import type React from "react"

import { useState, useMemo } from "react"
import { Plus, Search, Edit, Trash2, Notebook, FileText } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import ProtectedRoute from "@/components/ProtectedRoutes"
import type { ModalState } from "@/types/modalState.type"
import { useAuth } from "@/contexts/UsuarioContext"
import RadioButtonStatus from "@/components/RadioButtonStatus"
import { UnidadeForm } from "./components/UnidadeForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"
import { RequestUnidadeType } from "@/hooks/unidade/types"
import { useUnidadeManager } from "@/hooks/unidade/useUnidade"
import { useToast } from "@/hooks/use-toast"

export default function UnidadePage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestUnidadeType>>({ type: 'closed' });
  const { unidades, cadastrarUnidade, atualizarUnidade, inativarUnidade, isLoading, isDeleting } = useUnidadeManager(status);
  const { toast } = useToast();
  // 2. Memoizar a filtragem para otimização
  const unidadesFiltradas = useMemo(() =>
    unidades.filter((un) =>
      un.unNome?.toLowerCase().includes(busca.toLowerCase()) ||
      un.unSigla?.toLowerCase().includes(busca.toLowerCase())
    ), [unidades, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: RequestUnidadeType) => {
    if (modalState.type === 'edit') {
      atualizarUnidade({ id: modalState.id, dados: values });
    } else {
      cadastrarUnidade(values);
    }
    setModalState({ type: 'closed' });
  };

  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
      if (confirm("Tem certeza que deseja inativar/ativar esta unidade? ")) {
          inativarUnidade(id);
      }
  };
  
  if (isLoading) return <Loading />;

  return (
    <ProtectedRoute>
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2"><FileText className="h-5 w-5" /> Gerenciamento de Unidade</h1>
          <Button onClick={() => {
            setModalState({ type: 'new' })
            //oast({ title: "Sucesso!", description: "Unidade criada com sucesso." });
            }} className="bg-[#1e3a8a]"><Plus /> Nova unidade</Button>
        </div>
        
        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Unidade..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
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
                {unidadesFiltradas.map((unidade) => (
                  <tr key={unidade.unId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{unidade.unNome}</td>
                    <td className="py-3 px-4">{unidade.unSigla}</td>
                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: unidade, id: unidade.unId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(unidade.unId)}><Trash2 className="h-4 w-4" /></Button>
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
              <DialogTitle>{modalState.type === 'edit' ? "Editar Unidade" : "Nova Unidade"}</DialogTitle>
            </DialogHeader>
            <UnidadeForm
              unidadeAtual={modalState.type === 'edit' ? modalState.dados : null}
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
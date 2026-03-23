"use client"

import type React from "react"

import { useState, useMemo } from "react"
import { Plus, Search, Edit, Trash2, Notebook, ListRestart, Users } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import ProtectedRoutesRole from "@/components/ProtectedRoutesRole"
import type { ModalState } from "@/types/modalState.type"
import RadioButtonStatus from "@/components/RadioButtonStatus"
import { UsuarioForm } from "./components/UsuarioForm"
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog"
import { AlertDialogFooter, AlertDialogHeader } from "@/components/ui/alert-dialog"
import { RequestCadastroUsuarioType, RequestAtualizaUsuarioType } from "@/hooks/usuario/types"
import { useUsuarioManager } from "@/hooks/usuario/useUsuario"

export default function UsuarioPage() {
  const [busca, setBusca] = useState("");
  const [status, setStatus] = useState(true);
  const [modalState, setModalState] = useState<ModalState<RequestCadastroUsuarioType | RequestAtualizaUsuarioType>>({ type: 'closed' });
  const { usuarios, cadastrarUsuario, atualizarUsuario, inativarUsuario, resetarSenhaUsuario, isLoading, isDeleting } = useUsuarioManager(status);

  // 2. Memoizar a filtragem para otimização
  const usuariosFiltradas = useMemo(() =>
    usuarios.filter((usu) =>
      usu.usuNome?.toLowerCase().includes(busca.toLowerCase()) ||
      usu.usuEmail?.toLowerCase().includes(busca.toLowerCase()) ||
      usu.usuPerfilDescricao?.toLowerCase().includes(busca.toLowerCase())
    ), [usuarios, busca]);

  // 3. Lógica de submissão do formulário
  const handleFormSubmit = (values: RequestCadastroUsuarioType | RequestAtualizaUsuarioType) => {
    if (modalState.type === 'edit') {
      atualizarUsuario({ id: modalState.id, dados: values as RequestAtualizaUsuarioType });
    } else {
      cadastrarUsuario(values as RequestCadastroUsuarioType);
    }
    setModalState({ type: 'closed' });
  };

  // 4. Lógica de confirmação de exclusão
  const handleConfirmDelete = (id: number) => {
    if (confirm("Tem certeza que deseja inativar/ativar este usuário? ")) {
      inativarUsuario(id);
    }
  };

  const resetarSenha = (id: number) => {
    if (confirm("Tem certeza que deseja resetar a senha deste usuário? A senha será alterada para 123")) {
      resetarSenhaUsuario(id);
    }
  }
  
  if (isLoading) return <Loading />;

  return (
    <ProtectedRoutesRole allowedRole="ADMIN">
      <div className="p-6">
        {/* Header da Página */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Users className="h-6 w-6" />
            Gerenciamento de Usuários
          </h1>
          <Button onClick={() => setModalState({ type: 'new' })} className="bg-[#1e3a8a]"><Plus /> Novo Usuário</Button>
        </div>
        
        {/* Filtros e Tabela */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div className="flex gap-4 mb-6">
            <div className="relative flex-1"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><Input type="text" placeholder="Buscar Usuários..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} /></div>
            <RadioButtonStatus status={status ? 1 : 0} onStatusChange={(newStatus) => setStatus(newStatus === 1)} />
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left py-3 px-4">Nome</th>
                  <th className="text-left py-3 px-4">Perfil</th>
                  <th className="text-left py-3 px-4">Email</th>
                  <th className="text-left py-3 px-4">Ações</th>
                  <th className="text-left py-3 px-4">Resetar Senha</th>

                </tr>
              </thead>
              <tbody>
                {usuariosFiltradas.map((usu) => (
                  <tr key={usu.usuId} className="border-b hover:bg-gray-50">
                    <td className="py-3 px-4">{usu.usuNome}</td>
                    <td className="py-3 px-4">{usu.usuPerfilDescricao}</td>
                    <td className="py-3 px-4">{usu.usuEmail}</td>

                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <Button variant="outline" size="icon" onClick={() => setModalState({ type: 'edit', dados: usu, id: usu.usuId })}><Edit className="h-4 w-4" /></Button>
                        <Button variant="outline" size="icon" onClick={() => handleConfirmDelete(usu.usuId)}><Trash2 className="h-4 w-4" /></Button>
                      </div>
                    </td>
                    <td className="py-3 px-4">
                        <Button variant="outline" size="icon" onClick={() => resetarSenha(usu.usuId)}>
                          <ListRestart className="h-4 w-4" />
                        </Button>
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
              <DialogTitle>{modalState.type === 'edit' ? "Editar Usuário" : "Novo Usuário"}</DialogTitle>
            </DialogHeader>
            {/* 2. Passar os dados dos selects para o formulário */}
            <UsuarioForm
              usuarioAtual={modalState.type === 'edit' ? modalState.dados : null}
              onSubmit={handleFormSubmit}
              onCancel={() => setModalState({ type: 'closed' })}
              isSubmitting={isLoading}
            />
          </DialogContent>
        </Dialog>

      </div>
    </ProtectedRoutesRole>
  );
}
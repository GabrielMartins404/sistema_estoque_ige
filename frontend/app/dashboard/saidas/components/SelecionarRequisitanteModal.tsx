// src/features/movimentacoes/components/SelecionarProdutoModal.tsx

import { useState, useMemo } from 'react';
import { useMovimentacaoManager } from '@/hooks/movimentacao/useMovimentacao'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from '@/components/ui/label';
import { Search, ArrowRight } from 'lucide-react';
import { RequestProdutoType, ResponseProdutoType } from '@/hooks/produto/types';
import { RequestProdutoMovimentacaoType } from '@/hooks/movimentacao/types';
import { useRequisitanteManager } from '@/hooks/requisitante/useRequisitante';
import { ResponseRequisitanteType } from '@/hooks/requisitante/types';

interface SelecionarRequisitanteModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSelectRequisitante: (item: ResponseRequisitanteType) => void;
}

export function SelecionarRequisitanteModal({ open, onOpenChange, onSelectRequisitante }: SelecionarRequisitanteModalProps) {
  const { requisitantes, isLoading } = useRequisitanteManager(true);
  const [busca, setBusca] = useState('');
  const [selecionado, setSelecionado] = useState<ResponseRequisitanteType | null>(null);

  const requisitantesFiltrados = useMemo(() =>
    requisitantes.filter(p => p.reqNome.toLowerCase().includes(busca.toLowerCase())),
    [requisitantes, busca]
  );

  const handleConfirmarSelecao = () => {
    if (!selecionado) return;
    onSelectRequisitante({
      facNome: selecionado.facNome,
      reqId: selecionado.reqId,
      reqNome: selecionado.reqNome,
      facRequisitanteId: selecionado.facRequisitanteId,
      facSigla: selecionado.facSigla
    });
    // Reseta o estado local e fecha o modal
    setSelecionado(null);
    setBusca('');
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Buscar requisitantes</DialogTitle></DialogHeader>
        <div className="space-y-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <Input type="text" placeholder="Buscar requisitante..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} />
          </div>
          <div className="border rounded-md max-h-[300px] overflow-y-auto">
            <table className="w-full">
              <thead className="bg-muted sticky top-0">
                <tr>
                  <th className="text-left py-2 px-4">Código</th>
                  <th className="text-left py-2 px-4">Requisitante</th>
                  <th className="text-left py-2 px-4">Faculdade</th>
                  <th className="text-left py-2 px-4"></th>
                </tr>
              </thead>
              <tbody>
                {requisitantesFiltrados.map((requisitante, index) => (
                  <tr
                    key={index}
                    className={`border-t hover:bg-muted/50 cursor-pointer ${selecionado?.reqId === requisitante.reqId ? "bg-muted/50" : ""}`}
                    onClick={() => setSelecionado(requisitante)}
                  >
                    <td className="py-2 px-4">{requisitante.reqId}</td>
                    <td className="py-2 px-4">{requisitante.reqNome}</td>
                    <td className="py-2 px-4">
                      {requisitante.facNome}
                    </td>
                    <td className="py-2 px-4">
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation()
                          setSelecionado(requisitante)
                          handleConfirmarSelecao()
                        }}
                      >
                        Selecionar
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
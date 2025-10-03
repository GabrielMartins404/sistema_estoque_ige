// src/features/movimentacoes/components/SelecionarProdutoModal.tsx

import { useState, useMemo } from 'react';
import { useMovimentacaoManager } from '@/hooks/movimentacao/useMovimentacao'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from '@/components/ui/label';
import { Search, ArrowRight } from 'lucide-react';
import { RequestProdutoType, ResponseProdutoType } from '@/hooks/produto/types';
import { RequestProdutoMovimentacaoType, ResponseProdutoMovimentacaoType } from '@/hooks/movimentacao/types';
import { useProdutoManager } from '@/hooks/produto/useProduto';

interface SelecionarProdutoModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSelectProduto: (item: ResponseProdutoMovimentacaoType) => void;
}

export function SelecionarProdutoModal({ open, onOpenChange, onSelectProduto }: SelecionarProdutoModalProps) {
  const { produtos, isLoading } = useProdutoManager(true);
  const [busca, setBusca] = useState('');
  const [selecionado, setSelecionado] = useState<ResponseProdutoType | null>(null);
  const [quantidade, setQuantidade] = useState(1);

  const produtosFiltrados = useMemo(() =>
    produtos.filter(p => p.proNome.toLowerCase().includes(busca.toLowerCase())),
    [produtos, busca]
  );

  const handleConfirmarSelecao = () => {
    if (!selecionado || quantidade <= 0) return;
    onSelectProduto({
      produto: selecionado,
      proMovQtdProduto: quantidade,
      proMovCustoProduto: selecionado.proCusto,
    });
    // Reseta o estado local e fecha o modal
    setSelecionado(null);
    setQuantidade(1);
    setBusca('');
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Adicionar Produto</DialogTitle></DialogHeader>
        <div className="space-y-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <Input type="text" placeholder="Buscar produto..." className="pl-10" value={busca} onChange={(e) => setBusca(e.target.value)} />
          </div>
          <div className="border rounded-md max-h-[300px] overflow-y-auto">
            <table className="w-full">
              <thead className="bg-muted sticky top-0">
                <tr>
                  <th className="text-left py-2 px-4">Código</th>
                  <th className="text-left py-2 px-4">Produto</th>
                  <th className="text-left py-2 px-4">Estoque</th>
                  <th className="text-left py-2 px-4"></th>
                </tr>
              </thead>
              <tbody>
                {produtosFiltrados.map((produto, index) => (
                  <tr
                    key={index}
                    className={`border-t hover:bg-muted/50 cursor-pointer ${selecionado?.proId === produto.proId ? "bg-muted/50" : ""}`}
                    onClick={() => setSelecionado(produto)}
                  >
                    <td className="py-2 px-4">{produto.proSipac}</td>
                    <td className="py-2 px-4">{produto.proNome}</td>
                    <td className="py-2 px-4">
                      {produto.proQtd} {produto.proUnNome}
                    </td>
                    <td className="py-2 px-4">
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation()
                          setSelecionado(produto)
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
          {selecionado && (
            <div className="border rounded-md p-4 bg-muted/20 space-y-4">
              <div className='flex justify-between items-center'>
                <h4 className="font-medium">{selecionado.proNome}</h4>
                <div className="flex items-center gap-2">
                  <Label htmlFor="quantidade">Quantidade:</Label>
                  <Input id="quantidade" type="number" min="1" value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} className="w-20" />
                </div>
              </div>
              <div className="flex justify-end">
                <Button onClick={handleConfirmarSelecao} disabled={quantidade <= 0}>
                  Adicionar <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </div>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}
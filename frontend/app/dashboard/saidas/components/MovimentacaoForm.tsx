// src/features/movimentacoes/components/MovimentacaoForm.tsx

import { useForm, useFieldArray } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useState } from 'react';
import { SelecionarProdutoModal } from './SelecionarProdutoModal';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Label } from "@/components/ui/label"
import type { RequestProdutoMovimentacaoType, ResponseProdutoMovimentacaoType } from '@/hooks/movimentacao/types';
import { Plus, Trash2, User } from 'lucide-react';
import { ResponseProdutoType } from '@/hooks/produto/types';
import { ResponseRequisitanteType } from '@/hooks/requisitante/types';
import { SelecionarRequisitanteModal } from './SelecionarRequisitanteModal';

//  Schema de validação com Zod, incluindo o array de produtos
const produtoSchema = z.object({
  proId: z.number(),
  proNome: z.string(),
  proSipac: z.string().optional(),
  proUnNome: z.string(),
  proCusto: z.number(),
});

const produtoMovSchema = z.object({
  produto: produtoSchema, // Para simplicidade, mas pode ser um schema de produto mais estrito
  qtdProduto: z.number().positive(),
  custoProduto: z.number(),
});

const movimentacaoSchema = z.object({
  movRequisitanteId: z.number().positive({ message: "Selecione um requisitante." }),
  movNf: z.string().optional(),
  movNumRequisicao: z.string().optional(),
  movObservacao: z.string().optional(),
  produtosMov: z.array(produtoMovSchema).min(1, "Adicione pelo menos um produto."),
});

export type MovimentacaoFormValues = z.infer<typeof movimentacaoSchema>;

interface MovimentacaoFormProps {
  onSubmit: (values: MovimentacaoFormValues) => void;
  onCancel: () => void;
  isSubmitting: boolean;
}

export function MovimentacaoForm({ onSubmit, onCancel, isSubmitting }: MovimentacaoFormProps) {
  const [produtoModalOpen, setProdutoModalOpen] = useState(false);
  const [requisitanteModalOpen, setRequisitanteModalOpen] = useState(false);
  const [requisitanteDisplay, setRequisitanteDisplay] = useState<ResponseRequisitanteType | null>(null);
  const form = useForm<MovimentacaoFormValues>({
    resolver: zodResolver(movimentacaoSchema),
    defaultValues: {
      movRequisitanteId: undefined,
      movNf: "",
      movObservacao: "",
      movNumRequisicao: "",
      produtosMov: [],
    },
  });

  // `useFieldArray` para gerenciar a lista de produtos!
  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "produtosMov",
  });

   const handleSelectRequisitante = (req: ResponseRequisitanteType) => {
      // 3. Atualiza o valor do formulário programaticamente
      form.setValue('movRequisitanteId', req.reqId, { shouldValidate: true });
      // E atualiza o estado de exibição
      setRequisitanteDisplay(req);
      setRequisitanteModalOpen(false);
    };

  const handleSelectProduto = (item: ResponseProdutoMovimentacaoType) => {
    // Verifica se o produto já existe para somar a quantidade
    const existingIndex = fields.findIndex(field => {field.produto.proId === item.produto.proId});
    if (existingIndex > -1) {
      const currentItem = fields[existingIndex];
      // `update` é uma função do `useFieldArray`, mas somar e `remove`/`append` também funciona
      const updatedItem = { ...currentItem, qtdProdutoResponse: (currentItem.qtdProduto || 0) + (item.proMovQtdProduto || 0) };
      remove(existingIndex);
      append(updatedItem);
    } else {
      append({
        produto: item.produto,
        qtdProduto: Number(item.proMovQtdProduto),
        custoProduto: Number(item.produto.proCusto),
      });
    }
  };

  return (
    <>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <div className="space-y-2">
           <FormField
            control={form.control}
            name="movRequisitanteId" //  Conecta este bloco ao campo 'movRequisitanteId'
            render={({ field }) => (
              <FormItem>
                <FormLabel>Requisitante</FormLabel>
                <FormControl>
                  <Button
                    type="button"
                    variant="outline"
                    className="w-full justify-start text-left font-normal"
                    onClick={() => setRequisitanteModalOpen(true)}
                  >
                    <User className="mr-2 h-4 w-4" />
                    {requisitanteDisplay ? (
                      <span>
                        {requisitanteDisplay.reqNome} - {requisitanteDisplay.facSigla == null ? "Sem faculdade" : requisitanteDisplay.facSigla}
                      </span>
                    ) : (
                      <span>Selecionar requisitante</span>
                    )}
                  </Button>
                </FormControl>
                <FormMessage /> {/* Agora ele sabe que deve exibir o erro de 'movRequisitanteId' */}
              </FormItem>
            )}
          />
          </div>
          
          <div className="grid grid-cols-1 gap-4">
            <FormField control={form.control} name="movNumRequisicao" render={({ field }) => (
                <FormItem><FormLabel>Número da Requisição</FormLabel><FormControl><Input {...field} /></FormControl><FormMessage /></FormItem>
            )} />
          </div>

          <div className="space-y-2">
            <div className="flex justify-between items-center">
              <FormLabel>Produtos</FormLabel>
              <Button type="button" variant="outline" size="sm" onClick={() => setProdutoModalOpen(true)}>
                <Plus className="h-4 w-4 mr-1" /> Adicionar Produto
              </Button>
            </div>
            {/*3. Tabela reativa renderizada a partir do `useFieldArray` */}
            {fields.length > 0 ? (
              <div className="border rounded-md">
                <table className="w-full"> 
                  <thead className="bg-muted">
                    <tr className="bg-muted sticky top-0">
                      <th className="text-left py-2 px-4">Código</th>
                      <th className="text-left py-2 px-4">Produto</th>
                      <th className="text-left py-2 px-4">Quantidade</th>
                      <th className="text-left py-2 px-4">Unidade</th>
                      <th className="text-left py-2 px-4">Excluir</th>
                    </tr>
                  </thead>
                  <tbody>
                    {fields.map((item, index) => {
                      return (
                      <tr key={item.id} className="border-t hover:bg-muted/50 cursor-pointer">
                        <td className="text-left py-2 px-4">{item.produto.proSipac}</td>
                        <td className="text-left py-2 px-4">{item.produto.proNome}</td>
                        <td className="text-left py-2 px-4">{item.qtdProduto}</td>
                        <td className="text-left py-2 px-4">{item.produto.proUnNome}</td>
                        <td className="text-left py-2 px-4"><Button variant="ghost" size="icon" onClick={() => remove(index)}><Trash2/></Button></td>
                      </tr>
                    )})}
                  </tbody>
                </table>
              </div>
            ) : (  <FormMessage>{form.formState.errors.produtosMov?.message}</FormMessage> )}
           
          </div>
          
          <div className="flex justify-end gap-2">
            <Button type="button" variant="outline" onClick={onCancel}>Cancelar</Button>
            <Button type="submit" disabled={isSubmitting} className="bg-[#1e3a8a]">{isSubmitting ? "Finalizando..." : "Finalizar Movimentação"}</Button>
          </div>
        </form>
      </Form>

      {/* 4. O modal de seleção é controlado e usado por este formulário */}
      <SelecionarProdutoModal
        open={produtoModalOpen}
        onOpenChange={setProdutoModalOpen}
        onSelectProduto={handleSelectProduto}
      />

      <SelecionarRequisitanteModal
        open={requisitanteModalOpen}
        onOpenChange={setRequisitanteModalOpen}
        onSelectRequisitante={handleSelectRequisitante}
      />
    </>
  );
}
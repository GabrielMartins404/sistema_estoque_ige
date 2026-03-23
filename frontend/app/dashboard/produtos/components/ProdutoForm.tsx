import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useEffect } from 'react';
import { RequestFaculdadeType } from '@/hooks/faculdade/types';
import { RequestProdutoType } from '@/hooks/produto/types';
import { ResponseUnidadeType } from '@/hooks/unidade/types';
import { ResponseCategoriaType } from '@/hooks/categoria/types';
//import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@radix-ui/react-select';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';

//Definir os esquemas de validação com Zod
export const produtoSchema = z.object({
    proNome: z.string().min(3, "O nome do produto é obrigatório.").max(255, "O nome do produto deve ter no máximo 255 caracteres."),
    proSipac: z.string().min(3, "O código SIPAC é obrigatório.").max(100, "O código SIPAC deve ter no máximo 100 caracteres."),
    proDescricao: z.string().optional(),
    // ✨ z.coerce.number() converte a string do input para número automaticamente!
    proCusto: z.coerce.number({ invalid_type_error: "Deve ser um número" }).min(0, "O custo não pode ser negativo."),
    proEstoqueMin: z.coerce.number({ invalid_type_error: "Deve ser um número" }).int("Deve ser um número inteiro.").min(0),
    proUnId: z.coerce.number().positive({ message: "Selecione uma unidade." }),
    proCategoriaId: z.coerce.number().positive({ message: "Selecione uma categoria." }).optional(),
})

//Extrair o tipo inferido do esquema para uso posterior
type ProdutoFormValues = z.infer<typeof produtoSchema>;

interface ProdutoFormProps {
    produtoAtual?: RequestProdutoType | null;
    unidadeList: ResponseUnidadeType[];
    categoriaList: ResponseCategoriaType[];
    onSubmit: (data: ProdutoFormValues) => void;
    onCancel: () => void;
    isSubmitting: boolean;
}

export function ProdutoForm({ produtoAtual, unidadeList, categoriaList, onSubmit, onCancel, isSubmitting }: ProdutoFormProps) {
    // Inicializar o formulário com react-hook-form e zodResolver para validação
    const form = useForm<ProdutoFormValues>({
        resolver: zodResolver(produtoSchema),
        defaultValues: {
            proNome: produtoAtual?.proNome || "",
            proDescricao: produtoAtual?.proDescricao || "",
            proSipac: produtoAtual?.proSipac || "",
            proCusto: produtoAtual?.proCusto || 0,
            proEstoqueMin: produtoAtual?.proEstoqueMin || 0,
            proUnId: produtoAtual?.proUnId || 0,
            proCategoriaId: produtoAtual?.proCategoriaId || 0,
        },
    });
    //Essas const são importantes para o funcionamento do select, pois as renderizam corretamente
    const proUnId = form.watch("proUnId");
    const proCategoriaId = form.watch("proCategoriaId");

    useEffect(() => {
        form.reset({
            proNome: produtoAtual?.proNome || "",
            proDescricao: produtoAtual?.proDescricao || "",
            proSipac: produtoAtual?.proSipac || "",
            proCusto: produtoAtual?.proCusto || 0,
            proEstoqueMin: produtoAtual?.proEstoqueMin || 0,
            proUnId: produtoAtual?.proUnId || 0,
            proCategoriaId: produtoAtual?.proCategoriaId || 0,
        });
    }, [produtoAtual, form]);

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                {/* Unidade e Categoria */}
                <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="proUnId"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Unidade</FormLabel>
                                    <Select
                                        value={field.value ? String(field.value) : ""}
                                        onValueChange={(val) => field.onChange(Number(val))}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue placeholder="Selecione" />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {unidadeList.map((unidade) => (
                                                <SelectItem key={unidade.unId} value={String(unidade.unId)}>
                                                    {unidade.unNome} - {unidade.unSigla}
                                                </SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="proCategoriaId"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Categoria</FormLabel>
                                    <Select
                                        value={field.value ? String(field.value) : ""}
                                        onValueChange={(val) => field.onChange(Number(val))}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue placeholder="Selecione" />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {categoriaList.map((categoria) => (
                                                <SelectItem key={categoria.catProId} value={String(categoria.catProId)}>
                                                    {categoria.catProNome}
                                                </SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                </div>

                {/* Nome do Produto */}
                <FormField control={form.control} name="proNome" render={({ field }) => (
                    <FormItem><FormLabel>Nome do Produto</FormLabel><FormControl><Input {...field} /></FormControl><FormMessage /></FormItem>
                )} />
                {/* Código SIPAC */}
                <FormField control={form.control} name="proSipac" render={({ field }) => (
                    <FormItem><FormLabel>Código SIPAC</FormLabel><FormControl><Input {...field} /></FormControl><FormMessage /></FormItem>
                )} />
                {/* Custo e Estoque Mínimo */}
                <div className="grid grid-cols-2 gap-4">
                    <FormField control={form.control} name="proCusto" render={({ field }) => (
                        <FormItem><FormLabel>Valor de custo</FormLabel><FormControl><Input type="number" step="0.01" {...field} /></FormControl><FormMessage /></FormItem>
                    )} />
                    <FormField control={form.control} name="proEstoqueMin" render={({ field }) => (
                        <FormItem><FormLabel>Estoque Mínimo</FormLabel><FormControl><Input type="number" {...field} /></FormControl><FormMessage /></FormItem>
                    )} />
                </div>
                {/* Descrição */}
                <FormField control={form.control} name="proDescricao" render={({ field }) => (
                    <FormItem><FormLabel>Descrição</FormLabel><FormControl><Textarea {...field} /></FormControl><FormMessage /></FormItem>
                )} />
                {/* Botões */}
                <div className="flex justify-end gap-2">
                    <Button type="button" variant="outline" onClick={onCancel}>Cancelar</Button>
                    <Button type="submit" disabled={isSubmitting} className="bg-[#1e3a8a]">{isSubmitting ? "Salvando..." : "Salvar"}</Button>
                </div>
            </form>
        </Form>
    );
}
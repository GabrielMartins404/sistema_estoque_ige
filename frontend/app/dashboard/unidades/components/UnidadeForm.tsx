import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useEffect } from 'react';
import { RequestUnidadeType } from '@/hooks/unidade/types';

//Definir os esquemas de validação com Zod
const unidadeSchema = z.object({
    unNome: z.string().min(2, 'Nome da unidade ter no mínimo 2 caracteres').max(255, 'Nome da unidade deve ter no máximo 255 caracteres'),
    unSigla: z.string().min(2, 'Sigla da unidade ter no mínimo 2 caracteres').max(50, 'Sigla da unidade deve ter no máximo 50 caracteres'),

})

//Extrair o tipo inferido do esquema para uso posterior
type UnidadeFormValues = z.infer<typeof unidadeSchema>;
type UnidadeFormProps = {
    unidadeAtual?: RequestUnidadeType | null; 
    onSubmit: (data: UnidadeFormValues) => void; 
    onCancel: () => void; 
    isSubmitting: boolean; 
}

export function UnidadeForm({ unidadeAtual, onSubmit, onCancel, isSubmitting }: UnidadeFormProps) {
    // Inicializar o formulário com react-hook-form e zodResolver para validação
    const form = useForm<UnidadeFormValues>({
        resolver: zodResolver(unidadeSchema),
        defaultValues: {
            unNome: unidadeAtual?.unNome || "",
            unSigla: unidadeAtual?.unSigla || "",
        },
    });

    useEffect(() => {
        // Atualizar os valores do formulário quando a categoriaAtual mudar
        form.reset({
            unNome: unidadeAtual?.unNome || "",
            unSigla: unidadeAtual?.unSigla || "",
        });
    }, [unidadeAtual, form]);

    return(
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="unNome"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>Descrição da unidade</FormLabel>
                        <FormControl>
                            <Input placeholder="Ex: Unidade" {...field} />
                        </FormControl>
                        <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="unSigla"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>Sigla da unidade</FormLabel>
                        <FormControl>
                            <Input placeholder="Ex: UN" {...field} />
                        </FormControl>
                        <FormMessage />
                        </FormItem>
                    )}
                />
                <div className="flex justify-end gap-2">
                <Button type="button" variant="outline" onClick={onCancel}>
                    Cancelar
                </Button>
                <Button type="submit" disabled={isSubmitting} className="bg-[#1e3a8a]">
                    {isSubmitting ? "Salvando..." : "Salvar"}
                </Button>
                </div>
            </form>
        </Form>
    );
}
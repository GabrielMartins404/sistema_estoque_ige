import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useEffect } from 'react';
import { RequestFaculdadeType } from '@/hooks/faculdade/types';

//Definir os esquemas de validação com Zod
const faculdadeSchema = z.object({
    facNome: z.string().min(2, 'Nome da faculdade ter no mínimo 2 caracteres').max(255, 'Nome da faculdade deve ter no máximo 255 caracteres'),
    facSigla: z.string().min(2, 'Sigla da faculdade ter no mínimo 2 caracteres').max(50, 'Sigla da faculdade deve ter no máximo 50 caracteres'),

})

//Extrair o tipo inferido do esquema para uso posterior
type FaculdadeFormValues = z.infer<typeof faculdadeSchema>;

interface FaculdadeFormProps {
    faculdadeAtual?: RequestFaculdadeType | null; 
    onSubmit: (data: FaculdadeFormValues) => void; 
    onCancel: () => void; 
    isSubmitting: boolean; 
}

export function FaculdadeForm({ faculdadeAtual, onSubmit, onCancel, isSubmitting }: FaculdadeFormProps) {
    // Inicializar o formulário com react-hook-form e zodResolver para validação
    const form = useForm<FaculdadeFormValues>({
        resolver: zodResolver(faculdadeSchema),
        defaultValues: {
            facNome: faculdadeAtual?.facNome || "",
            facSigla: faculdadeAtual?.facSigla || "",
        },
    });

    useEffect(() => {
        // Atualizar os valores do formulário quando a categoriaAtual mudar
        form.reset({
            facNome: faculdadeAtual?.facNome || "",
            facSigla: faculdadeAtual?.facSigla || "",
        });
    }, [faculdadeAtual, form]);

    return(
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="facNome"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>Nome da faculdade</FormLabel>
                        <FormControl>
                            <Input placeholder="Ex: Engenharia da Computação" {...field} />
                        </FormControl>
                        <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="facSigla"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>Sigla da faculdade</FormLabel>
                        <FormControl>
                            <Input placeholder="Ex: FEC" {...field} />
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
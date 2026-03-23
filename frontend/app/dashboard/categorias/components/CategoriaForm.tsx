import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useEffect } from 'react';
import { RequestCategoriaType } from '@/hooks/categoria/types';

//Definir os esquemas de validação com Zod
const categoriaSchema = z.object({
    catProNome: z.string().min(2, 'Nome deve ter no mínimo 2 caracteres').max(255, 'Nome deve ter no máximo 255 caracteres'),
})

//Extrair o tipo inferido do esquema para uso posterior
type CategoriaFormValues = z.infer<typeof categoriaSchema>;

interface CategoriaFormProps {
    categoriaAtual?: RequestCategoriaType | null; // Categoria atual para edição, se houver
    onSubmit: (data: CategoriaFormValues) => void; // Função de callback para submissão do formulário
    onCancel: () => void; // Função de callback para cancelar a operação
    isSubmitting: boolean; // Indicador de estado de submissão
}

export function CategoriaForm({ categoriaAtual, onSubmit, onCancel, isSubmitting }: CategoriaFormProps) {
    // Inicializar o formulário com react-hook-form e zodResolver para validação
    const form = useForm<CategoriaFormValues>({
        resolver: zodResolver(categoriaSchema),
        defaultValues: {
            catProNome:  categoriaAtual?.catProNome || "",
        },
    });

    useEffect(() => {
        // Atualizar os valores do formulário quando a categoriaAtual mudar
        form.reset({
            catProNome: categoriaAtual?.catProNome || "",
        });
    }, [categoriaAtual, form]);

    return(
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                control={form.control}
                name="catProNome"
                render={({ field }) => (
                    <FormItem>
                    <FormLabel>Nome da categoria</FormLabel>
                    <FormControl>
                        <Input placeholder="Ex: Equipamentos de TI" {...field} />
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
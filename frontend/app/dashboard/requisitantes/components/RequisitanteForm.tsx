import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useEffect } from 'react';
import { RequestFaculdadeType, ResponseFaculdadeType } from '@/hooks/faculdade/types';
import { RequestProdutoType } from '@/hooks/produto/types';
import { ResponseUnidadeType } from '@/hooks/unidade/types';
import { ResponseCategoriaType } from '@/hooks/categoria/types';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from '@/components/ui/textarea';
import { RequestRequisitanteType } from '@/hooks/requisitante/types';

//Definir os esquemas de validação com Zod
const requisitanteSchema = z.object({
    reqNome: z.string().min(3, "O nome do requisitante é obrigatório.").max(255, "O nome do requisitante deve ter no máximo 255 caracteres."),
    facRequisitanteId: z.coerce.number().int().nonnegative({ message: "Selecione uma faculdade." }).optional(),
})

//Extrair o tipo inferido do esquema para uso posterior
type RequisitanteFormValues = z.infer<typeof requisitanteSchema>;

interface RequisitanteFormProps {
    requisitanteAtual?: RequestRequisitanteType | null;
    faculdadeList: ResponseFaculdadeType[];
    onSubmit: (data: RequisitanteFormValues) => void; 
    onCancel: () => void; 
    isSubmitting: boolean; 
}

export function RequisitanteForm({ requisitanteAtual, faculdadeList, onSubmit, onCancel, isSubmitting }: RequisitanteFormProps) {
    // Inicializar o formulário com react-hook-form e zodResolver para validação
    const form = useForm<RequisitanteFormValues>({
        resolver: zodResolver(requisitanteSchema),
        defaultValues: {
            reqNome: requisitanteAtual?.reqNome || "",
            facRequisitanteId: requisitanteAtual?.facRequisitanteId || 0,
        },
    });

    useEffect(() => {
        // Atualizar os valores do formulário quando a categoriaAtual mudar
        form.reset({
            reqNome: requisitanteAtual?.reqNome || "",
            facRequisitanteId: requisitanteAtual?.facRequisitanteId || 0,
        });
    }, [requisitanteAtual, form]);

    return(
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                {/* Unidade e Categoria */}
                <div className="grid grid-cols-2 gap-4">
                <FormField control={form.control} name="reqNome" render={({ field }) => (
                    <FormItem>
                    <FormLabel>Nome</FormLabel>
                    <Input {...field} />
                    <FormMessage />
                    </FormItem>
                )} />
                <FormField control={form.control} name="facRequisitanteId" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Faculdade</FormLabel>
                        <Select
                            value={field.value ? String(field.value) : ""}
                            onValueChange={(val) => field.onChange(Number(val))}
                        >
                            <FormControl>
                                <SelectTrigger>
                                    <SelectValue placeholder="Selecione uma faculdade" />
                                </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                                <SelectItem key={0} value="0">
                                    Sem faculdade
                                </SelectItem>
                                {faculdadeList.map((faculdade) => (
                                    <SelectItem key={faculdade.facId} value={String(faculdade.facId)}>
                                        {faculdade.facNome} - {faculdade.facSigla}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                        <FormMessage />
                    </FormItem>
                )} />
                </div>
                <div className="flex justify-end space-x-2">
                    <Button type="button" onClick={onCancel} variant="outline">Cancelar</Button>
                    <Button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? "Salvando..." : "Salvar"}
                    </Button>
                </div>
            </form>
        </Form>
    );
}

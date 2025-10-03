
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import type { RequestCadastroUsuarioType, RequestAtualizaUsuarioType } from '@/hooks/usuario/types';
import { useEffect } from 'react';
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input"

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

// Schema Zod com validação de confirmação de senha
const usuarioSchema = z.object({
  usuNome: z.string().min(3, "O nome é obrigatório."),
  usuLogin: z.string().email("Formato de e-mail inválido.").optional(),
  usuPerfil: z.number().min(0, "Selecione um perfil."),
  usuSenha: z.string().min(6, "A senha deve ter no mínimo 6 caracteres.").optional(),
  confirmaSenha: z.string().optional(),
}).refine(data => {
  // A validação da senha só é necessária se o campo senha for preenchido
  if (data.usuSenha) {
    return data.usuSenha === data.confirmaSenha;
  }
  return true;
}, {
  message: "As senhas não conferem.",
  path: ["confirmaSenha"], // O erro aparecerá no campo de confirmação
});

type UsuarioFormValues = z.infer<typeof usuarioSchema>;

interface UsuarioFormProps {
  usuarioAtual?: RequestCadastroUsuarioType | RequestAtualizaUsuarioType | null;
  onSubmit: (values: UsuarioFormValues) => void;
  onCancel: () => void;
  isSubmitting: boolean;
}

export function UsuarioForm({ usuarioAtual, onSubmit, onCancel, isSubmitting }: UsuarioFormProps) {
  const isEditMode = !!usuarioAtual;
  const form = useForm<UsuarioFormValues>({
    resolver: zodResolver(usuarioSchema),
    // Define valores padrão para TODOS os campos do schema.
    defaultValues: {
      usuNome: "",
      usuLogin: "",
      usuPerfil: undefined, // undefined é ok para selects com placeholder
      usuSenha: "",
      confirmaSenha: "",
    },
  });

 useEffect(() => {
    if (usuarioAtual) {
      // Quando um `usuarioAtual` é fornecido, resetamos o formulário com seus dados.
      form.reset({
        usuNome: usuarioAtual.usuNome,
        usuPerfil: usuarioAtual.usuPerfil
      });
    }
  }, [usuarioAtual, form]);
  

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        {/* Campo Nome */}
        <FormField control={form.control} name="usuNome" render={({ field }) => (
            <FormItem>
                <FormLabel>Nome</FormLabel>
                <FormControl>
                    <Input {...field} />
                </FormControl>
                <FormMessage />
            </FormItem>
        )} />
        {/* Campo Perfil (Select) */}
        <FormField control={form.control} name="usuPerfil" render={({ field }) => (
            <FormItem>
                <FormLabel>Perfil</FormLabel>
                <Select
                    value={field.value !== undefined && field.value !== null ? String(field.value) : ""}
                    onValueChange={(val) => field.onChange(Number(val))}
                >
                    <FormControl>
                        <SelectTrigger>
                            <SelectValue placeholder="Selecione" />
                        </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem key={0} value={String(0)}>Administrador</SelectItem>
                      <SelectItem key={1} value={String(1)}>Almoxarifado</SelectItem>
                    </SelectContent>
                </Select>
                <FormMessage />
            </FormItem>
        )} />

        {/* Campos que só aparecem no modo de criação */}
        {!isEditMode && (
          <>
            <FormField control={form.control} name="usuLogin" render={({ field }) => (
                <FormItem>
                    <FormLabel>Login</FormLabel>
                    <FormControl>
                        <Input {...field} />
                    </FormControl>
                    <FormMessage />
                </FormItem>
            )} />
            <FormField control={form.control} name="usuSenha" render={({ field }) => (
                <FormItem>
                    <FormLabel>Senha</FormLabel>
                    <FormControl>
                        <Input {...field} />
                    </FormControl>
                    <FormMessage />
                </FormItem>
            )} />
            <FormField control={form.control} name="confirmaSenha" render={({ field }) => (
                <FormItem>
                    <FormLabel>Confirmar Senha</FormLabel>
                    <FormControl>
                        <Input {...field} />
                    </FormControl>
                    <FormMessage />
                </FormItem>
            )} />
          </>
        )}
        
        <div className="flex justify-end gap-2">
          <Button type="button" variant="outline" onClick={onCancel}>Cancelar</Button>
          <Button type="submit" disabled={isSubmitting}>{isSubmitting ? "Salvando..." : "Salvar"}</Button>
        </div>
      </form>
    </Form>
  );
}
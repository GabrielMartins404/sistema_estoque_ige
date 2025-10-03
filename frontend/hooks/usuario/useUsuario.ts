import { useState } from "react";
import {
    useGetUsuarios,
    useCreateUsuario,
    useUpdateUsuario,
    useInactivateUsuario,
    useUpdatePasswordUsuario,
    useResetPasswordUsuario
} from './api'

export function useUsuarioManager(status: boolean){

    const {data: usuarios = [], isLoading, isError, error} = useGetUsuarios(status)
    const {mutate: cadastrarUsuario, isPending: isCreating} = useCreateUsuario()
    const {mutate: atualizarUsuario, isPending: isUpdating} = useUpdateUsuario()
    const {mutate: atualizarSenhaUsuario, isPending: isUpdatingPassword} = useUpdatePasswordUsuario()
    const {mutate: resetarSenhaUsuario, isPending: isResettingPassword} = useResetPasswordUsuario()
    const {mutate: inativarUsuario, isPending: isDeleting} = useInactivateUsuario()

    const loading = isCreating || isUpdating || isUpdatingPassword || isDeleting || isResettingPassword
    
    return {
        usuarios,
        status,
        isLoading,
        loading,
        isError,
        error,
        isDeleting,

        //Ações
        cadastrarUsuario,
        atualizarUsuario,
        atualizarSenhaUsuario,
        inativarUsuario,
        resetarSenhaUsuario
    }
}
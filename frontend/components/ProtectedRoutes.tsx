'use client'

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { useAuth } from "@/contexts/UsuarioContext"
import { useError } from "@/contexts/NotificationContext"
import Loading from "./Loading"

interface ProtectedRoutesProps {
    children: React.ReactNode
}

export default function ProtectedRoute({children}: ProtectedRoutesProps){
    const erro = useError()
    const { usuario, isAutenticado, loading } = useAuth();
    const router = useRouter()
    useEffect(() => {
    // Se não estiver carregando e o usuário não for autenticado ou não tiver a permissão...
    if (!loading && !isAutenticado) {
        // Redireciona para o dashboard ou página de login
        erro.addNotification("Token inválido ou expirado")
        erro.addNotification("Faça login novamente")
        router.push('/');
        }
    }, [loading, isAutenticado, usuario, router]);

    // Enquanto o status de autenticação está sendo verificado, exibe um loading.
    if (loading) {
        return <Loading />; // Não renderiza a página protegida ainda
    }

    // Se o usuário tem a permissão correta, renderiza a página filha.
    // Se não tem, renderiza `null` enquanto o `useEffect` faz o redirecionamento.
    if (isAutenticado) {
        return <>{children}</>;
    }

    if(loading || !isAutenticado){
        return <Loading/>
    }
    return <>{children} </>
}
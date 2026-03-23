// src/components/ProtectedRoutes.tsx (EXEMPLO REATORADO)

"use client";

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/UsuarioContext';
import Loading from '@/components/Loading'; // Um componente de loading de página inteira

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRole: string; // Passamos a permissão necessária como prop
}

export default function ProtectedRoute({ children, allowedRole }: ProtectedRouteProps) {
  const { usuario, isAutenticado, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    // Se não estiver carregando e o usuário não for autenticado ou não tiver a permissão...
    if (!loading && (!isAutenticado || usuario?.usuPerfil !== allowedRole)) {
      // Redireciona para o dashboard ou página de login
      router.push('/dashboard');
    }
  }, [loading, isAutenticado, usuario, allowedRole, router]);

  // Enquanto o status de autenticação está sendo verificado, exibe um loading.
  if (loading) {
    return <Loading />; // Não renderiza a página protegida ainda
  }

  // Se o usuário tem a permissão correta, renderiza a página filha.
  // Se não tem, renderiza `null` enquanto o `useEffect` faz o redirecionamento.
  if (isAutenticado && usuario?.usuPerfil === allowedRole) {
    return <>{children}</>;
  }

  // Retorna nulo para evitar o "flash" do conteúdo antes do redirecionamento
  return null;
}
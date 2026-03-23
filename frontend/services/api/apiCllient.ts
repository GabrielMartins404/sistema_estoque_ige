// Em src/services/apiClient.ts (SEU ARQUIVO ATUALIZADO)

import axios from 'axios';
import { ApiError } from './errorsApi'; // Importa nossa classe de erro customizada

const BACKEND_HOST = process.env.NEXT_PUBLIC_BACKEND_HOST;

if (!BACKEND_HOST) {
  throw new Error("Variável de ambiente Backend não configurada.");
}

const apiClient = axios.create({
  baseURL: BACKEND_HOST,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor de requisição (permanece o mesmo, está ótimo)
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('Authorization');
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
});

// INTERCEPTOR DE RESPOSTA REATORADO
apiClient.interceptors.response.use(
  // 1. Para respostas de SUCESSO, não fazemos nada, apenas repassamos.
  (response) => response,

  // 2. Para respostas de ERRO, vamos PADRONIZAR o objeto de erro.
  (error) => {
    // Verifica se o erro é do Axios e tem uma resposta do servidor
    if (axios.isAxiosError(error) && error.response) {
      const status = error.response.status;
      const data = error.response.data;
      let errorMessage = data?.mensagem || error.message || "Erro inesperado.";
      
      // Caso específico para erro 422 (Validation Error)
      if (status === 422 && data?.errors) {
        // Concatena todas as mensagens de erro de validação em uma única string
        errorMessage = data.errors
          .map((err: { campo: string; mensagem: string }) => `${err.campo}: ${err.mensagem}`)
          .join(' ');
      }

      // Criamos uma instância do nosso erro padronizado
      const apiError = new ApiError(errorMessage, status, data?.errors);

      // O PONTO CRUCIAL: Rejeitamos a Promise com nosso novo objeto de erro padronizado.
      return Promise.reject(apiError);
    }
    
    // Para outros tipos de erro (ex: rede), rejeitamos o erro original.
    return Promise.reject(error);
  }
);

export default apiClient;
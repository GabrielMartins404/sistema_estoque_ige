'use client'

import { useError } from "@/contexts/NotificationContext";
import '../styles/erroNotification.css'

/**
 * Componente que exibe notificações de erro globais
 */
const ErrorNotification = () => {
    const { errors, removeError } = useError();
    
    if (errors.length === 0) return null;
    
    return (
      <div className="error-container">
        {errors.map((error) => (
          <div 
            key={error.id} 
            className={`error-message ${error.tipo || 'error'} align-items-center justify-content-center`}
            onClick={() => removeError(error)}
          >
            <div>
              <b>{error.titulo && error.titulo}</b>
              <p>{error.mensagem}</p>
            </div>
            <button 
              onClick={(e) => {
                e.stopPropagation();
                removeError(error);
              }}
            >
              ×
            </button>
          </div>
        ))}
      </div>
    );
  };
  
  export default ErrorNotification;
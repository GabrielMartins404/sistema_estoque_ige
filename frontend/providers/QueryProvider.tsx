"use client"

import { useState, ReactNode } from "react"
import {QueryClient, QueryClientProvider} from '@tanstack/react-query'
import {ReactQueryDevtools} from '@tanstack/react-query-devtools'

export function QueryProvider({children}: {children: ReactNode}) {
    const [queryClient] = useState(() => new QueryClient({
        defaultOptions: {
            queries: {
                staleTime: 1000*60*5 //So haverá refresh de 5 em 5 minutos
            }
        }
    }))

    return(
        <QueryClientProvider client={queryClient}>
            {children}
            {/* Ferramenta de desenvolvimento. Só aparece em ambiente de desenvolvimento. */}
            {process.env.NODE_ENV === 'development' && <ReactQueryDevtools initialIsOpen={false} />}
        </QueryClientProvider>
    )
}

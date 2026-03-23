"use client"
import { useForm } from "react-hook-form";
import { useState, useEffect } from "react"
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import {  FileText, ShoppingCart, Users, BarChart2, LogOut, BookOpen, Clipboard, UserRoundPlus, Box, PackagePlus, Notebook, User, List } from "lucide-react"
import Link from "next/link"
import ErrorNotification from "@/components/ErrorNotification"
import { useAuth } from "@/contexts/UsuarioContext"
import ProtectedRoute from "@/components/ProtectedRoutes"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import Loading from "@/components/Loading"
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input"
import { useUsuarioManager } from "@/hooks/usuario/useUsuario";
import { RequestAlteraSenhaUsuarioType } from "@/hooks/usuario/types/RequestUsuario.type";

import {
  useGetProdutosMaisMovimentados,
  useGetProdutosPorRequisitante,
  useGetQtdMov,
  useGetQtdProdutosAtivos,
  useGetQtdProdutosAbaixoMin,
} from "@/hooks/relatorios/api"

//  Schema de validação com Zod, incluindo o array de produtos
const alterarSenhaSchema = z.object({
  senhaAntiga: z.string().nonempty("A senha antiga não pode ser vazia"),
  novaSenha: z.string().min(6, "A nova senha deve ter no mínimo 6 caracteres.").nonempty("A senha não pode ser vazia"),
  confirmaSenha: z.string().nonempty("A senha não pode ser vazia"),
}).refine(data => {
  // A validação da senha só é necessária se o campo senha for preenchido
  if (data.novaSenha) {
    return data.novaSenha === data.confirmaSenha;
  }
  return true;
}, {
  message: "As senhas não conferem.",
  path: ["confirmaSenha"], // O erro aparecerá no campo de confirmação
});


export type AlterarSenhaFormValues = z.infer<typeof alterarSenhaSchema>;

export default function Dashboard() {
  const { atualizarSenhaUsuario, loading: loadSenha } = useUsuarioManager(true);
  const { logout, usuario, isAutenticado } = useAuth()
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [openDialog, setOpenDialog] = useState<"senha" | null>(null)
  const [senhaAntiga, setSenhaAntiga] = useState("")
  const [novaSenha, setNovaSenha] = useState("")
  const [confirmaSenha, setConfirmaSenha] = useState("")
  const [loading, setLoading] = useState(false)

  const { data: produtosMaisMovimentado = [] } = useGetProdutosMaisMovimentados()
  const { data: produtosPorRequisitante = [] } = useGetProdutosPorRequisitante()
  const { data: qtdMovEntrada = 0 } = useGetQtdMov('E', 'F')
  const { data: qtdMovSaida = 0 } = useGetQtdMov('S', 'F')
  const { data: qtdProdutosAtivos = 0 } = useGetQtdProdutosAtivos()
  const { data: qtdProdutosAbaixoMin = 0, isLoading: isLoadingQtdProdutosAbaixoMin, isError: isErrorQtdProdutosAbaixoMin, error: errorQtdProdutosAbaixoMin } = useGetQtdProdutosAbaixoMin()

  const form = useForm<AlterarSenhaFormValues>({
    resolver: zodResolver(alterarSenhaSchema),
    // Define valores padrão para TODOS os campos do schema.
    defaultValues: {
      senhaAntiga: "",
      novaSenha: "",
      confirmaSenha: ""
    },
  });

  const alterarSenhaUsuario = (id: number | undefined) => {
    if (id == undefined) {
      return
    }
    const dados: RequestAlteraSenhaUsuarioType = {
      usuSenhaNova: form.getValues().novaSenha,
      usuSenhaAntiga: form.getValues().senhaAntiga
    }
    atualizarSenhaUsuario({ id: id, dados: dados })
    if (loadSenha) return <Loading />;
    setOpenDialog(null)
    setSenhaAntiga("")
    setNovaSenha("")
    setConfirmaSenha("")
    form.reset()


  }
  if (loading) return <Loading />;
  return (
    // O sistema está com um comportamento que precisa ser corrigido. Ao fazer login por um curto espaço de tempo, o sistema vem para essa tela e retorna para a tela de login e novamente retorna para essa tela.
    <ProtectedRoute>
      <div className="flex h-screen bg-gray-100">
        {/* Avaliar se esse componente é realmente necessário aqui */}
        <ErrorNotification />
        <aside
          className={`bg-[#1e3a8a] text-white ${sidebarOpen ? "w-64" : "w-20"
            } transition-all duration-300 h-screen relative flex flex-col`}
        >
          <div className="p-4 flex justify-between items-center">
            <h2 className={`font-bold ${sidebarOpen ? "block" : "hidden"}`}>
              Almoxarifado
            </h2>
            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="text-white"
            >
              {sidebarOpen ? "←" : "→"}
            </button>
          </div>

          <nav className="mt-8 flex-1 overflow-y-auto">
            <ul className="space-y-2 px-2">
              <li>
                <Link
                  href="/dashboard"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <BarChart2 className="h-5 w-5" />
                  {sidebarOpen && <span>Dashboard</span>}
                </Link>
              </li>

              {usuario?.usuPerfil === "ADMIN" && (
                <li>
                  <Link
                    href="/dashboard/usuarios"
                    className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                  >
                    <Users className="h-5 w-5" />
                    {sidebarOpen && <span>Usuários</span>}
                  </Link>
                </li>
              )}
              <li>
                <Link
                  href="/dashboard/saidas"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <ShoppingCart className="h-5 w-5" />
                  {sidebarOpen && <span>Efetuar Saida</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/entradas"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <PackagePlus className="h-5 w-5" />
                  {sidebarOpen && <span>Efetuar Entrada</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/produtos"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <Box className="h-5 w-5" />
                  {sidebarOpen && <span>Produtos</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/categorias"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <Notebook className="h-5 w-5" />
                  {sidebarOpen && <span>Categorias de produtos</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/unidades"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <FileText className="h-5 w-5" />
                  {sidebarOpen && <span>Unidades</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/requisitantes"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <UserRoundPlus className="h-5 w-5" />
                  {sidebarOpen && <span>Requisitantes</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="/dashboard/faculdades"
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <BookOpen className="h-5 w-5" />
                  {sidebarOpen && <span>Faculdades</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="#"
                  onClick={() => alert("Implementações futuras !!")}
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <Clipboard className="h-5 w-5" />
                  {sidebarOpen && <span>Relatórios</span>}
                </Link>
              </li>

              <li>
                <Link
                  href="#"
                  onClick={() => alert("Implementações futuras !!")}
                  className="flex items-center gap-3 p-3 rounded-md hover:bg-blue-800"
                >
                  <List className="h-5 w-5" />
                  {sidebarOpen && <span>Requisições</span>}
                </Link>
              </li>


              <li>
                <button
                  onClick={() => logout()}
                  className="flex items-center gap-3 p-3 w-full rounded-md hover:bg-blue-800"
                >
                  <LogOut className="h-5 w-5" />
                  {sidebarOpen && <span>Sair</span>}
                </button>
              </li>
            </ul>
          </nav>
        </aside>

        {/* Main content */}
        <main className="flex-1 overflow-auto">
          <header className="bg-white shadow-sm p-4">
            <div className="flex justify-between items-center">
              <h1 className="text-xl font-semibold">Dashboard</h1>
              <div className="flex items-center gap-4">
                <span>Bem-vindo, {usuario?.usuNome}</span>
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="outline" size="icon" onClick={() => console.log("Oi")}>
                      <User className="h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end">
                    <DropdownMenuItem onClick={() => setOpenDialog("senha")}>
                      Alterar Senha
                    </DropdownMenuItem>
                    <DropdownMenuItem onClick={logout}>
                      <LogOut className="h-5 w-5" />
                      Sair

                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>

              </div>
            </div>
          </header>
          <Dialog open={openDialog === "senha"} onOpenChange={(open) => !open && setOpenDialog(null)}>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Alterar Senha</DialogTitle>
              </DialogHeader>
              {/* Formulário de alteração de senha */}
              {
                loadSenha ?
                  <Loading />
                  :
                  <Form {...form}>
                    <form
                      className="space-y-4"
                      onSubmit={form.handleSubmit(() => alterarSenhaUsuario(usuario?.usuId))}
                    >
                      <FormField
                        control={form.control}
                        name="senhaAntiga"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Senha Antiga</FormLabel>
                            <FormControl>
                              <Input {...field} type="password" />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <FormField
                        control={form.control}
                        name="novaSenha"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Nova Senha</FormLabel>
                            <FormControl>
                              <Input {...field} type="password" />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <FormField
                        control={form.control}
                        name="confirmaSenha"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Confirmar Senha</FormLabel>
                            <FormControl>
                              <Input {...field} type="password" />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <Button type="submit">Alterar Senha</Button>
                    </form>
                  </Form>
              }

            </DialogContent>
          </Dialog>

          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
              <div className="bg-white p-6 rounded-lg shadow-sm">
                <h3 className="text-gray-500 mb-2">Total de produtos</h3>
                <p className="text-3xl font-bold">{qtdProdutosAtivos}</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-sm">
                <h3 className="text-gray-500 mb-2">Produtos em Baixa</h3>
                <p className="text-3xl font-bold">{qtdProdutosAbaixoMin}</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-sm">
                <h3 className="text-gray-500 mb-2">QTD de saidas do mês</h3>
                <p className="text-3xl font-bold">{qtdMovSaida}</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-sm">
                <h3 className="text-gray-500 mb-2">QTD de entradas do mês</h3>
                <p className="text-3xl font-bold">{qtdMovEntrada}</p>
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-1 gap-6">
              <div className="bg-white p-12 rounded-lg shadow-sm">
                <h3 className="font-semibold mb-4">TOP 10 produtos com mais saidas no mês</h3>
                <table className="w-full">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left py-2">Nº</th>
                      <th className="text-left py-2">Produto</th>
                      <th className="text-left py-2">QTD. Movimentações</th>
                      <th className="text-left py-2">Total movimentado</th>
                      <th className="text-left py-2">Estoque atual</th>
                      <th className="text-left py-2">Status estoque</th>
                    </tr>
                  </thead>
                  <tbody>
                    {
                      produtosMaisMovimentado.map((produto, index) => (
                        <tr className="border-b" key={index}>
                          <td className="py-2">{index + 1}º</td>
                          <td className="py-2">{produto.produto}</td>
                          <td className="py-2">{produto.qtdMov}</td>
                          <td className="py-2">{produto.qtdTotal}</td>
                          <td className="py-2">{produto.estoque}</td>
                          <td className="py-2">
                            {produto.isAbaixoMin ? (
                              <span className="px-2 py-1 bg-red-100 text-red-800 rounded-full text-xs">Baixo</span>
                            ) : (
                              <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs">Normal</span>
                            )}
                          </td>
                        </tr>
                      )
                      )}
                  </tbody>
                </table>
              </div>

              <div className="bg-white p-12 rounded-lg shadow-sm">
                <h3 className="font-semibold mb-4">TOP 10 requisitante com mais saidas de produtos no mês</h3>
                <table className="w-full">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left py-2">Nº</th>
                      <th className="text-left py-2">Requisitante</th>
                      <th className="text-left py-2">QTD. Movimentações</th>
                      <th className="text-left py-2">Total movimentado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {
                      produtosPorRequisitante.map((requisitante, index) => (
                        <tr className="border-b" key={index}>
                          <td className="py-2">{index + 1}º</td>
                          <td className="py-2">{requisitante.requisitante}</td>
                          <td className="py-2">{requisitante.totalMovimentacao}</td>
                          <td className="py-2">{requisitante.totalProdutos}</td>
                        </tr>
                      )
                      )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  )
}


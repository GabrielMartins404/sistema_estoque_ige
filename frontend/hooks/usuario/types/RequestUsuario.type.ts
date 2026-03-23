export type RequestCadastroUsuarioType = {
    usuNome: string,
    usuLogin: string,
    usuSenha: string, 
    usuPerfil: number
}

export type RequestAtualizaUsuarioType = {
    usuNome: string,
    usuPerfil: number
}

export type RequestAlteraSenhaUsuarioType = {
    usuSenhaAntiga: string,
    usuSenhaNova: string
}
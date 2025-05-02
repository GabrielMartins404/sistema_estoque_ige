package com.estoqueige.estoqueige.services;

import com.estoqueige.estoqueige.models.Usuario;
import com.estoqueige.estoqueige.models.enums.PerfisUsuario;
import com.estoqueige.estoqueige.repositories.UsuarioRepository;
import com.estoqueige.estoqueige.security.JWTutil;
import com.estoqueige.estoqueige.security.UserSpringSecurity;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroAutorizacao;
import com.estoqueige.estoqueige.services.exceptions.ErroCamposFixos;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacoesObjRepetidos;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServices {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //Aqui serve para criptografar

    @Autowired
    private JWTutil jwtUtil;
    
    private final UsuarioRepository usuarioRepository;

    public UsuarioServices(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

    }

    /* Método services */
    public static Boolean validarUsuario(String mensagem) {
        UserSpringSecurity usuarioLogado = autenticado();

        // Verifica se o usuário está autenticado
        if (usuarioLogado == null) {
            throw new ErroAutorizacao(mensagem);
        }
    
        // Verifica se o usuário tem perfil de ADMIN
        if (usuarioLogado.hasRole(PerfisUsuario.ADMIN)) {
            return true;
        }

        throw new ErroAutorizacao(mensagem);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        //Um usuário que não é adm deverá ver somente as informações dele
        UserSpringSecurity usuarioLogado = autenticado();
        //Verifico se o usuário está logado, ou se não é admin ou se está buscando um usuário cujo id não seja o mesmo do usuário, dará erro
        if (usuarioLogado == null || (!usuarioLogado.hasRole(PerfisUsuario.ADMIN) && !id.equals(usuarioLogado.getId()))) {
            throw new ErroAutorizacao("Usuário não tem permissão para buscar outros usuários");
        }

        Optional<Usuario> usuario = this.usuarioRepository.findById(id);
        return usuario.orElseThrow(() -> new ErroAoBuscarObjetos("Não foi possivel encontrar o usuario de id: "+id));
    }

    public List<Usuario> buscarTodosUsuarios(Boolean status) {
        
        if(!validarUsuario("Usuário não tem permissão para buscar outros usuários!!!")){
            return null;
        }
        
        List<Usuario> usuarios = this.usuarioRepository.buscarUsuarios(status);
        return usuarios;
        
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        
        if(this.usuarioRepository.findByUsuLogin(usuario.getUsuLogin()).isPresent()){
            throw new ErroValidacoesObjRepetidos("Já existe um usuário cadastrado com esse login! Realizar login, por gentileza");
        }else{
            usuario.setUsuId(null);
            usuario.setUsuSenha(this.bCryptPasswordEncoder.encode(usuario.getUsuSenha())); //Esse método criptografa a senha

            // Validação do perfil de usuário
            if (usuario.getUsuPerfil() == null) {
                //Se o usuário passado for inválido, o mesmo será almoxarifado
                usuario.setUsuPerfil(PerfisUsuario.ALMOXARIFADO);
            } else if (usuario.getUsuPerfil() != PerfisUsuario.ADMIN && usuario.getUsuPerfil() != PerfisUsuario.ALMOXARIFADO) {
                throw new ErroCamposFixos("O perfil de usuário é inválido");
            }
            
            return this.usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public Usuario atualizarUsuario(Usuario usuario) {
        Usuario newUsuario = this.buscarUsuarioPorId(usuario.getUsuId());

        if (usuario.getUsuNome() != null && !usuario.getUsuNome().isBlank()) {
            newUsuario.setUsuNome(usuario.getUsuNome());
        }
        
        if (usuario.getUsuPerfil() != null) {
            newUsuario.setUsuPerfil(usuario.getUsuPerfil());
        }
        
        if (usuario.getIsAtivo() != null) {
            newUsuario.setIsAtivo(usuario.getIsAtivo());
        }

        this.usuarioRepository.save(newUsuario);

        return newUsuario;
    }

    public Usuario alterarSenhaDeUsuario(Long id, String senha, String senhaAntiga){
        if(!validarUsuario("Usuário não tem permissão para alterar senha de outros usuários")){
            return null;
        }
        Usuario usuario = this.buscarUsuarioPorId(id);

        if(senha != null && !senha.isBlank()){
            if(this.bCryptPasswordEncoder.matches(senhaAntiga, usuario.getUsuSenha())){ //Se a senha passada pelo usuário após a criptografia é igual ao hash do banco de dados
                usuario.setUsuSenha(this.bCryptPasswordEncoder.encode(senha));
            }else{
                throw new ErroAutorizacao("Senha atual não é a mesma no banco de dados!"); //Refatorar essa mensagem
            }
        }
        return this.usuarioRepository.save(usuario);
    }

    //Esse método serve para que usuários adms possam resetar a senha de usuários que tenham perdido o acesso
    //Reseta para 123
    public Usuario resetarSenhaDeUsuario(Long id){
        if(!validarUsuario("Usuário não tem permissão para alterar senha de outros usuários")){
            return null;
        }
        Usuario usuario = this.buscarUsuarioPorId(id);
        usuario.setUsuSenha(this.bCryptPasswordEncoder.encode("123"));
        return this.usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario alterarStatusAtivoUsuario(Long id) {
        if(!validarUsuario("Usuário não tem permissão para inativar usuários")){
            return null;
        }
        Usuario usuario = this.buscarUsuarioPorId(id);

        usuario.setIsAtivo(!usuario.getIsAtivo());
        return this.usuarioRepository.save(usuario);   
    }

    //Método para validar se o token repassado pelo Front ainda é valido
    //Esse método pega o token passado e verifica sua validade no JWTutil
    public Usuario validarToken(String tokenPassado){
        if(tokenPassado != null && tokenPassado.startsWith("Bearer ")){ //Verifico se o token passado inicia com Bearer
            String token = tokenPassado.substring(7);

            if(this.jwtUtil.isValidToken(token)){ //Verifico se é valido no jwtUtil
                String userName = jwtUtil.getUserName(token);
                Optional<Usuario> usuario = this.usuarioRepository.findByUsuLogin(userName); //O token só é gerado se haver senha, desse modo, basta buscar pelo nome do usuário

                if(usuario.isPresent()){
                    return usuario.get();
                }
            }
        }

        return null;
    }


    //Função serve para verificar se o usuário está autenticado
    public static UserSpringSecurity autenticado(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}

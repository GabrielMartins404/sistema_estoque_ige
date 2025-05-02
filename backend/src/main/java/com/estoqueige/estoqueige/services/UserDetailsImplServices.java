package com.estoqueige.estoqueige.services;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.Usuario;
import com.estoqueige.estoqueige.repositories.UsuarioRepository;
import com.estoqueige.estoqueige.security.UserSpringSecurity;
import com.estoqueige.estoqueige.services.exceptions.ErroAutorizacao;

@Service
public class UserDetailsImplServices implements UserDetailsService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    UserDetailsImplServices(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> buscaUsuario = this.usuarioRepository.findByUsuLogin(username);
        Usuario usuario = buscaUsuario.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if(usuario.getIsAtivo()){
            return new UserSpringSecurity(
                usuario.getUsuId(), 
                usuario.getUsuLogin(), 
                usuario.getUsuSenha(), 
                usuario.getUsuPerfil()
            );
        }else{
            throw new ErroAutorizacao("Usuário está inativo e não possui permissão para acessar o sistema!");
        }
    }
    
}

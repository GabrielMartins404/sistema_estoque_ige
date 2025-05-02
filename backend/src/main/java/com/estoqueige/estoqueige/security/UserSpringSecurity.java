package com.estoqueige.estoqueige.security;

import java.util.Collection;
import java.util.Collections;  // Para usar Collections.singletonList

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.estoqueige.estoqueige.models.enums.PerfisUsuario;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails {
    
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity(Long id, String username, String password, PerfisUsuario perfisUsuario) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(perfisUsuario.getDescricao()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(PerfisUsuario perfisUsuario){
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(perfisUsuario.getDescricao()));
    }
}

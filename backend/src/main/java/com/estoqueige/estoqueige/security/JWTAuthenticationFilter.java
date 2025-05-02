package com.estoqueige.estoqueige.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.estoqueige.estoqueige.exceptions.GlobalExceptionsHandler;
import com.estoqueige.estoqueige.models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Tudo que cair em login, vai ser usado nessa classe
//A diferença para a AuthorizationFilter é que 
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private AuthenticationManager authenticationManager;
    private JWTutil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTutil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionsHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Usuario userCredentials = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
    
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userCredentials.getUsuLogin(), userCredentials.getUsuSenha(), Collections.emptyList());
    
            return this.authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuário: " + e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException{
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
        String userName = userSpringSecurity.getUsername();
        String token = this.jwtUtil.generateToken(userSpringSecurity);
        response.addHeader("Authorization", "Bearer "+token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
    
}

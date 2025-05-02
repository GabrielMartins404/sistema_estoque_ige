package com.estoqueige.estoqueige.security;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTutil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    //Esse arquivo tem como função gerar o token de acordo com minha chave unica

    public String generateToken(UserSpringSecurity user) {
        SecretKey key = getKeyBySecret(); // Obtém a chave secreta
        
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority()) // Converte as roles para Strings
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())  // Define o usuário no "sub"
                .claim("roles", roles)           // Adiciona as permissões no token
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key)                   // Assina o token
                .compact();
    }

    private SecretKey getKeyBySecret(){
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String userName = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
    
            if (Objects.nonNull(userName) && Objects.nonNull(expirationDate) && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }


    public String getUserName(String token){
        Claims claims = getClaims(token);
        if(Objects.nonNull(claims)){
            return claims.getSubject();
        }
        return null;
    }

    private Claims getClaims(String token){
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}

package com.pedro_duarte.agenda_dentista.security;

import com.pedro_duarte.agenda_dentista.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    // Esta é a assinatura digital do seu servidor. Precisa ter mais de 32 caracteres.
    @Value("${api.security.token.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getLogin()) // Coloca o login do usuário dentro do token
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(7200))) // Token expira em 2 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validarToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // Devolve o login se o token for válido
        } catch (Exception e) {
            return null; // Retorna nulo se o token for inválido, falso ou expirado
        }
    }
}
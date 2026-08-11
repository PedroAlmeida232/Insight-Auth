package com.example.Insight_Auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.Insight_Auth.model.UserModel; // Verifique o pacote da sua classe Usuario
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Injeta a chave secreta definida no application.properties
    @Value("${api.security.token.secret}")
    private String secret;


     // Gera o token JWT assinado para o usuário autenticado.

    public String gerarToken(UserModel userModel) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("insight-auth-api") // Nome da sua aplicação/emissor
                    .withSubject(userModel.getEmail()) // Identificador único do usuário (ex: e-mail)
                    .withExpiresAt(gerarDataExpiracao()) // Data e hora de expiração
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }


     // Valida o token recebido nas requisições protegidas e retorna o e-mail .

    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("insight-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }


     //tempo de expiração do token

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
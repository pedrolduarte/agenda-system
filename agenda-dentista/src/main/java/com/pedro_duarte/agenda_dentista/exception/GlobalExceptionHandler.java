package com.pedro_duarte.agenda_dentista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Record interno para garantir sintaxe simples e serialização limpa em JSON
    public record ErroResponse(String message) {}

    // Captura validações de campo (@FutureOrPresent, @NotBlank, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse(mensagem));
    }

    // Captura regras do Service (ex: horário ocupado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse(ex.getMessage()));
    }

    // Captura recursos não encontrados (ex: agendamento inexistente)
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErroResponse> handleNotFoundResourceException(NotFoundResourceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(ex.getMessage()));
    }

    // Captura falhas de autenticação (ex: credenciais inválidas)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroResponse("Email ou senha inválidos"));
    }
}
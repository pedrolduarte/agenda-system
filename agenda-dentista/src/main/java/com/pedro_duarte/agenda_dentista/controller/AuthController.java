package com.pedro_duarte.agenda_dentista.controller;

import com.pedro_duarte.agenda_dentista.dto.AuthenticationDTO;
import com.pedro_duarte.agenda_dentista.dto.LoginResponseDTO;
import com.pedro_duarte.agenda_dentista.entity.Usuario;
import com.pedro_duarte.agenda_dentista.repository.UsuarioRepository;
import com.pedro_duarte.agenda_dentista.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationDTO data) {
        if (this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());
        Usuario newUser = new Usuario();
        newUser.setLogin(data.login());
        newUser.setSenha(encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
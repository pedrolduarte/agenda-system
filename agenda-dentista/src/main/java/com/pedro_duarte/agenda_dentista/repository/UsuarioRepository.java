package com.pedro_duarte.agenda_dentista.repository;

import com.pedro_duarte.agenda_dentista.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O Spring Security precisa buscar o usuário pelo login para validar a senha
    UserDetails findByLogin(String login);
}
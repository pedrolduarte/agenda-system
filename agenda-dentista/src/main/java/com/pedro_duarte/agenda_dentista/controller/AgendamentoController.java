package com.pedro_duarte.agenda_dentista.controller;

import com.pedro_duarte.agenda_dentista.entity.Agendamento;
import com.pedro_duarte.agenda_dentista.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos") // Esta é a URL base da nossa API
@CrossOrigin(origins = "http://localhost:5173") // Permite que o front-end (React/Next) acesse esta API depois
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    // Endpoint GET: Retorna a lista de todos os agendamentos
    @GetMapping
    public List<Agendamento> listar() {
        return service.listarTodos();
    }

    // Endpoint POST: Recebe os dados em JSON e cria um novo agendamento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna o código 201 (Criado com sucesso)
    public Agendamento criar(@Valid @RequestBody Agendamento agendamento) {
        return service.criarAgendamento(agendamento);
    }

    // Endpoint DELETE: Remove um agendamento pelo ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 (Sucesso, sem conteúdo)
    public void deletar(@PathVariable Long id) {
        // Futuramente colocamos isso no Service, mas para simplificar:
        service.deletarAgendamento(id); 
    }

    // Endpoint PATCH: Atualiza apenas um campo específico (o status)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
        @PathVariable Long id, 
        @RequestParam("status") String status) {
        
        Agendamento atualizado = service.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<Agendamento>> buscarPorData(@PathVariable LocalDate data) {
        return ResponseEntity.ok(service.buscarPorData(data));
    }
}
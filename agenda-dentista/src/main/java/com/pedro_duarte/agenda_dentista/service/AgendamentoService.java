package com.pedro_duarte.agenda_dentista.service;

import com.pedro_duarte.agenda_dentista.entity.Agendamento;
import com.pedro_duarte.agenda_dentista.exception.NotFoundResourceException;
import com.pedro_duarte.agenda_dentista.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    public Agendamento criarAgendamento(Agendamento agendamento) {
        if (repository.existsByDataHora(agendamento.getDataHora())) {
            throw new IllegalArgumentException("Já existe um agendamento para este horário exato.");
        }

        agendamento.setStatus("PENDENTE");
        return repository.save(agendamento);
    }

    public void deletarAgendamento(Long id) {
        repository.deleteById(id);
    }

    public Agendamento atualizarStatus(Long id, String novoStatus) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Agendamento não encontrado"));

        agendamento.setStatus(novoStatus);
        return repository.save(agendamento);
    }

    // Lembre-se de importar o java.time.LocalDate no topo do arquivo
    public List<Agendamento> buscarPorData(LocalDate data) {
        return repository.buscarPorData(data);
    }
}
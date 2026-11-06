package com.pedro_duarte.agenda_dentista.repository;

import com.pedro_duarte.agenda_dentista.entity.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // O Spring gera o SQL automaticamente por causa da palavra "existsBy..."
    boolean existsByDataHora(LocalDateTime dataHora);

    // Compara apenas a data (ignora a hora do agendamento)
    @Query("SELECT a FROM Agendamento a WHERE CAST(a.dataHora AS date) = :data")
    List<Agendamento> buscarPorData(@Param("data") LocalDate data);

}
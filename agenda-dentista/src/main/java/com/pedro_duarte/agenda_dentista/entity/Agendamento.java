package com.pedro_duarte.agenda_dentista.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import java.time.LocalDateTime;
import com.pedro_duarte.agenda_dentista.entity.StatusAgendamento;

@Entity
@Table(name = "agendamentos")
@Data // Anotação do Lombok que cria os Getters e Setters por baixo dos panos
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do paciente é obrigatório")
    @Column(nullable = false, length = 100)
    private String nomePaciente;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(nullable = false, length = 20)
    private String telefonePaciente;

    @NotNull(message = "A data e hora são obrigatórias")
    @FutureOrPresent(message = "Não é possível agendar uma consulta no passado")
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "O status é obrigatório")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    // O @Transient diz ao banco de dados para ignorar este campo
    @Transient
    public String getLinkWhatsapp() {
        // Formata a mensagem com os dados do paciente
        String mensagem = "Olá, " + nomePaciente + "! Tudo bem? Passando para confirmar sua consulta no dia " 
                + dataHora + ". Responda SIM para confirmar.";
        
        // Substitui os espaços por %20 para a URL funcionar corretamente na internet
        String mensagemFormatada = mensagem.replace(" ", "%20");
        
        // Retorna o link oficial do WhatsApp
        return "https://wa.me/55" + telefonePaciente + "?text=" + mensagemFormatada;
    }
}
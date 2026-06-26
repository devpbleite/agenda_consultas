package br.com.clinica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtendimento tipoAtendimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column
    private String motivoCancelamento;

    public Agendamento() {}

    public Agendamento(Paciente paciente, Profissional profissional,
                       LocalDateTime dataHora, TipoAtendimento tipoAtendimento) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.dataHora = dataHora;
        this.tipoAtendimento = tipoAtendimento;
        this.status = StatusAgendamento.AGENDADO;
    }

    public Long getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public Profissional getProfissional() { return profissional; }
    public LocalDateTime getDataHora() { return dataHora; }
    public TipoAtendimento getTipoAtendimento() { return tipoAtendimento; }
    public StatusAgendamento getStatus() { return status; }
    public String getMotivoCancelamento() { return motivoCancelamento; }

    public void setId(Long id) { this.id = id; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setTipoAtendimento(TipoAtendimento tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public void setMotivoCancelamento(String motivoCancelamento) { this.motivoCancelamento = motivoCancelamento; }
}

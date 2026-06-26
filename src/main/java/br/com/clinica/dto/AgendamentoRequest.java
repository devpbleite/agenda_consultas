package br.com.clinica.dto;

import br.com.clinica.model.TipoAtendimento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull(message = "ID do paciente é obrigatório") Long pacienteId,
        @NotNull(message = "ID do profissional é obrigatório") Long profissionalId,
        @NotNull(message = "Data e hora são obrigatórias")
        @Future(message = "A data do agendamento deve ser no futuro")
        LocalDateTime dataHora,
        @NotNull(message = "Tipo de atendimento é obrigatório") TipoAtendimento tipoAtendimento
) {}

package br.com.clinica.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelamentoRequest(
        @NotBlank(message = "Motivo do cancelamento é obrigatório") String motivo
) {}

package br.com.clinica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PacienteRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "CPF é obrigatório") String cpf,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        String telefone
) {}

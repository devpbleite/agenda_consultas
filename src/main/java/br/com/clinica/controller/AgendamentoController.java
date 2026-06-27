package br.com.clinica.controller;

import br.com.clinica.dto.AgendamentoRequest;
import br.com.clinica.dto.CancelamentoRequest;
import br.com.clinica.model.Agendamento;
import br.com.clinica.model.StatusAgendamento;
import br.com.clinica.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(@Valid @RequestBody AgendamentoRequest request) {
        Agendamento agendamento = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long profissionalId,
            @RequestParam(required = false) StatusAgendamento status) {
        return ResponseEntity.ok(service.listar(pacienteId, profissionalId, status));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agendamento> cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelamentoRequest request) {
        Agendamento cancelado = service.cancelar(id, request);
        return ResponseEntity.ok(cancelado);
    }
}

package br.com.clinica.service;

import br.com.clinica.dto.AgendamentoRequest;
import br.com.clinica.dto.CancelamentoRequest;
import br.com.clinica.exception.RecursoNaoEncontradoException;
import br.com.clinica.exception.RegraDeNegocioException;
import br.com.clinica.model.Agendamento;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.Profissional;
import br.com.clinica.model.StatusAgendamento;
import br.com.clinica.repository.AgendamentoRepository;
import br.com.clinica.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteService pacienteService;
    private final ProfissionalRepository profissionalRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              PacienteService pacienteService,
                              ProfissionalRepository profissionalRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteService = pacienteService;
        this.profissionalRepository = profissionalRepository;
    }

    public Agendamento criar(AgendamentoRequest request) {
        Paciente paciente = pacienteService.buscarPorId(request.pacienteId());
        Profissional profissional = profissionalRepository.findById(request.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Profissional não encontrado com ID: " + request.profissionalId()));

        // Regra: profissional não pode ter dois agendamentos no mesmo horário
        boolean conflitoDeHorario = agendamentoRepository
                .existsByProfissionalIdAndDataHoraAndStatusNot(
                        request.profissionalId(),
                        request.dataHora(),
                        StatusAgendamento.CANCELADO
                );
        if (conflitoDeHorario) {
            throw new RegraDeNegocioException(
                    "O profissional já possui um agendamento em " + request.dataHora());
        }

        // Regra: não permitir data no passado (salvaguarda além do @Future do DTO)
        if (request.dataHora().isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
            throw new RegraDeNegocioException("Não é permitido agendar em data/hora passada.");
        }

        Agendamento agendamento = new Agendamento(
                paciente, profissional, request.dataHora(), request.tipoAtendimento());
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listar(Long pacienteId, Long profissionalId, StatusAgendamento status) {
        return agendamentoRepository.findWithFilters(pacienteId, profissionalId, status);
    }

    public Agendamento cancelar(Long id, CancelamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado com ID: " + id));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RegraDeNegocioException("Este agendamento já está cancelado.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(request.motivo());
        return agendamentoRepository.save(agendamento);
    }
}

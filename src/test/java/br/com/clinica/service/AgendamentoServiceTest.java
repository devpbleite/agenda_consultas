package br.com.clinica.service;

import br.com.clinica.dto.AgendamentoRequest;
import br.com.clinica.dto.CancelamentoRequest;
import br.com.clinica.exception.RegraDeNegocioException;
import br.com.clinica.model.*;
import br.com.clinica.repository.AgendamentoRepository;
import br.com.clinica.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private PacienteService pacienteService;
    @Mock private ProfissionalRepository profissionalRepository;

    @InjectMocks private AgendamentoService service;

    private Paciente paciente;
    private Profissional profissional;
    private LocalDateTime dataFutura;

    @BeforeEach
    void setUp() {
        paciente = new Paciente("João Silva", "123.456.789-00", "joao@email.com", "11999999999");
        profissional = new Profissional("Dra. Ana Lima", "Cardiologia", "CRM-12345");
        dataFutura = LocalDateTime.now(ZoneId.systemDefault()).plusDays(1);
    }

    @Test
    @DisplayName("Deve criar agendamento com sucesso quando não há conflito")
    void deveCriarAgendamentoComSucesso() {
        AgendamentoRequest request = new AgendamentoRequest(1L, 1L, dataFutura, TipoAtendimento.CONSULTA);
        Agendamento agendamentoSalvo = new Agendamento(paciente, profissional, dataFutura, TipoAtendimento.CONSULTA);

        when(pacienteService.buscarPorId(1L)).thenReturn(paciente);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(agendamentoRepository.existsByProfissionalIdAndDataHoraAndStatusNot(
                anyLong(), any(), any()
        )).thenReturn(false);
        when(agendamentoRepository.save(any())).thenReturn(agendamentoSalvo);

        Agendamento resultado = service.criar(request);

        assertNotNull(resultado);
        assertEquals(StatusAgendamento.AGENDADO, resultado.getStatus());
        assertEquals(TipoAtendimento.CONSULTA, resultado.getTipoAtendimento());
    }

    @Test
    @DisplayName("Deve lançar exceção quando profissional já tem agendamento no mesmo horário")
    void deveLancarExcecaoQuandoConflitoDeHorario() {
        AgendamentoRequest request = new AgendamentoRequest(1L, 1L, dataFutura, TipoAtendimento.CONSULTA);

        when(pacienteService.buscarPorId(1L)).thenReturn(paciente);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(agendamentoRepository.existsByProfissionalIdAndDataHoraAndStatusNot(
                eq(1L), eq(dataFutura), eq(StatusAgendamento.CANCELADO)
        )).thenReturn(true);

        RegraDeNegocioException ex = assertThrows(
                RegraDeNegocioException.class,
                () -> service.criar(request)
        );

        assertTrue(ex.getMessage().contains("já possui um agendamento"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar agendamento já cancelado")
    void deveLancarExcecaoAoCancelarAgendamentoJaCancelado() {
        Agendamento jaCancelado = new Agendamento(paciente, profissional, dataFutura, TipoAtendimento.RETORNO);
        jaCancelado.setStatus(StatusAgendamento.CANCELADO);

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(jaCancelado));

        RegraDeNegocioException ex = assertThrows(
                RegraDeNegocioException.class,
                () -> service.cancelar(1L, new CancelamentoRequest("motivo qualquer"))
        );

        assertTrue(ex.getMessage().contains("já está cancelado"));
    }
}

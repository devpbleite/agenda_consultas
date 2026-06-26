package br.com.clinica.repository;

import br.com.clinica.model.Agendamento;
import br.com.clinica.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Verifica conflito de horário para um profissional (ignora agendamentos cancelados)
    boolean existsByProfissionalIdAndDataHoraAndStatusNot(
            Long profissionalId,
            LocalDateTime dataHora,
            StatusAgendamento status
    );

    // Listagem com filtros opcionais por paciente, profissional e status
    @Query("""
        SELECT a FROM Agendamento a
        WHERE (:pacienteId IS NULL OR a.paciente.id = :pacienteId)
          AND (:profissionalId IS NULL OR a.profissional.id = :profissionalId)
          AND (:status IS NULL OR a.status = :status)
        ORDER BY a.dataHora
    """)
    List<Agendamento> findWithFilters(
            @Param("pacienteId") Long pacienteId,
            @Param("profissionalId") Long profissionalId,
            @Param("status") StatusAgendamento status
    );
}

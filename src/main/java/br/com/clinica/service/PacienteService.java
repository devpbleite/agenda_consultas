package br.com.clinica.service;

import br.com.clinica.dto.PacienteRequest;
import br.com.clinica.exception.RecursoNaoEncontradoException;
import br.com.clinica.exception.RegraDeNegocioException;
import br.com.clinica.model.Paciente;
import br.com.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente cadastrar(PacienteRequest request) {
        if (repository.existsByCpf(request.cpf())) {
            throw new RegraDeNegocioException("Já existe um paciente cadastrado com o CPF: " + request.cpf());
        }
        Paciente paciente = new Paciente(request.nome(), request.cpf(), request.email(), request.telefone());
        return repository.save(paciente);
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com ID: " + id));
    }
}

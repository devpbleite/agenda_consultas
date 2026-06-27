package br.com.clinica.service;

import br.com.clinica.dto.PacienteRequest;
import br.com.clinica.exception.RegraDeNegocioException;
import br.com.clinica.model.Paciente;
import br.com.clinica.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @InjectMocks
    private PacienteService service;

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar CPF duplicado")
    void deveLancarExcecaoCpfDuplicado() {
        PacienteRequest request = new PacienteRequest("João", "111.111.111-11", "joao@email.com", "11999999999");
        
        when(repository.existsByCpf("111.111.111-11")).thenReturn(true);

        RegraDeNegocioException ex = assertThrows(
                RegraDeNegocioException.class,
                () -> service.cadastrar(request)
        );

        assertTrue(ex.getMessage().contains("Já existe um paciente cadastrado com o CPF"));
    }

    @Test
    @DisplayName("Deve cadastrar paciente com sucesso quando CPF é inédito")
    void deveCadastrarComSucesso() {
        PacienteRequest request = new PacienteRequest("João", "111.111.111-11", "joao@email.com", "11999999999");
        Paciente pacienteSalvo = new Paciente("João", "111.111.111-11", "joao@email.com", "11999999999");
        
        when(repository.existsByCpf("111.111.111-11")).thenReturn(false);
        when(repository.save(any())).thenReturn(pacienteSalvo);

        Paciente resultado = service.cadastrar(request);

        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("111.111.111-11", resultado.getCpf());
    }
}

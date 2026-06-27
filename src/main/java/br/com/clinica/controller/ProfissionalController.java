package br.com.clinica.controller;

import br.com.clinica.model.Profissional;
import br.com.clinica.repository.ProfissionalRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalRepository repository;

    public ProfissionalController(ProfissionalRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Profissional> cadastrar(@Valid @RequestBody Profissional profissional) {
        Profissional salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}

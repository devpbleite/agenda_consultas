package br.com.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Especialidade é obrigatória")
    @Column(nullable = false)
    private String especialidade;

    @Column(unique = true)
    private String registro; // CRM, CRO, etc.

    public Profissional() {}

    public Profissional(String nome, String especialidade, String registro) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.registro = registro;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecialidade() { return especialidade; }
    public String getRegistro() { return registro; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public void setRegistro(String registro) { this.registro = registro; }
}

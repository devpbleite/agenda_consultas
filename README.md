# Clínica Agendamentos — API REST

API desenvolvida para o gerenciamento de agendamentos de consultas em clínicas, focando no princípio "simples e bem feito", utilizando boas práticas de desenvolvimento back-end e orientação a objetos.

## 🛠️ Tecnologias Utilizadas

- **Java 21 (LTS)**: Linguagem base.
- **Spring Boot 3.5.0**: Framework para criação ágil da API REST.
- **Maven**: Gerenciamento de dependências e build.
- **H2 Database**: Banco de dados relacional embarcado operando em modo arquivo, garantindo persistência local sem exigir configuração de infraestrutura externa do avaliador.
- **Spring Data JPA + Hibernate**: Persistência de dados.
- **Bean Validation (Jakarta)**: Validação de entradas na API.
- **JUnit 5 & Mockito**: Testes unitários das regras de negócio.

## ⚙️ Pré-requisitos

Para testar o projeto na sua máquina, você só precisa ter instalado:
- **JDK 21**
- **Maven 3.8+**

> Não é necessário instalar nenhum banco de dados (MySQL/PostgreSQL), o projeto rodará perfeitamente e os dados persistirão em um arquivo local na pasta `/data`.

## 🚀 Como Executar o Projeto

**1. Clone o repositório:**
```bash
git clone https://github.com/devpbleite/agenda_consultas.git
cd agenda_consultas
```

**2. Rode a aplicação:**
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

**3. Acesse o Banco de Dados (Console H2):**
Para inspecionar os dados via interface web durante a avaliação, com a aplicação rodando, acesse:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/agendadb`
- **User Name**: `sa`
- **Password**: *(deixe em branco)*

## 🧪 Como Executar os Testes Unitários

Para garantir que as regras de negócio estão blindadas:
```bash
mvn test
```

## 📋 Endpoints Principais

### Pacientes
- `POST /pacientes`: Cadastra um paciente (Valida CPF único).
- `GET /pacientes`: Lista pacientes.

### Profissionais
- `POST /profissionais`: Cadastra um profissional.
- `GET /profissionais`: Lista profissionais.

### Agendamentos
- `POST /agendamentos`: Cria um agendamento.
- `GET /agendamentos`: Lista agendamentos (Permite filtros opcionais via Query Params: `?pacienteId=1&profissionalId=1&status=AGENDADO`).
- `PATCH /agendamentos/{id}/cancelar`: Cancela um agendamento justificando o motivo.

## 💼 Regras de Negócio Implementadas

1. **Conflito de Horário**: Um profissional não pode ter dois agendamentos simultâneos no mesmo horário.
2. **Datas Retroativas**: O sistema bloqueia agendamentos criados com datas que já passaram.
3. **Soft Delete / Cancelamento Justificado**: O agendamento nunca é deletado fisicamente. Seu status muda para `CANCELADO` e um motivo passa a ser exigido.
4. **CPF Único**: O sistema bloqueia a duplicidade de CPFs no cadastro de pacientes.

## 🛑 Tratamento de Erros

A API possui um `GlobalExceptionHandler` configurado (`@RestControllerAdvice`) para padronizar o retorno de todos os erros (400, 404, etc.) em um formato JSON amigável e previsível para quem consome a API.

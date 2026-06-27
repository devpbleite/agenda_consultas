# DECISOES.md — Justificativas Técnicas e Arquiteturais

Este documento visa expor a linha de raciocínio utilizada durante a estruturação e desenvolvimento da API. Como candidato à vaga de Desenvolvedor Júnior, meu foco foi entregar o que foi solicitado de forma **simples, segura e muito bem feita**, priorizando os fundamentos em detrimento de overengineering.

## 1. Escolha da Stack

- **Java 21 (LTS)**: Escolha pela segurança de estar usando a versão com suporte de longo prazo mais atualizada.
- **H2 em modo arquivo (`jdbc:h2:file:./data/agendadb`)**: Decidi utilizar o H2 ao invés do MySQL/PostgreSQL visando a **experiência do avaliador**. O modo arquivo (`file`) simula perfeitamente um banco real persistindo os dados entre reinicializações do servidor, sem o peso de exigir a configuração de um contêiner Docker ou banco de dados local na máquina de quem for corrigir o teste.
- **Maven**: Ferramenta de build consolidada. Ideal para projetos Spring Boot tradicionais pela vasta documentação, essencial para o nível júnior.

## 2. Modelagem de Dados

- **Uso de Enums (`StatusAgendamento`, `TipoAtendimento`)**: Garantem integridade no banco e no código, evitando "magic strings". Salvos como `STRING` no banco de dados para melhorar a legibilidade de consultas SQL nativas.
- **Soft Delete no Cancelamento**: Em aplicações reais, dados médicos e históricos de agendamentos raramente são excluídos (`DELETE FROM`). A decisão arquitetural foi alterar o `status` e exigir um `motivoCancelamento`, mantendo um histórico auditável.

## 3. Padrão Arquitetural

Foi adotada a arquitetura tradicional em **camadas (Layered Architecture)**, o que traz organização e facilidade de manutenção sem adicionar complexidades de Hexagonal ou Clean Architecture que não cabem no escopo deste teste:

- `controller`: Interface de comunicação HTTP.
- `service`: Isolamento total das regras de negócio.
- `repository`: Comunicação com o JPA.
- `model`: Entidades e mapeamento relacional.
- `dto`: Padrão Data Transfer Object implementado com **Java Records**.

## 4. Uso de DTOs e Java Records

A API nunca recebe ou devolve as Entidades JPA diretamente (`Paciente`, `Agendamento`).

- **O problema**: Retornar entidades expõe a estrutura de banco de dados, cria problemas de recursão infinita (JSON) com `@ManyToOne` e misturam anotações de banco com anotações de payload.
- **A solução**: Implementei **Java Records** (funcionalidade recente do Java 14+) para criar DTOs imutáveis, concisos e limpos (como o `AgendamentoRequest`). Toda a validação (`@NotBlank`, `@Future`) ocorre diretamente no DTO, antes mesmo da requisição atingir a camada de serviço.

## 5. Regras de Negócio e Testes

As três regras principais (conflito de horário, restrição de data no passado e cancelamento duplo) foram encapsuladas no `AgendamentoService`.
Para provar o funcionamento, **Testes Unitários** usando **JUnit 5 + Mockito** foram implementados para essa classe. A escolha de usar Mockito ao invés de carregar todo o contexto do Spring (Testes de Integração) permite que os testes rodem em milissegundos.

## 6. Handler Global de Exceções

Implementei um `@RestControllerAdvice`. Isso significa que qualquer exceção disparada no sistema (`RegraDeNegocioException`, validações falhas do DTO, ou `RecursoNaoEncontradoException`) é capturada globalmente, formatada e retornada com os timestamps e HTTP Status corretos, gerando uma experiência impecável para o Front-End que for consumir a API.

## 7. O Que Ficou de Fora (Trade-offs)

- **Spring Security (JWT/Auth)**: Poderia ser implementado, porém o enunciado focava estritamente no fluxo de negócio de agendamentos. Manter simples significa focar no core business.
- **Paginação**: Endpoints como `GET /agendamentos` retornam Listas simples. Numa versão de produção v2, adicionaríamos paginação (`Pageable`) e Hateoas.
- **Front-end em Angular**: Embora fosse um diferencial, dediquei 100% do tempo do teste em garantir que a arquitetura do Back-end fosse robusta, tipada, testada e tolerante a falhas.
- **Banco Oracle**: Para simplificar a execução por parte do avaliador, optei pelo H2. No entanto, o código foi feito usando Spring Data JPA (Hibernate), o que significa que a aplicação é 100% compatível com Oracle, bastando trocar a dependência no `pom.xml` e a URL no `application.properties`.

## 8. Uso de Inteligência Artificial (IA)

Em conformidade com o que foi solicitado no case, declaro o uso de IA (como pair programming) durante o desenvolvimento:

- **Em quais partes utilizei**: Na geração de código "boilerplate" (getters, setters, imports, anotações JPA padrão) e para estruturar a base inicial do projeto e dos arquivos `.md`.
- **Como validei o resultado**: Toda a arquitetura, como decisão de usar DTOs, Enums, tratamento de exceções globais, foi definida e guiada por mim enquanto fui estudando sobre os temas. Revisei todas as regras de negócio geradas, como a query de conflito de horários, e executei os testes unitários localmente para garantir que as validações de datas e status de cancelamento estavam operando de acordo com as regras de negócio estipuladas no enunciado. O principal uso da IA foi para acelerar o desenvolvimento e garantir que eu estava seguindo as melhores práticas de mercado, enquanto revia e tirava dúvidas de assuntos que já não via a algum tempo.

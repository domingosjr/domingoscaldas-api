# domingoscaldas-api — BJJ School

Projeto da disciplina **Desenvolvimento de aplicações Java com Spring Boot [26E3_2]** (Infnet).

**Aluno:** Domingos Caldas de Oliveira Junior

## Domínio

Gestão de uma escola de Jiu-Jitsu:

- **Cadastro de alunos e instrutores** (`Pessoa` abstrata → `Aluno`, `Instrutor`);
- **Frequência** (`Presenca`): cada presença em treino conta para a graduação;
- **Campeonatos e conquistas** (`Campeonato`, `Conquista` com medalha de ouro/prata/bronze), que também pesam na graduação;
- **Graduações** (`Graduacao`): histórico de troca de faixa (`Faixa`: branca → azul → roxa → marrom → preta) e de graus na mesma faixa.

Relacionamentos um-para-muitos: `Aluno` 1-N `Presenca`, `Aluno` 1-N `Conquista`, `Aluno` 1-N `Graduacao`.

## Como executar

Requisitos: JDK 17+ e Maven.

```bash
mvn spring-boot:run
```

A rotina de inicialização (`ProjectRunner`) carrega dados de demonstração pela camada de serviço e exercita o modelo no console. A API sobe em `http://localhost:8080`.

## API REST

Arquitetura: Cliente HTTP → `Controller` → `Service` → `Map` (armazenamento em memória até a etapa 4).

| Recurso | Endpoints |
|---|---|
| `/alunos` | `GET` lista · `GET /{id}` · `POST` (201 + Location) · `PUT /{id}` · `DELETE /{id}` (204) · filtros `?nome=` `?faixa=AZUL` `?ativos=true` `?aptosGraduacao=true` `?ordenarPor=frequencia` · `GET /{id}/pontos-graduacao` |
| `/alunos/{id}/presencas` | `GET` · `POST` registra presença do aluno |
| `/alunos/{id}/conquistas` | `GET` · `POST ?campeonatoId=` registra conquista em campeonato |
| `/alunos/{id}/graduacoes` | `GET` · `POST` registra graduação (atualiza faixa/graus do aluno) |
| `/instrutores`, `/campeonatos` | CRUD completo |
| `/presencas` | `GET` lista/id · `?inicio=&fim=` · `PUT` · `DELETE` |
| `/conquistas` | `GET` lista/id · `?medalha=OURO` · `GET /quadro-medalhas` · `PUT` · `DELETE` |
| `/graduacoes` | `GET` lista/id · `PUT` · `DELETE` |

Códigos HTTP: `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request` (dados inválidos), `404 Not Found` (id inexistente), `409 Conflict` (id duplicado) — tratados centralmente no `GlobalExceptionHandler` (`@RestControllerAdvice`).

- **Documentação OpenAPI/Swagger:** http://localhost:8080/swagger-ui.html (JSON em `/v3/api-docs`).
- **Collection Postman:** [`postman/domingoscaldas-api.postman_collection.json`](postman/domingoscaldas-api.postman_collection.json) — importar no Postman; variável `baseUrl` já aponta para `http://localhost:8080`.

## Etapas do projeto

- **etapa-1** — Orientação a Objetos: modelo de negócio (classes, herança, interface `Identificavel`, relacionamentos, enums) + rotina de inicialização no console.
- **etapa-2** — Estruturas de Dados e Serviços: armazenamento em memória com `Map`, camada `Service` (CRUD), consultas com Collections/lambdas/Streams e exceções customizadas.
- **etapa-3** — API REST com Spring Boot: controllers REST (GET/POST/PUT/DELETE), injeção de dependência por construtor, `ResponseEntity` com códigos HTTP adequados, `@RestControllerAdvice`, Swagger e collection Postman.
- **etapa-4** — Persistência com Spring Data JPA *(a fazer)*.

Cada etapa concluída é registrada com uma tag git (`etapa-1`, `etapa-2`, ...).

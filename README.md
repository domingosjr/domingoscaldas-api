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

Requisitos: JDK 17+ (o Maven Wrapper baixa o Maven sozinho).

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080` com banco **H2 em memória**. A rotina de inicialização (`ProjectRunner`) persiste dados de demonstração pela camada de serviço e exercita o modelo no console (desative com `app.runner.habilitado=false` no `application.properties`).

- **Console do H2:** http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:domingoscaldasdb`, usuário `sa`, senha em branco).

## API REST

Arquitetura: Cliente HTTP → `Controller` → `Service` → `Repository` → Banco de dados (H2).

Persistência com **Spring Data JPA**: entidades mapeadas com `@Entity`/`@Id`/`@GeneratedValue(IDENTITY)`; herança `Pessoa` → `Aluno`/`Instrutor` com estratégia **JOINED**; relacionamentos um-para-muitos com `@OneToMany(mappedBy)`/`@ManyToOne` + `@JoinColumn` (`aluno_id`, `campeonato_id`); consultas derivadas (`findByAtivoTrue`, `findByNomeContainingIgnoreCase`, `findByFaixa`, `findAllByOrderByNomeAsc`, `findByDataBetween`, `findByMedalha`); **Bean Validation** (`@NotBlank`, `@Size`, `@Email`, `@NotNull`, `@PositiveOrZero`) com `@Valid` nos controllers. Os identificadores agora são gerados pelo banco — o POST não recebe `id`.

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

Códigos HTTP: `200 OK`, `201 Created` (+ header `Location`), `204 No Content`, `400 Bad Request` (dados inválidos / Bean Validation, com os campos e mensagens no corpo), `404 Not Found` (id inexistente) — tratados centralmente no `GlobalExceptionHandler` (`@RestControllerAdvice`), sempre com o corpo padronizado `ErroResponse` (status, erro, mensagem, dataHora).

- **Documentação OpenAPI/Swagger:** http://localhost:8080/swagger-ui.html (JSON em `/v3/api-docs`).
- **Collection Postman:** [`postman/domingoscaldas-api.postman_collection.json`](postman/domingoscaldas-api.postman_collection.json) — importar no Postman; variável `baseUrl` já aponta para `http://localhost:8080`.

## Etapas do projeto

- **etapa-1** — Orientação a Objetos: modelo de negócio (classes, herança, interface `Identificavel`, relacionamentos, enums) + rotina de inicialização no console.
- **etapa-2** — Estruturas de Dados e Serviços: armazenamento em memória com `Map`, camada `Service` (CRUD), consultas com Collections/lambdas/Streams e exceções customizadas.
- **etapa-3** — API REST com Spring Boot: controllers REST (GET/POST/PUT/DELETE), injeção de dependência por construtor, `ResponseEntity` com códigos HTTP adequados, `@RestControllerAdvice`, Swagger e collection Postman.
- **etapa-4** — Persistência com Spring Data JPA: repositories, entidades JPA (herança JOINED, relacionamentos com FK), consultas derivadas `findBy...`, Bean Validation e banco H2 substituindo os Maps em memória.

Cada etapa concluída é registrada com uma tag git (`etapa-1`, `etapa-2`, ...).

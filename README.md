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

A rotina de inicialização (`ProjectRunner`) instancia o modelo, estabelece os relacionamentos e apresenta os dados no console.

## Etapas do projeto

- **etapa-1** — Orientação a Objetos: modelo de negócio (classes, herança, interface `Identificavel`, relacionamentos, enums) + rotina de inicialização no console.
- **etapa-2** — Estruturas de Dados e Serviços: armazenamento em memória com `Map`, camada `Service` (CRUD), consultas com Collections/lambdas/Streams e exceções customizadas.
- **etapa-3** — API REST com Spring Boot *(a fazer)*.
- **etapa-4** — Persistência com Spring Data JPA *(a fazer)*.

Cada etapa concluída é registrada com uma tag git (`etapa-1`, `etapa-2`, ...).

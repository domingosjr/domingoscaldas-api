package br.edu.infnet.domingoscaldasapi;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.infnet.domingoscaldasapi.domain.Aluno;
import br.edu.infnet.domingoscaldasapi.domain.Campeonato;
import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Faixa;
import br.edu.infnet.domingoscaldasapi.domain.Graduacao;
import br.edu.infnet.domingoscaldasapi.domain.Instrutor;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;
import br.edu.infnet.domingoscaldasapi.domain.Pessoa;
import br.edu.infnet.domingoscaldasapi.domain.Presenca;

/**
 * Rotina de inicialização: instancia os objetos do domínio, estabelece os
 * relacionamentos e apresenta os dados no console, demonstrando o modelo.
 */
@Component
public class ProjectRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		System.out.println("===== BJJ School - demonstração do modelo de domínio =====");

		Instrutor instrutor = new Instrutor(1L, "Ricardo Almeida", "ricardo@bjjschool.com.br", "(21) 99999-0001",
				Faixa.PRETA, 3, "CBJJ-12345", true);

		Aluno anderson = new Aluno(1L, "Anderson Souza", "anderson@gmail.com", "(21) 98888-0002",
				LocalDate.of(1995, 3, 10), LocalDate.of(2024, 2, 1), 82.5, true, Faixa.AZUL, 1);

		Aluno beatriz = new Aluno(2L, "Beatriz Lima", "beatriz@gmail.com", "(21) 97777-0003",
				LocalDate.of(2000, 11, 25), LocalDate.of(2025, 6, 15), 61.0, true, Faixa.BRANCA, 2);

		demonstrarHerancaEPolimorfismo(instrutor, anderson, beatriz);
		demonstrarRelacionamentoPresencas(anderson);
		demonstrarRelacionamentoConquistas(anderson, beatriz);
		demonstrarGraduacao(beatriz);
		demonstrarValidacao(anderson);
	}

	private void demonstrarHerancaEPolimorfismo(Instrutor instrutor, Aluno anderson, Aluno beatriz) {

		System.out.println("\n--- Herança e polimorfismo (List<Pessoa>) ---");

		List<Pessoa> pessoas = List.of(instrutor, anderson, beatriz);
		pessoas.forEach(System.out::println);
	}

	private void demonstrarRelacionamentoPresencas(Aluno aluno) {

		System.out.println("\n--- Relacionamento um-para-muitos: Aluno x Presenca ---");
		System.out.println("Antes das presenças: " + aluno);

		aluno.adicionarPresenca(new Presenca(1L, LocalDate.of(2026, 8, 3), "Gi"));
		aluno.adicionarPresenca(new Presenca(2L, LocalDate.of(2026, 8, 5), "No-Gi"));
		aluno.adicionarPresenca(new Presenca(3L, LocalDate.of(2026, 8, 7), "Gi"));

		System.out.println("Depois das presenças: " + aluno);

		for (Presenca presenca : aluno.getPresencas()) {
			System.out.println("  " + presenca);
		}
	}

	private void demonstrarRelacionamentoConquistas(Aluno anderson, Aluno beatriz) {

		System.out.println("\n--- Relacionamento um-para-muitos: Aluno x Conquista ---");

		Campeonato copaRio = new Campeonato(1L, "Copa Rio de Jiu-Jitsu", "Rio de Janeiro",
				LocalDate.of(2026, 5, 17));
		System.out.println(copaRio);

		anderson.adicionarConquista(new Conquista(1L, "Adulto Azul Pena", Medalha.OURO, copaRio));
		beatriz.adicionarConquista(new Conquista(2L, "Adulto Branca Leve", Medalha.PRATA, copaRio));

		anderson.getConquistas().forEach(conquista -> System.out.println("  " + conquista));
		beatriz.getConquistas().forEach(conquista -> System.out.println("  " + conquista));
	}

	private void demonstrarGraduacao(Aluno aluno) {

		System.out.println("\n--- Graduação: atualização de faixa/graus pelo histórico ---");
		System.out.println("Antes da graduação: faixa " + aluno.getFaixa() + ", " + aluno.getGraus() + " grau(s)");

		Faixa novaFaixa = aluno.getFaixa().proxima();
		aluno.adicionarGraduacao(new Graduacao(1L, novaFaixa, 0, LocalDate.of(2026, 8, 10)));

		System.out.println("Depois da graduação: faixa " + aluno.getFaixa() + ", " + aluno.getGraus() + " grau(s)");
		aluno.getGraduacoes().forEach(graduacao -> System.out.println("  " + graduacao));
	}

	private void demonstrarValidacao(Aluno aluno) {

		System.out.println("\n--- Validação: presença nula é barrada ---");

		try {
			aluno.adicionarPresenca(null);
		} catch (IllegalArgumentException e) {
			System.out.println("Erro esperado: " + e.getMessage());
		}
	}
}

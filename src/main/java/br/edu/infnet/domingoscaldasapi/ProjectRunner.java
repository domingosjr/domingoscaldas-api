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
import br.edu.infnet.domingoscaldasapi.exception.IdentificadorDuplicadoException;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.service.AlunoService;
import br.edu.infnet.domingoscaldasapi.service.CampeonatoService;
import br.edu.infnet.domingoscaldasapi.service.ConquistaService;
import br.edu.infnet.domingoscaldasapi.service.GraduacaoService;
import br.edu.infnet.domingoscaldasapi.service.InstrutorService;
import br.edu.infnet.domingoscaldasapi.service.PresencaService;

/**
 * Rotina de inicialização: carrega os dados de demonstração através da camada
 * de serviço (Aplicação -> Service -> Map) e exercita o modelo, o CRUD, as
 * consultas com Streams e o tratamento de exceções.
 */
@Component
public class ProjectRunner implements CommandLineRunner {

	private final AlunoService alunoService;
	private final InstrutorService instrutorService;
	private final PresencaService presencaService;
	private final CampeonatoService campeonatoService;
	private final ConquistaService conquistaService;
	private final GraduacaoService graduacaoService;

	public ProjectRunner(AlunoService alunoService, InstrutorService instrutorService,
			PresencaService presencaService, CampeonatoService campeonatoService,
			ConquistaService conquistaService, GraduacaoService graduacaoService) {
		this.alunoService = alunoService;
		this.instrutorService = instrutorService;
		this.presencaService = presencaService;
		this.campeonatoService = campeonatoService;
		this.conquistaService = conquistaService;
		this.graduacaoService = graduacaoService;
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("===== BJJ School - demonstração (services + coleções; API REST em /alunos, /presencas...) =====");

		carregarDados();
		demonstrarHerancaEPolimorfismo();
		demonstrarCrud();
		demonstrarConsultasComStreams();
		demonstrarTratamentoDeExcecoes();
	}

	private void carregarDados() {

		instrutorService.incluir(new Instrutor(1L, "Domingos Caldas", "domingojr@bjjschool.com.br", "(21) 99999-0001",
				Faixa.PRETA, 3, "CBJJ-484817", true));

		Aluno anderson = new Aluno(1L, "Anderson Souza", "anderson@gmail.com", "(21) 98888-0002",
				LocalDate.of(1995, 3, 10), LocalDate.of(2024, 2, 1), 82.5, true, Faixa.AZUL, 1);
		Aluno beatriz = new Aluno(2L, "Beatriz Lima", "beatriz@gmail.com", "(21) 97777-0003",
				LocalDate.of(2000, 11, 25), LocalDate.of(2025, 6, 15), 61.0, true, Faixa.BRANCA, 2);
		Aluno carlos = new Aluno(3L, "Carlos Pereira", "carlos@gmail.com", "(21) 96666-0004", LocalDate.of(1988, 7, 2),
				LocalDate.of(2023, 9, 10), 94.3, false, Faixa.ROXA, 4);

		alunoService.incluir(anderson);
		alunoService.incluir(beatriz);
		alunoService.incluir(carlos);

		carregarPresencas(anderson, 1L, LocalDate.of(2026, 1, 5), 58);
		carregarPresencas(beatriz, 100L, LocalDate.of(2026, 8, 11), 5);

		Campeonato copaRio = new Campeonato(1L, "Copa Rio de Jiu-Jitsu", "Rio de Janeiro", LocalDate.of(2026, 5, 17));
		campeonatoService.incluir(copaRio);

		Conquista ouroAnderson = new Conquista(1L, "Adulto Azul Pena", Medalha.OURO, copaRio);
		Conquista prataBeatriz = new Conquista(2L, "Adulto Branca Leve", Medalha.PRATA, copaRio);
		Conquista bronzeCarlos = new Conquista(3L, "Adulto Roxa Pesado", Medalha.BRONZE, copaRio);

		anderson.adicionarConquista(ouroAnderson);
		beatriz.adicionarConquista(prataBeatriz);
		carlos.adicionarConquista(bronzeCarlos);

		conquistaService.incluir(ouroAnderson);
		conquistaService.incluir(prataBeatriz);
		conquistaService.incluir(bronzeCarlos);

		Graduacao azulBeatriz = new Graduacao(1L, Faixa.AZUL, 0, LocalDate.of(2026, 8, 10));
		beatriz.adicionarGraduacao(azulBeatriz);
		graduacaoService.incluir(azulBeatriz);

		System.out.println("\nDados carregados: " + alunoService.obterLista().size() + " alunos, "
				+ instrutorService.obterLista().size() + " instrutor(es), " + presencaService.obterLista().size()
				+ " presenças, " + conquistaService.obterLista().size() + " conquistas, "
				+ graduacaoService.obterLista().size() + " graduação(ões).");
	}

	private void carregarPresencas(Aluno aluno, long idInicial, LocalDate dataInicial, int quantidade) {

		for (int i = 0; i < quantidade; i++) {
			Presenca presenca = new Presenca(idInicial + i, dataInicial.plusDays(i), i % 2 == 0 ? "Gi" : "No-Gi");
			aluno.adicionarPresenca(presenca);
			presencaService.incluir(presenca);
		}
	}

	private void demonstrarHerancaEPolimorfismo() {

		System.out.println("\n--- Herança e polimorfismo (List<Pessoa>) ---");

		List<Pessoa> pessoas = List.of(instrutorService.obterPorId(1L), alunoService.obterPorId(1L),
				alunoService.obterPorId(2L));
		pessoas.forEach(System.out::println);
	}

	private void demonstrarCrud() {

		System.out.println("\n--- CRUD através da camada de serviço ---");

		Aluno diego = new Aluno(4L, "Diego Ramos", "diego@gmail.com", "(21) 95555-0005", LocalDate.of(1999, 1, 30),
				LocalDate.of(2026, 8, 1), 77.0, true, Faixa.BRANCA, 0);
		alunoService.incluir(diego);
		System.out.println("Incluído:  " + alunoService.obterPorId(4L));

		diego.setTelefone("(21) 94444-0006");
		alunoService.alterar(diego);
		System.out.println("Alterado:  " + alunoService.obterPorId(4L));

		alunoService.excluir(4L);
		System.out.println("Excluído o aluno 4. Lista atual:");
		alunoService.obterLista().forEach(aluno -> System.out.println("  " + aluno));
	}

	private void demonstrarConsultasComStreams() {

		System.out.println("\n--- Consultas com Collections, lambdas e Streams ---");

		System.out.println("Alunos ativos (filtragem):");
		alunoService.obterAtivos().forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println("Busca por nome contendo 'sou' (busca):");
		alunoService.buscarPorNome("sou").forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println("Alunos da faixa AZUL (filtragem):");
		alunoService.obterPorFaixa(Faixa.AZUL).forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println(
				"Nomes em ordem alfabética (transformação + ordenação): " + alunoService.obterNomesOrdenados());

		System.out.println("Alunos por frequência (ordenação):");
		alunoService.ordenarPorFrequencia().forEach(aluno -> System.out
				.println("  " + aluno.getNome() + " -> " + aluno.getPresencas().size() + " presença(s)"));

		System.out.println("Presenças entre 01/08/2026 e 15/08/2026 (filtragem por período): "
				+ presencaService.obterPorPeriodo(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 15)).size());

		System.out.println("Quadro de medalhas (agrupamento): " + conquistaService.obterQuadroDeMedalhas());

		System.out.println("Pontos para graduação (presenças + medalhas desde a última graduação):");
		alunoService.obterLista()
				.forEach(aluno -> System.out.println("  " + aluno.getNome() + " (" + aluno.getFaixa() + ", mínimo "
						+ aluno.getFaixa().getPresencasMinimasPorGrau() + ") -> "
						+ alunoService.calcularPontosDesdeUltimaGraduacao(aluno) + " ponto(s)"));

		System.out.println("Alunos aptos a novo grau/faixa:");
		alunoService.obterAptosParaGraduacao().forEach(aluno -> System.out.println("  " + aluno.getNome()));
	}

	private void demonstrarTratamentoDeExcecoes() {

		System.out.println("\n--- Tratamento de situações excepcionais ---");

		try {
			Aluno duplicado = new Aluno(1L, "Outro Anderson", "outro@gmail.com", "(21) 90000-0000",
					LocalDate.of(1990, 1, 1), LocalDate.of(2026, 8, 1), 80.0, true, Faixa.BRANCA, 0);
			alunoService.incluir(duplicado);
		} catch (IdentificadorDuplicadoException e) {
			System.out.println("Identificador duplicado: " + e.getMessage());
		}

		try {
			alunoService.obterPorId(999L);
		} catch (RecursoNaoEncontradoException e) {
			System.out.println("Recurso não encontrado: " + e.getMessage());
		}

		try {
			alunoService.incluir(null);
		} catch (IllegalArgumentException e) {
			System.out.println("Dados inválidos: " + e.getMessage());
		}

		try {
			alunoService.buscarPorNome("  ");
		} catch (IllegalArgumentException e) {
			System.out.println("Dados inválidos: " + e.getMessage());
		}
	}
}

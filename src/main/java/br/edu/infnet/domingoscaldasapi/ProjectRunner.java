package br.edu.infnet.domingoscaldasapi;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.service.AlunoService;
import br.edu.infnet.domingoscaldasapi.service.CampeonatoService;
import br.edu.infnet.domingoscaldasapi.service.ConquistaService;
import br.edu.infnet.domingoscaldasapi.service.GraduacaoService;
import br.edu.infnet.domingoscaldasapi.service.InstrutorService;
import br.edu.infnet.domingoscaldasapi.service.PresencaService;

/**
 * Rotina de inicialização: carrega os dados de demonstração através da camada
 * de serviço (agora persistidos no banco via repositories) e exercita o
 * modelo, o CRUD, as consultas e o tratamento de exceções.
 * Pode ser desativada com app.runner.habilitado=false.
 */
@Component
public class ProjectRunner implements CommandLineRunner {

	private final AlunoService alunoService;
	private final InstrutorService instrutorService;
	private final PresencaService presencaService;
	private final CampeonatoService campeonatoService;
	private final ConquistaService conquistaService;
	private final GraduacaoService graduacaoService;
	private final boolean habilitado;

	public ProjectRunner(AlunoService alunoService, InstrutorService instrutorService,
			PresencaService presencaService, CampeonatoService campeonatoService,
			ConquistaService conquistaService, GraduacaoService graduacaoService,
			@Value("${app.runner.habilitado:true}") boolean habilitado) {
		this.alunoService = alunoService;
		this.instrutorService = instrutorService;
		this.presencaService = presencaService;
		this.campeonatoService = campeonatoService;
		this.conquistaService = conquistaService;
		this.graduacaoService = graduacaoService;
		this.habilitado = habilitado;
	}

	@Override
	public void run(String... args) throws Exception {

		if (!habilitado) {
			return;
		}

		System.out.println("===== BJJ School - carga de demonstração (Controller -> Service -> Repository -> H2) =====");

		carregarDados();
		demonstrarHerancaEPolimorfismo();
		demonstrarCrud();
		demonstrarConsultas();
		demonstrarTratamentoDeExcecoes();
	}

	private void carregarDados() {

		instrutorService.incluir(new Instrutor(null, "Domingos Caldas", "domingojr@bjjschool.com.br",
				"(21) 99999-0001", Faixa.PRETA, 3, "CBJJ-484817", true));

		Aluno anderson = alunoService.incluir(new Aluno(null, "Anderson Souza", "anderson@gmail.com",
				"(21) 98888-0002", LocalDate.of(1995, 3, 10), LocalDate.of(2024, 2, 1), 82.5, true, Faixa.AZUL, 1));
		Aluno beatriz = alunoService.incluir(new Aluno(null, "Beatriz Lima", "beatriz@gmail.com",
				"(21) 97777-0003", LocalDate.of(2000, 11, 25), LocalDate.of(2025, 6, 15), 61.0, true, Faixa.BRANCA, 2));
		alunoService.incluir(new Aluno(null, "Carlos Pereira", "carlos@gmail.com", "(21) 96666-0004",
				LocalDate.of(1988, 7, 2), LocalDate.of(2023, 9, 10), 94.3, false, Faixa.ROXA, 4));

		carregarPresencas(anderson, LocalDate.of(2026, 1, 5), 58);
		carregarPresencas(beatriz, LocalDate.of(2026, 8, 11), 5);

		Campeonato copaRio = campeonatoService.incluir(
				new Campeonato(null, "Copa Rio de Jiu-Jitsu", "Rio de Janeiro", LocalDate.of(2026, 5, 17)));

		alunoService.registrarConquista(anderson.getId(), copaRio.getId(),
				new Conquista(null, "Adulto Azul Pena", Medalha.OURO, null));
		alunoService.registrarConquista(beatriz.getId(), copaRio.getId(),
				new Conquista(null, "Adulto Branca Leve", Medalha.PRATA, null));
		alunoService.registrarConquista(3L, copaRio.getId(),
				new Conquista(null, "Adulto Roxa Pesado", Medalha.BRONZE, null));

		alunoService.registrarGraduacao(beatriz.getId(), new Graduacao(null, Faixa.AZUL, 0, LocalDate.of(2026, 8, 10)));

		System.out.println("\nDados persistidos no H2: " + alunoService.obterLista().size() + " alunos, "
				+ instrutorService.obterLista().size() + " instrutor(es), " + presencaService.obterLista().size()
				+ " presenças, " + conquistaService.obterLista().size() + " conquistas, "
				+ graduacaoService.obterLista().size() + " graduação(ões).");
	}

	private void carregarPresencas(Aluno aluno, LocalDate dataInicial, int quantidade) {

		for (int i = 0; i < quantidade; i++) {
			alunoService.registrarPresenca(aluno.getId(),
					new Presenca(null, dataInicial.plusDays(i), i % 2 == 0 ? "Gi" : "No-Gi"));
		}
	}

	private void demonstrarHerancaEPolimorfismo() {

		System.out.println("\n--- Herança e polimorfismo (List<Pessoa>) ---");

		List<Pessoa> pessoas = List.of(instrutorService.obterLista().get(0), alunoService.obterPorId(2L),
				alunoService.obterPorId(3L));
		pessoas.forEach(System.out::println);
	}

	private void demonstrarCrud() {

		System.out.println("\n--- CRUD através de Service -> Repository ---");

		Aluno diego = alunoService.incluir(new Aluno(null, "Diego Ramos", "diego@gmail.com", "(21) 95555-0005",
				LocalDate.of(1999, 1, 30), LocalDate.of(2026, 8, 1), 77.0, true, Faixa.BRANCA, 0));
		System.out.println("Incluído (id gerado pelo banco):  " + alunoService.obterPorId(diego.getId()));

		diego.setTelefone("(21) 94444-0006");
		alunoService.alterar(diego.getId(), diego);
		System.out.println("Alterado:  " + alunoService.obterPorId(diego.getId()));

		alunoService.excluir(diego.getId());
		System.out.println("Excluído o aluno " + diego.getId() + ". Lista atual:");
		alunoService.obterLista().forEach(aluno -> System.out.println("  " + aluno));
	}

	private void demonstrarConsultas() {

		System.out.println("\n--- Consultas derivadas (findBy...) e Streams ---");

		System.out.println("Alunos ativos (findByAtivoTrue):");
		alunoService.obterAtivos().forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println("Busca por nome contendo 'sou' (findByNomeContainingIgnoreCase):");
		alunoService.buscarPorNome("sou").forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println("Alunos da faixa AZUL (findByFaixa):");
		alunoService.obterPorFaixa(Faixa.AZUL).forEach(aluno -> System.out.println("  " + aluno.getNome()));

		System.out.println(
				"Nomes em ordem alfabética (findAllByOrderByNomeAsc): " + alunoService.obterNomesOrdenados());

		System.out.println("Alunos por frequência (Streams):");
		alunoService.ordenarPorFrequencia().forEach(aluno -> System.out
				.println("  " + aluno.getNome() + " -> " + aluno.getPresencas().size() + " presença(s)"));

		System.out.println("Presenças entre 01/08/2026 e 15/08/2026 (findByDataBetween): "
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

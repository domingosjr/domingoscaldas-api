package br.edu.infnet.domingoscaldasapi.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import br.edu.infnet.domingoscaldasapi.domain.Aluno;
import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Faixa;
import br.edu.infnet.domingoscaldasapi.domain.Graduacao;

/**
 * Regras de negócio dos alunos, incluindo as consultas com Streams e o
 * critério de aptidão para graduação (frequência + conquistas em campeonatos).
 */
public class AlunoService extends BaseService<Aluno> {

	private static final int PONTOS_MEDALHA_OURO = 10;
	private static final int PONTOS_MEDALHA_PRATA = 5;
	private static final int PONTOS_MEDALHA_BRONZE = 3;

	public List<Aluno> obterAtivos() {
		return obterLista().stream()
				.filter(Aluno::isAtivo)
				.toList();
	}

	public List<Aluno> buscarPorNome(String termo) {

		if (termo == null || termo.isBlank()) {
			throw new IllegalArgumentException("O termo de busca não pode ser vazio!");
		}

		return obterLista().stream()
				.filter(aluno -> aluno.getNome().toLowerCase().contains(termo.toLowerCase()))
				.toList();
	}

	public List<Aluno> obterPorFaixa(Faixa faixa) {

		if (faixa == null) {
			throw new IllegalArgumentException("A faixa não pode ser nula!");
		}

		return obterLista().stream()
				.filter(aluno -> aluno.getFaixa() == faixa)
				.toList();
	}

	public List<String> obterNomesOrdenados() {
		return obterLista().stream()
				.map(Aluno::getNome)
				.sorted()
				.toList();
	}

	public List<Aluno> ordenarPorFrequencia() {
		return obterLista().stream()
				.sorted(Comparator.comparingInt((Aluno aluno) -> aluno.getPresencas().size()).reversed())
				.toList();
	}

	/**
	 * Um aluno ativo está apto a um novo grau (ou faixa) quando os pontos
	 * acumulados desde a última graduação atingem o mínimo exigido pela faixa
	 * atual. Cada presença vale 1 ponto; medalhas em campeonatos aceleram a
	 * graduação (ouro=10, prata=5, bronze=3).
	 */
	public List<Aluno> obterAptosParaGraduacao() {
		return obterAtivos().stream()
				.filter(aluno -> calcularPontosDesdeUltimaGraduacao(aluno) >= aluno.getFaixa()
						.getPresencasMinimasPorGrau())
				.toList();
	}

	public long calcularPontosDesdeUltimaGraduacao(Aluno aluno) {

		if (aluno == null) {
			throw new IllegalArgumentException("O aluno não pode ser nulo!");
		}

		LocalDate ultimaGraduacao = aluno.getGraduacoes().stream()
				.map(Graduacao::getData)
				.max(LocalDate::compareTo)
				.orElse(null);

		long pontosPresencas = aluno.getPresencas().stream()
				.filter(presenca -> ultimaGraduacao == null || presenca.getData().isAfter(ultimaGraduacao))
				.count();

		long pontosConquistas = aluno.getConquistas().stream()
				.filter(conquista -> conquista.getCampeonato() != null
						&& (ultimaGraduacao == null || conquista.getCampeonato().getData().isAfter(ultimaGraduacao)))
				.mapToLong(this::calcularPontosDaConquista)
				.sum();

		return pontosPresencas + pontosConquistas;
	}

	private long calcularPontosDaConquista(Conquista conquista) {
		return switch (conquista.getMedalha()) {
			case OURO -> PONTOS_MEDALHA_OURO;
			case PRATA -> PONTOS_MEDALHA_PRATA;
			case BRONZE -> PONTOS_MEDALHA_BRONZE;
		};
	}
}

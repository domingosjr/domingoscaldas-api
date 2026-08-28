package br.edu.infnet.domingoscaldasapi.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Aluno;
import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Faixa;
import br.edu.infnet.domingoscaldasapi.domain.Graduacao;
import br.edu.infnet.domingoscaldasapi.domain.Presenca;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.AlunoRepository;

/**
 * Regras de negócio dos alunos: CRUD via repository, consultas derivadas,
 * registro de presenças/conquistas/graduações e o critério de aptidão para
 * graduação (frequência + conquistas em campeonatos).
 */
@Service
public class AlunoService {

	private static final int PONTOS_MEDALHA_OURO = 10;
	private static final int PONTOS_MEDALHA_PRATA = 5;
	private static final int PONTOS_MEDALHA_BRONZE = 3;

	private final AlunoRepository alunoRepository;
	private final PresencaService presencaService;
	private final ConquistaService conquistaService;
	private final GraduacaoService graduacaoService;
	private final CampeonatoService campeonatoService;

	public AlunoService(AlunoRepository alunoRepository, PresencaService presencaService,
			ConquistaService conquistaService, GraduacaoService graduacaoService,
			CampeonatoService campeonatoService) {
		this.alunoRepository = alunoRepository;
		this.presencaService = presencaService;
		this.conquistaService = conquistaService;
		this.graduacaoService = graduacaoService;
		this.campeonatoService = campeonatoService;
	}

	public List<Aluno> obterLista() {
		return alunoRepository.findAll();
	}

	public Aluno obterPorId(Long id) {
		return alunoRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Aluno incluir(Aluno aluno) {
		validarObjeto(aluno);

		return alunoRepository.save(aluno);
	}

	/**
	 * O identificador da URL prevalece; os campos são copiados sobre a entidade
	 * existente para não perder os históricos (presenças, conquistas, graduações).
	 */
	public Aluno alterar(Long id, Aluno aluno) {
		validarObjeto(aluno);

		Aluno existente = obterPorId(id);
		existente.setNome(aluno.getNome());
		existente.setEmail(aluno.getEmail());
		existente.setTelefone(aluno.getTelefone());
		existente.setDataNascimento(aluno.getDataNascimento());
		existente.setDataMatricula(aluno.getDataMatricula());
		existente.setPeso(aluno.getPeso());
		existente.setAtivo(aluno.isAtivo());
		existente.setFaixa(aluno.getFaixa());
		existente.setGraus(aluno.getGraus());

		return alunoRepository.save(existente);
	}

	public void excluir(Long id) {
		Aluno aluno = obterPorId(id);

		alunoRepository.delete(aluno);
	}

	/**
	 * Registra uma presença para o aluno: vincula ao aluno e persiste.
	 */
	public Presenca registrarPresenca(Long alunoId, Presenca presenca) {
		Aluno aluno = obterPorId(alunoId);
		aluno.adicionarPresenca(presenca);

		return presencaService.incluir(presenca);
	}

	/**
	 * Registra uma conquista do aluno em um campeonato existente.
	 */
	public Conquista registrarConquista(Long alunoId, Long campeonatoId, Conquista conquista) {
		Aluno aluno = obterPorId(alunoId);
		conquista.setCampeonato(campeonatoService.obterPorId(campeonatoId));
		aluno.adicionarConquista(conquista);

		return conquistaService.incluir(conquista);
	}

	/**
	 * Registra uma graduação: atualiza faixa/graus do aluno e guarda o histórico.
	 */
	public Graduacao registrarGraduacao(Long alunoId, Graduacao graduacao) {
		Aluno aluno = obterPorId(alunoId);
		aluno.adicionarGraduacao(graduacao);

		Graduacao registrada = graduacaoService.incluir(graduacao);
		alunoRepository.save(aluno);

		return registrada;
	}

	public List<Aluno> obterAtivos() {
		return alunoRepository.findByAtivoTrue();
	}

	public List<Aluno> buscarPorNome(String termo) {
		validarTermo(termo);

		return alunoRepository.findByNomeContainingIgnoreCase(termo);
	}

	public List<Aluno> obterPorFaixa(Faixa faixa) {

		if (faixa == null) {
			throw new IllegalArgumentException("A faixa não pode ser nula!");
		}

		return alunoRepository.findByFaixa(faixa);
	}

	public List<String> obterNomesOrdenados() {
		return alunoRepository.findAllByOrderByNomeAsc().stream()
				.map(Aluno::getNome)
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
		validarObjeto(aluno);

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

	private void validarObjeto(Aluno aluno) {

		if (aluno == null) {
			throw new IllegalArgumentException("O aluno não pode ser nulo!");
		}
	}

	private void validarTermo(String termo) {

		if (termo == null || termo.isBlank()) {
			throw new IllegalArgumentException("O termo de busca não pode ser vazio!");
		}
	}
}

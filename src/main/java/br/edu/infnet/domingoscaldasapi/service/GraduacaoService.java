package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Graduacao;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.GraduacaoRepository;

/**
 * Regras de negócio do histórico de graduações.
 */
@Service
public class GraduacaoService {

	private final GraduacaoRepository graduacaoRepository;

	public GraduacaoService(GraduacaoRepository graduacaoRepository) {
		this.graduacaoRepository = graduacaoRepository;
	}

	public List<Graduacao> obterLista() {
		return graduacaoRepository.findAll();
	}

	public Graduacao obterPorId(Long id) {
		return graduacaoRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Graduacao incluir(Graduacao graduacao) {

		if (graduacao == null) {
			throw new IllegalArgumentException("A graduação não pode ser nula!");
		}

		return graduacaoRepository.save(graduacao);
	}

	/**
	 * Atualiza a graduação existente no lugar, preservando o vínculo com o aluno.
	 */
	public Graduacao alterar(Long id, Graduacao graduacao) {
		Graduacao existente = obterPorId(id);
		existente.setFaixa(graduacao.getFaixa());
		existente.setGrau(graduacao.getGrau());
		existente.setData(graduacao.getData());

		return graduacaoRepository.save(existente);
	}

	public void excluir(Long id) {
		Graduacao graduacao = obterPorId(id);

		graduacaoRepository.delete(graduacao);
	}
}

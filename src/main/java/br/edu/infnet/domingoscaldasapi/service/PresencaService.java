package br.edu.infnet.domingoscaldasapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Presenca;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.PresencaRepository;

/**
 * Regras de negócio das presenças (frequência nos treinos).
 */
@Service
public class PresencaService {

	private final PresencaRepository presencaRepository;

	public PresencaService(PresencaRepository presencaRepository) {
		this.presencaRepository = presencaRepository;
	}

	public List<Presenca> obterLista() {
		return presencaRepository.findAll();
	}

	public Presenca obterPorId(Long id) {
		return presencaRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Presenca incluir(Presenca presenca) {

		if (presenca == null) {
			throw new IllegalArgumentException("A presença não pode ser nula!");
		}

		return presencaRepository.save(presenca);
	}

	/**
	 * Atualiza a presença existente no lugar, preservando o vínculo com o aluno.
	 */
	public Presenca alterar(Long id, Presenca presenca) {
		Presenca existente = obterPorId(id);
		existente.setData(presenca.getData());
		existente.setTipoTreino(presenca.getTipoTreino());

		return presencaRepository.save(existente);
	}

	public void excluir(Long id) {
		Presenca presenca = obterPorId(id);

		presencaRepository.delete(presenca);
	}

	public List<Presenca> obterPorPeriodo(LocalDate inicio, LocalDate fim) {

		if (inicio == null || fim == null) {
			throw new IllegalArgumentException("O período não pode ser nulo!");
		}

		return presencaRepository.findByDataBetween(inicio, fim);
	}
}

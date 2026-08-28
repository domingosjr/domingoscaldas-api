package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.ConquistaRepository;

/**
 * Regras de negócio das conquistas em campeonatos.
 */
@Service
public class ConquistaService {

	private final ConquistaRepository conquistaRepository;

	public ConquistaService(ConquistaRepository conquistaRepository) {
		this.conquistaRepository = conquistaRepository;
	}

	public List<Conquista> obterLista() {
		return conquistaRepository.findAll();
	}

	public Conquista obterPorId(Long id) {
		return conquistaRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Conquista incluir(Conquista conquista) {

		if (conquista == null) {
			throw new IllegalArgumentException("A conquista não pode ser nula!");
		}

		return conquistaRepository.save(conquista);
	}

	/**
	 * Atualiza a conquista existente no lugar, preservando o vínculo com o
	 * aluno; o campeonato só muda se informado.
	 */
	public Conquista alterar(Long id, Conquista conquista) {
		Conquista existente = obterPorId(id);
		existente.setCategoria(conquista.getCategoria());
		existente.setMedalha(conquista.getMedalha());

		if (conquista.getCampeonato() != null) {
			existente.setCampeonato(conquista.getCampeonato());
		}

		return conquistaRepository.save(existente);
	}

	public void excluir(Long id) {
		Conquista conquista = obterPorId(id);

		conquistaRepository.delete(conquista);
	}

	public List<Conquista> obterPorMedalha(Medalha medalha) {

		if (medalha == null) {
			throw new IllegalArgumentException("A medalha não pode ser nula!");
		}

		return conquistaRepository.findByMedalha(medalha);
	}

	/**
	 * Quadro de medalhas da escola: total de conquistas por tipo de medalha.
	 */
	public Map<Medalha, Long> obterQuadroDeMedalhas() {
		return obterLista().stream()
				.collect(Collectors.groupingBy(Conquista::getMedalha, Collectors.counting()));
	}
}

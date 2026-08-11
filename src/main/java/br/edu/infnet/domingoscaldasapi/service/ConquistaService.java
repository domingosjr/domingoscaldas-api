package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;

/**
 * Regras de negócio das conquistas em campeonatos.
 */
public class ConquistaService extends BaseService<Conquista> {

	public List<Conquista> obterPorMedalha(Medalha medalha) {

		if (medalha == null) {
			throw new IllegalArgumentException("A medalha não pode ser nula!");
		}

		return obterLista().stream()
				.filter(conquista -> conquista.getMedalha() == medalha)
				.toList();
	}

	/**
	 * Quadro de medalhas da escola: total de conquistas por tipo de medalha.
	 */
	public Map<Medalha, Long> obterQuadroDeMedalhas() {
		return obterLista().stream()
				.collect(Collectors.groupingBy(Conquista::getMedalha, Collectors.counting()));
	}
}

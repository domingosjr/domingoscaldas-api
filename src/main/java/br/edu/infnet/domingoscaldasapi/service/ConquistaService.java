package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;

/**
 * Regras de negócio das conquistas em campeonatos.
 */
@Service
public class ConquistaService extends BaseService<Conquista> {

	/**
	 * Atualiza a conquista existente no lugar (a mesma instância está na lista
	 * do aluno), preservando o vínculo com o aluno; o campeonato só muda se informado.
	 */
	@Override
	public Conquista alterar(Conquista conquista) {
		Conquista atual = obterPorId(conquista.getId());
		atual.setCategoria(conquista.getCategoria());
		atual.setMedalha(conquista.getMedalha());

		if (conquista.getCampeonato() != null) {
			atual.setCampeonato(conquista.getCampeonato());
		}

		return super.alterar(atual);
	}

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

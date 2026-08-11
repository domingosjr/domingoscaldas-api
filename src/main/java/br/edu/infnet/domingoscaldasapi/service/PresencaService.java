package br.edu.infnet.domingoscaldasapi.service;

import java.time.LocalDate;
import java.util.List;

import br.edu.infnet.domingoscaldasapi.domain.Presenca;

/**
 * Regras de negócio das presenças (frequência nos treinos).
 */
public class PresencaService extends BaseService<Presenca> {

	public List<Presenca> obterPorPeriodo(LocalDate inicio, LocalDate fim) {

		if (inicio == null || fim == null) {
			throw new IllegalArgumentException("O período não pode ser nulo!");
		}

		return obterLista().stream()
				.filter(presenca -> !presenca.getData().isBefore(inicio) && !presenca.getData().isAfter(fim))
				.toList();
	}
}

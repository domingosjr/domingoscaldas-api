package br.edu.infnet.domingoscaldasapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Presenca;

/**
 * Regras de negócio das presenças (frequência nos treinos).
 */
@Service
public class PresencaService extends BaseService<Presenca> {

	/**
	 * Atualiza a presença existente no lugar (a mesma instância está na lista do
	 * aluno), preservando o vínculo com o aluno.
	 */
	@Override
	public Presenca alterar(Presenca presenca) {
		Presenca atual = obterPorId(presenca.getId());
		atual.setData(presenca.getData());
		atual.setTipoTreino(presenca.getTipoTreino());

		return super.alterar(atual);
	}

	public List<Presenca> obterPorPeriodo(LocalDate inicio, LocalDate fim) {

		if (inicio == null || fim == null) {
			throw new IllegalArgumentException("O período não pode ser nulo!");
		}

		return obterLista().stream()
				.filter(presenca -> !presenca.getData().isBefore(inicio) && !presenca.getData().isAfter(fim))
				.toList();
	}
}

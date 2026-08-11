package br.edu.infnet.domingoscaldasapi.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.edu.infnet.domingoscaldasapi.domain.Identificavel;
import br.edu.infnet.domingoscaldasapi.exception.IdentificadorDuplicadoException;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;

/**
 * Serviço genérico com o CRUD comum a todas as entidades do domínio.
 * O Map simula temporariamente o armazenamento dos dados (substituído por
 * banco de dados na etapa 4) e fica encapsulado na camada de serviço.
 */
public abstract class BaseService<T extends Identificavel> {

	private final Map<Long, T> dados = new LinkedHashMap<>();

	public void incluir(T objeto) {
		validarObjeto(objeto);

		if (dados.containsKey(objeto.getId())) {
			throw new IdentificadorDuplicadoException(
					"Já existe um objeto com esse identificador: " + objeto.getId());
		}

		dados.put(objeto.getId(), objeto);
	}

	public void alterar(T objeto) {
		validarObjeto(objeto);
		verificarExistencia(objeto.getId());

		dados.put(objeto.getId(), objeto);
	}

	public void excluir(Long id) {
		verificarExistencia(id);

		dados.remove(id);
	}

	public T obterPorId(Long id) {
		verificarExistencia(id);

		return dados.get(id);
	}

	public List<T> obterLista() {
		return new ArrayList<>(dados.values());
	}

	private void validarObjeto(T objeto) {

		if (objeto == null) {
			throw new IllegalArgumentException("O objeto não pode ser nulo!");
		}

		if (objeto.getId() == null) {
			throw new IllegalArgumentException("O identificador não pode ser nulo!");
		}
	}

	private void verificarExistencia(Long id) {

		if (id == null) {
			throw new IllegalArgumentException("O identificador não pode ser nulo!");
		}

		if (!dados.containsKey(id)) {
			throw new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id);
		}
	}
}

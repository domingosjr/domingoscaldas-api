package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Campeonato;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.CampeonatoRepository;

/**
 * Regras de negócio dos campeonatos.
 */
@Service
public class CampeonatoService {

	private final CampeonatoRepository campeonatoRepository;

	public CampeonatoService(CampeonatoRepository campeonatoRepository) {
		this.campeonatoRepository = campeonatoRepository;
	}

	public List<Campeonato> obterLista() {
		return campeonatoRepository.findAll();
	}

	public Campeonato obterPorId(Long id) {
		return campeonatoRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Campeonato incluir(Campeonato campeonato) {

		if (campeonato == null) {
			throw new IllegalArgumentException("O campeonato não pode ser nulo!");
		}

		return campeonatoRepository.save(campeonato);
	}

	public Campeonato alterar(Long id, Campeonato campeonato) {
		Campeonato existente = obterPorId(id);
		existente.setNome(campeonato.getNome());
		existente.setCidade(campeonato.getCidade());
		existente.setData(campeonato.getData());

		return campeonatoRepository.save(existente);
	}

	public void excluir(Long id) {
		Campeonato campeonato = obterPorId(id);

		campeonatoRepository.delete(campeonato);
	}
}

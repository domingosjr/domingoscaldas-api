package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Instrutor;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.InstrutorRepository;

/**
 * Regras de negócio dos instrutores.
 */
@Service
public class InstrutorService {

	private final InstrutorRepository instrutorRepository;

	public InstrutorService(InstrutorRepository instrutorRepository) {
		this.instrutorRepository = instrutorRepository;
	}

	public List<Instrutor> obterLista() {
		return instrutorRepository.findAll();
	}

	public Instrutor obterPorId(Long id) {
		return instrutorRepository.findById(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Nenhum recurso encontrado para esse identificador: " + id));
	}

	public Instrutor incluir(Instrutor instrutor) {

		if (instrutor == null) {
			throw new IllegalArgumentException("O instrutor não pode ser nulo!");
		}

		return instrutorRepository.save(instrutor);
	}

	public Instrutor alterar(Long id, Instrutor instrutor) {
		Instrutor existente = obterPorId(id);
		existente.setNome(instrutor.getNome());
		existente.setEmail(instrutor.getEmail());
		existente.setTelefone(instrutor.getTelefone());
		existente.setFaixa(instrutor.getFaixa());
		existente.setGraus(instrutor.getGraus());
		existente.setRegistroFederacao(instrutor.getRegistroFederacao());
		existente.setAtivo(instrutor.isAtivo());

		return instrutorRepository.save(existente);
	}

	public void excluir(Long id) {
		Instrutor instrutor = obterPorId(id);

		instrutorRepository.delete(instrutor);
	}
}

package br.edu.infnet.domingoscaldasapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.client.EnderecoViaCep;
import br.edu.infnet.domingoscaldasapi.client.ViaCepClient;
import br.edu.infnet.domingoscaldasapi.domain.Campeonato;
import br.edu.infnet.domingoscaldasapi.exception.RecursoNaoEncontradoException;
import br.edu.infnet.domingoscaldasapi.repository.CampeonatoRepository;
import feign.FeignException;

/**
 * Regras de negócio dos campeonatos. Quando o CEP é informado, a cidade é
 * preenchida automaticamente consultando a API externa ViaCEP (OpenFeign).
 */
@Service
public class CampeonatoService {

	private final CampeonatoRepository campeonatoRepository;
	private final ViaCepClient viaCepClient;

	public CampeonatoService(CampeonatoRepository campeonatoRepository, ViaCepClient viaCepClient) {
		this.campeonatoRepository = campeonatoRepository;
		this.viaCepClient = viaCepClient;
	}

	/**
	 * Consulta o endereço de um CEP na API externa ViaCEP.
	 */
	public EnderecoViaCep consultarEnderecoPorCep(String cep) {

		if (cep == null || !cep.matches("\\d{8}")) {
			throw new IllegalArgumentException("O CEP deve possuir exatamente 8 dígitos numéricos!");
		}

		EnderecoViaCep endereco;

		try {
			endereco = viaCepClient.consultarCep(cep);
		} catch (FeignException e) {
			throw new IllegalArgumentException("Não foi possível consultar o CEP " + cep + " no ViaCEP!");
		}

		if (endereco == null || endereco.isCepInexistente()) {
			throw new RecursoNaoEncontradoException("Nenhum endereço encontrado para o CEP: " + cep);
		}

		return endereco;
	}

	private void preencherCidadePeloCep(Campeonato campeonato) {

		if (campeonato.getCep() == null || campeonato.getCep().isBlank()) {
			return;
		}

		EnderecoViaCep endereco = consultarEnderecoPorCep(campeonato.getCep());
		campeonato.setCidade(endereco.getLocalidade() + " - " + endereco.getUf());
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

		preencherCidadePeloCep(campeonato);

		return campeonatoRepository.save(campeonato);
	}

	public Campeonato alterar(Long id, Campeonato campeonato) {
		preencherCidadePeloCep(campeonato);

		Campeonato existente = obterPorId(id);
		existente.setNome(campeonato.getNome());
		existente.setCidade(campeonato.getCidade());
		existente.setCep(campeonato.getCep());
		existente.setData(campeonato.getData());

		return campeonatoRepository.save(existente);
	}

	public void excluir(Long id) {
		Campeonato campeonato = obterPorId(id);

		campeonatoRepository.delete(campeonato);
	}
}

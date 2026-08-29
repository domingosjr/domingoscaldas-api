package br.edu.infnet.domingoscaldasapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente OpenFeign para a API pública ViaCEP — a aplicação atua como
 * consumidora de outro serviço (desafio adicional da disciplina).
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

	@GetMapping("/{cep}/json")
	EnderecoViaCep consultarCep(@PathVariable String cep);
}

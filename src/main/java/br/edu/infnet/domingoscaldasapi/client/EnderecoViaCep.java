package br.edu.infnet.domingoscaldasapi.client;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Resposta da API pública ViaCEP (https://viacep.com.br).
 * O campo "erro" só vem preenchido quando o CEP não existe.
 */
public class EnderecoViaCep {

	private String cep;
	private String logradouro;
	private String bairro;
	private String localidade;
	private String uf;
	private Boolean erro;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@JsonIgnore
	public Boolean getErro() {
		return erro;
	}

	public void setErro(Boolean erro) {
		this.erro = erro;
	}

	@JsonIgnore
	public boolean isCepInexistente() {
		return Boolean.TRUE.equals(erro);
	}

	@Override
	public String toString() {
		return String.format("EnderecoViaCep {cep='%s', logradouro='%s', bairro='%s', localidade='%s', uf='%s'}",
				cep, logradouro, bairro, localidade, uf);
	}
}

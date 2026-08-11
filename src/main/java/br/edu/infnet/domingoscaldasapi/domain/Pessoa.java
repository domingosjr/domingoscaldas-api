package br.edu.infnet.domingoscaldasapi.domain;

/**
 * Classe base abstrata com os dados comuns às pessoas da escola de Jiu-Jitsu.
 */
public abstract class Pessoa implements Identificavel {

	private Long id;
	private String nome;
	private String email;
	private String telefone;

	public Pessoa() {
	}

	public Pessoa(Long id, String nome, String email, String telefone) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return String.format("id=%d, nome='%s', email='%s', telefone='%s'", id, nome, email, telefone);
	}
}

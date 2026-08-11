package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

/**
 * Campeonato de Jiu-Jitsu em que os alunos competem.
 */
public class Campeonato implements Identificavel {

	private Long id;
	private String nome;
	private String cidade;
	private LocalDate data;

	public Campeonato() {
	}

	public Campeonato(Long id, String nome, String cidade, LocalDate data) {
		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.data = data;
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

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("Campeonato {id=%d, nome='%s', cidade='%s', data=%s}", id, nome, cidade, data);
	}
}

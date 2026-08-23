package br.edu.infnet.domingoscaldasapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Conquista (título) de um aluno em um campeonato: medalha e categoria disputada.
 * Também é considerada nos critérios de graduação.
 */
public class Conquista implements Identificavel {

	private Long id;
	private String categoria;
	private Medalha medalha;
	private Campeonato campeonato;
	@JsonBackReference("aluno-conquistas")
	private Aluno aluno;

	public Conquista() {
	}

	public Conquista(Long id, String categoria, Medalha medalha, Campeonato campeonato) {
		this.id = id;
		this.categoria = categoria;
		this.medalha = medalha;
		this.campeonato = campeonato;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Medalha getMedalha() {
		return medalha;
	}

	public void setMedalha(Medalha medalha) {
		this.medalha = medalha;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Override
	public String toString() {
		String nomeCampeonato = campeonato != null ? campeonato.getNome() : "sem campeonato";
		String nomeAluno = aluno != null ? aluno.getNome() : "sem aluno";

		return String.format("Conquista {id=%d, categoria='%s', medalha=%s, campeonato=%s, aluno=%s}",
				id, categoria, medalha, nomeCampeonato, nomeAluno);
	}
}

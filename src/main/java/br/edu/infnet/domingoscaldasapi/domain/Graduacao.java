package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Registro histórico de uma graduação do aluno: nova faixa ou novo grau na
 * faixa atual (grau 0 representa a troca de faixa).
 */
public class Graduacao implements Identificavel {

	private Long id;
	private Faixa faixa;
	private int grau;
	private LocalDate data;
	@JsonBackReference("aluno-graduacoes")
	private Aluno aluno;

	public Graduacao() {
	}

	public Graduacao(Long id, Faixa faixa, int grau, LocalDate data) {
		this.id = id;
		this.faixa = faixa;
		this.grau = grau;
		this.data = data;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Faixa getFaixa() {
		return faixa;
	}

	public void setFaixa(Faixa faixa) {
		this.faixa = faixa;
	}

	public int getGrau() {
		return grau;
	}

	public void setGrau(int grau) {
		this.grau = grau;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Override
	public String toString() {
		String nomeAluno = aluno != null ? aluno.getNome() : "sem aluno";

		return String.format("Graduacao {id=%d, faixa=%s, grau=%d, data=%s, aluno=%s}", id, faixa, grau, data, nomeAluno);
	}
}

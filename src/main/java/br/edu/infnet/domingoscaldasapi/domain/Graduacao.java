package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Registro histórico de uma graduação do aluno: nova faixa ou novo grau na
 * faixa atual (grau 0 representa a troca de faixa).
 */
@Entity
@Table(name = "graduacoes")
public class Graduacao implements Identificavel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "A faixa deve ser informada")
	@Enumerated(EnumType.STRING)
	private Faixa faixa;

	@PositiveOrZero(message = "O grau não pode ser negativo")
	private int grau;

	@NotNull(message = "A data da graduação deve ser informada")
	private LocalDate data;

	@JsonBackReference("aluno-graduacoes")
	@ManyToOne
	@JoinColumn(name = "aluno_id")
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

package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Campeonato de Jiu-Jitsu em que os alunos competem.
 */
@Entity
@Table(name = "campeonatos")
public class Campeonato implements Identificavel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome deve ser informado")
	@Size(max = 120, message = "O nome deve possuir no máximo 120 caracteres")
	private String nome;

	@Size(max = 80, message = "A cidade deve possuir no máximo 80 caracteres")
	private String cidade;

	@NotNull(message = "A data deve ser informada")
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

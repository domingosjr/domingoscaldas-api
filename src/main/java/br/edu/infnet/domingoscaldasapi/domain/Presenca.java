package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Presença de um aluno em um treino. A frequência é o principal critério
 * para a graduação de graus e faixas.
 */
public class Presenca implements Identificavel {

	private Long id;
	private LocalDate data;
	private String tipoTreino;
	@JsonBackReference("aluno-presencas")
	private Aluno aluno;

	public Presenca() {
	}

	public Presenca(Long id, LocalDate data, String tipoTreino) {
		this.id = id;
		this.data = data;
		this.tipoTreino = tipoTreino;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getTipoTreino() {
		return tipoTreino;
	}

	public void setTipoTreino(String tipoTreino) {
		this.tipoTreino = tipoTreino;
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

		return String.format("Presenca {id=%d, data=%s, tipoTreino='%s', aluno=%s}", id, data, tipoTreino, nomeAluno);
	}
}

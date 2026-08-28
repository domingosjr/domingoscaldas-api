package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Presença de um aluno em um treino. A frequência é o principal critério
 * para a graduação de graus e faixas.
 */
@Entity
@Table(name = "presencas")
public class Presenca implements Identificavel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "A data da presença deve ser informada")
	private LocalDate data;

	@NotBlank(message = "O tipo de treino deve ser informado")
	@Size(max = 30, message = "O tipo de treino deve possuir no máximo 30 caracteres")
	private String tipoTreino;

	@JsonBackReference("aluno-presencas")
	@ManyToOne
	@JoinColumn(name = "aluno_id")
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

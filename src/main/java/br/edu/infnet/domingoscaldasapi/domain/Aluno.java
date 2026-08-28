package br.edu.infnet.domingoscaldasapi.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Aluno da escola de Jiu-Jitsu. Mantém a faixa/graus atuais e os históricos
 * de presenças (frequência), conquistas em campeonatos e graduações.
 */
@Entity
@Table(name = "alunos")
public class Aluno extends Pessoa {

	private LocalDate dataNascimento;
	private LocalDate dataMatricula;

	@PositiveOrZero(message = "O peso não pode ser negativo")
	private double peso;

	private boolean ativo;

	@NotNull(message = "A faixa deve ser informada")
	@Enumerated(EnumType.STRING)
	private Faixa faixa;

	@PositiveOrZero(message = "Os graus não podem ser negativos")
	private int graus;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonManagedReference("aluno-presencas")
	@OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
	private List<Presenca> presencas = new ArrayList<>();

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonManagedReference("aluno-conquistas")
	@OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
	private List<Conquista> conquistas = new ArrayList<>();

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonManagedReference("aluno-graduacoes")
	@OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
	private List<Graduacao> graduacoes = new ArrayList<>();

	public Aluno() {
	}

	public Aluno(Long id, String nome, String email, String telefone, LocalDate dataNascimento,
			LocalDate dataMatricula, double peso, boolean ativo, Faixa faixa, int graus) {
		super(id, nome, email, telefone);
		this.dataNascimento = dataNascimento;
		this.dataMatricula = dataMatricula;
		this.peso = peso;
		this.ativo = ativo;
		this.faixa = faixa;
		this.graus = graus;
	}

	public void adicionarPresenca(Presenca presenca) {

		if (presenca == null) {
			throw new IllegalArgumentException("A presença não pode ser nula!");
		}

		presencas.add(presenca);
		presenca.setAluno(this);
	}

	public void adicionarConquista(Conquista conquista) {

		if (conquista == null) {
			throw new IllegalArgumentException("A conquista não pode ser nula!");
		}

		conquistas.add(conquista);
		conquista.setAluno(this);
	}

	/**
	 * Registra uma graduação no histórico e atualiza a faixa e os graus atuais do aluno.
	 */
	public void adicionarGraduacao(Graduacao graduacao) {

		if (graduacao == null) {
			throw new IllegalArgumentException("A graduação não pode ser nula!");
		}

		graduacoes.add(graduacao);
		graduacao.setAluno(this);

		this.faixa = graduacao.getFaixa();
		this.graus = graduacao.getGrau();
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public LocalDate getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(LocalDate dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Faixa getFaixa() {
		return faixa;
	}

	public void setFaixa(Faixa faixa) {
		this.faixa = faixa;
	}

	public int getGraus() {
		return graus;
	}

	public void setGraus(int graus) {
		this.graus = graus;
	}

	public List<Presenca> getPresencas() {
		return Collections.unmodifiableList(presencas);
	}

	public List<Conquista> getConquistas() {
		return Collections.unmodifiableList(conquistas);
	}

	public List<Graduacao> getGraduacoes() {
		return Collections.unmodifiableList(graduacoes);
	}

	@Override
	public String toString() {
		return String.format(
				"Aluno {%s, dataNascimento=%s, dataMatricula=%s, peso=%.1f, ativo=%s, faixa=%s, graus=%d, "
						+ "quantidadePresencas=%d, quantidadeConquistas=%d, quantidadeGraduacoes=%d}",
				super.toString(), dataNascimento, dataMatricula, peso, ativo ? "Sim" : "Não", faixa, graus,
				presencas.size(), conquistas.size(), graduacoes.size());
	}
}

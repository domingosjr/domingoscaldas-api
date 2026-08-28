package br.edu.infnet.domingoscaldasapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe base abstrata com os dados comuns às pessoas da escola de Jiu-Jitsu.
 * Herança mapeada com a estratégia JOINED: tabela "pessoas" com os dados
 * comuns e uma tabela por subclasse (alunos, instrutores).
 */
@Entity
@Table(name = "pessoas")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa implements Identificavel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome deve ser informado")
	@Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres")
	private String nome;

	@NotBlank(message = "O e-mail deve ser informado")
	@Email(message = "O e-mail deve ser válido")
	private String email;

	@Size(max = 20, message = "O telefone deve possuir no máximo 20 caracteres")
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

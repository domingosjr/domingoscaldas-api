package br.edu.infnet.domingoscaldasapi.domain;

/**
 * Instrutor responsável pelos treinos e pelas graduações dos alunos.
 */
public class Instrutor extends Pessoa {

	private Faixa faixa;
	private int graus;
	private String registroFederacao;
	private boolean ativo;

	public Instrutor() {
	}

	public Instrutor(Long id, String nome, String email, String telefone, Faixa faixa, int graus,
			String registroFederacao, boolean ativo) {
		super(id, nome, email, telefone);
		this.faixa = faixa;
		this.graus = graus;
		this.registroFederacao = registroFederacao;
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

	public String getRegistroFederacao() {
		return registroFederacao;
	}

	public void setRegistroFederacao(String registroFederacao) {
		this.registroFederacao = registroFederacao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		return String.format("Instrutor {%s, faixa=%s, graus=%d, registroFederacao='%s', ativo=%s}",
				super.toString(), faixa, graus, registroFederacao, ativo ? "Sim" : "Não");
	}
}

package br.edu.infnet.domingoscaldasapi.domain;

/**
 * Faixas do Jiu-Jitsu (adulto), na ordem de progressão.
 * Cada faixa define a quantidade mínima de presenças para receber um novo grau.
 */
public enum Faixa {

	BRANCA(40),
	AZUL(60),
	ROXA(80),
	MARROM(100),
	PRETA(150);

	private final int presencasMinimasPorGrau;

	Faixa(int presencasMinimasPorGrau) {
		this.presencasMinimasPorGrau = presencasMinimasPorGrau;
	}

	public int getPresencasMinimasPorGrau() {
		return presencasMinimasPorGrau;
	}

	public Faixa proxima() {
		int posicao = ordinal();

		return posicao < values().length - 1 ? values()[posicao + 1] : this;
	}
}

package br.edu.infnet.domingoscaldasapi.exception;

/**
 * Lançada quando se tenta incluir um objeto cujo identificador já existe.
 */
public class IdentificadorDuplicadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdentificadorDuplicadoException(String mensagem) {
		super(mensagem);
	}
}

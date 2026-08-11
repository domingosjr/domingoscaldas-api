package br.edu.infnet.domingoscaldasapi.exception;

/**
 * Lançada quando nenhum objeto é encontrado para o identificador informado.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecursoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}

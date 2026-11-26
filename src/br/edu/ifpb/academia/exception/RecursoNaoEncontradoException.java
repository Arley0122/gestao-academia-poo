package br.edu.ifpb.academia.exception;

/**
 * Exceção lançada quando um recurso solicitado por identificador não é encontrado.
 */
public class RecursoNaoEncontradoException extends DominioException {

	public RecursoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}

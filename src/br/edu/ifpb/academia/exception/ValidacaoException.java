package br.edu.ifpb.academia.exception;

/**
 * Exceção lançada quando dados de entrada violam restrições estruturais ou de formato.
 */
public class ValidacaoException extends DominioException {

	public ValidacaoException(String mensagem) {
		super(mensagem);
	}
}

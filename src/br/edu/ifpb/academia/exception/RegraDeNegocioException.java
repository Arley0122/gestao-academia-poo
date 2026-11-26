package br.edu.ifpb.academia.exception;

/**
 * Exceção lançada quando uma regra de negócio do domínio é violada.
 */
public class RegraDeNegocioException extends DominioException {

	public RegraDeNegocioException(String mensagem) {
		super(mensagem);
	}
}

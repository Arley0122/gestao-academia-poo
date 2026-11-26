package br.edu.ifpb.academia.exception;

/**
 * Exceção base para erros de domínio e aplicação do sistema da academia.
 */
public abstract class DominioException extends RuntimeException {

	protected DominioException(String mensagem) {
		super(mensagem);
	}
}

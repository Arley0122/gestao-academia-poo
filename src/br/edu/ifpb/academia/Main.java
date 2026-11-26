package br.edu.ifpb.academia;

import br.edu.ifpb.academia.config.SistemaAcademiaContexto;
import br.edu.ifpb.academia.test.VerificadorFluxos;
import br.edu.ifpb.academia.ui.MenuConsole;

/**
 * Ponto de entrada da aplicação POO de Gestão de Academia.
 * Use o argumento {@code --test} para executar apenas a suíte automatizada de verificação.
 */
public class Main {

	public static void main(String[] args) {
		if (args != null && args.length > 0 && "--test".equalsIgnoreCase(args[0])) {
			VerificadorFluxos.executarTodos();
			return;
		}
		SistemaAcademiaContexto contexto = new SistemaAcademiaContexto();
		new MenuConsole(contexto).iniciar();
	}
}

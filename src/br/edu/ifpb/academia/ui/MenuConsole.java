package br.edu.ifpb.academia.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.config.SistemaAcademiaContexto;
import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.exception.DominioException;
import br.edu.ifpb.academia.model.ResultadoAcesso;
import br.edu.ifpb.academia.test.VerificadorFluxos;

/**
 * Interface de linha de comando para operar todos os casos de uso do sistema POO.
 */
public class MenuConsole {

	private final SistemaAcademiaContexto ctx;
	private final LeitorConsole leitor;

	public MenuConsole(SistemaAcademiaContexto ctx) {
		this.ctx = ctx;
		this.leitor = new LeitorConsole();
	}

	public void iniciar() {
		boolean executando = true;
		while (executando) {
			exibirMenuPrincipal();
			int opcao = leitor.lerOpcao("Opção");
			try {
				switch (opcao) {
					case 1 -> menuAlunos();
					case 2 -> menuUnidades();
					case 3 -> menuPlanos();
					case 4 -> menuContratos();
					case 5 -> menuCobrancas();
					case 6 -> menuTreinos();
					case 7 -> menuTreinosAtivos();
					case 8 -> verificarAcessoCatraca();
					case 9 -> VerificadorFluxos.executarTodos();
					case 0 -> executando = false;
					default -> System.out.println("Opção inválida.");
				}
			} catch (DominioException ex) {
				System.out.println("[ERRO DE DOMÍNIO] " + ex.getMessage());
			} catch (Exception ex) {
				System.out.println("[ERRO INESPERADO] " + ex.getMessage());
			}
		}
		System.out.println("Sistema encerrado.");
	}

	private void exibirMenuPrincipal() {
		System.out.println("\n=== SISTEMA DE GESTÃO DE ACADEMIA (POO) ===");
		System.out.println("1. Gerenciar Alunos");
		System.out.println("2. Gerenciar Unidades");
		System.out.println("3. Gerenciar Planos");
		System.out.println("4. Gerenciar Contratos");
		System.out.println("5. Gerenciar Cobranças e Notificações");
		System.out.println("6. Gerenciar Programas, Dias e Exercícios");
		System.out.println("7. Consultar Treinos Ativos e Histórico");
		System.out.println("8. Verificar Acesso na Catraca");
		System.out.println("9. Executar Suíte Completa de Cenários Automatizados");
		System.out.println("0. Sair");
	}

	private void menuAlunos() {
		System.out.println("\n--- ALUNOS ---");
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getAlunoService().listar());
			case 2 -> System.out.println(ctx.getAlunoService().buscar(leitor.lerLong("ID do aluno")));
			case 3 -> {
				String nome = leitor.lerTexto("Nome");
				String cpf = leitor.lerTexto("CPF");
				String telefone = leitor.lerTexto("Telefone");
				String email = leitor.lerTexto("E-mail");
				LocalDate nascimento = leitor.lerData("Data de nascimento");
				BigDecimal peso = leitor.lerDecimal("Peso (kg)");
				BigDecimal altura = leitor.lerDecimal("Altura (m)");
				NivelExperiencia nivel = leitor.lerEnum("Nível de experiência", NivelExperiencia.class);
				Integer frequencia = leitor.lerInteiro("Frequência semanal (3 a 6)");
				StatusAluno status = leitor.lerEnumOpcional("Status inicial", StatusAluno.class);
				System.out.println("Criado: " + ctx.getAlunoService().criar(nome, cpf, telefone, email,
						nascimento, peso, altura, nivel, frequencia, status));
			}
			case 4 -> {
				Long id = leitor.lerLong("ID do aluno");
				String nome = leitor.lerTexto("Novo nome");
				String cpf = leitor.lerTexto("Novo CPF");
				String telefone = leitor.lerTexto("Novo telefone");
				String email = leitor.lerTexto("Novo e-mail");
				LocalDate nascimento = leitor.lerData("Nova data de nascimento");
				BigDecimal peso = leitor.lerDecimal("Novo peso (kg)");
				BigDecimal altura = leitor.lerDecimal("Nova altura (m)");
				NivelExperiencia nivel = leitor.lerEnum("Novo nível de experiência", NivelExperiencia.class);
				Integer frequencia = leitor.lerInteiro("Nova frequência semanal (3 a 6)");
				StatusAluno status = leitor.lerEnumOpcional("Novo status", StatusAluno.class);
				System.out.println("Atualizado: " + ctx.getAlunoService().atualizar(id, nome, cpf, telefone, email,
						nascimento, peso, altura, nivel, frequencia, status));
			}
			case 5 -> {
				ctx.getAlunoService().excluir(leitor.lerLong("ID do aluno"));
				System.out.println("Aluno removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuUnidades() {
		System.out.println("\n--- UNIDADES ---");
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getUnidadeService().listar());
			case 2 -> System.out.println(ctx.getUnidadeService().buscar(leitor.lerLong("ID da unidade")));
			case 3 -> System.out.println("Criada: " + ctx.getUnidadeService().criar(
					leitor.lerTexto("Nome"),
					leitor.lerTexto("Endereço"),
					leitor.lerTexto("CNPJ"),
					leitor.lerTexto("Telefone")));
			case 4 -> System.out.println("Atualizada: " + ctx.getUnidadeService().atualizar(
					leitor.lerLong("ID da unidade"),
					leitor.lerTexto("Novo nome"),
					leitor.lerTexto("Novo endereço"),
					leitor.lerTexto("Novo CNPJ"),
					leitor.lerTexto("Novo telefone")));
			case 5 -> {
				ctx.getUnidadeService().excluir(leitor.lerLong("ID da unidade"));
				System.out.println("Unidade removida.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuPlanos() {
		System.out.println("\n--- PLANOS ---");
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getPlanoService().listar());
			case 2 -> System.out.println(ctx.getPlanoService().buscar(leitor.lerLong("ID do plano")));
			case 3 -> System.out.println("Criado: " + ctx.getPlanoService().criar(
					leitor.lerEnum("Tipo do plano", TipoPlano.class),
					leitor.lerDecimal("Valor"),
					leitor.lerInteiro("Duração em dias"),
					leitor.lerDecimal("Percentual de desconto"),
					leitor.lerTexto("Descrição")));
			case 4 -> System.out.println("Atualizado: " + ctx.getPlanoService().atualizar(
					leitor.lerLong("ID do plano"),
					leitor.lerEnum("Tipo do plano", TipoPlano.class),
					leitor.lerDecimal("Valor"),
					leitor.lerInteiro("Duração em dias"),
					leitor.lerDecimal("Percentual de desconto"),
					leitor.lerTexto("Descrição")));
			case 5 -> {
				ctx.getPlanoService().excluir(leitor.lerLong("ID do plano"));
				System.out.println("Plano removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuContratos() {
		System.out.println("\n--- CONTRATOS ---");
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Cancelar | 6. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getContratoService().listar());
			case 2 -> System.out.println(ctx.getContratoService().buscar(leitor.lerLong("ID do contrato")));
			case 3 -> System.out.println("Criado: " + ctx.getContratoService().criar(
					leitor.lerLong("ID do aluno"),
					leitor.lerLong("ID da unidade"),
					leitor.lerLong("ID do plano"),
					leitor.lerData("Data de início"),
					leitor.lerEnumOpcional("Status", StatusContrato.class)));
			case 4 -> System.out.println("Atualizado: " + ctx.getContratoService().atualizar(
					leitor.lerLong("ID do contrato"),
					leitor.lerLong("ID do aluno"),
					leitor.lerLong("ID da unidade"),
					leitor.lerLong("ID do plano"),
					leitor.lerData("Data de início"),
					leitor.lerEnumOpcional("Status", StatusContrato.class)));
			case 5 -> System.out.println("Cancelado: " + ctx.getContratoService().cancelar(
					leitor.lerLong("ID do contrato")));
			case 6 -> {
				ctx.getContratoService().excluir(leitor.lerLong("ID do contrato"));
				System.out.println("Contrato removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuCobrancas() {
		System.out.println("\n--- COBRANÇAS ---");
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Pagar | 6. Excluir | 7. Rotina Notificações/Atrasos | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getCobrancaService().listar());
			case 2 -> System.out.println(ctx.getCobrancaService().buscar(leitor.lerLong("ID da cobrança")));
			case 3 -> System.out.println("Criada: " + ctx.getCobrancaService().criar(
					leitor.lerLong("ID do contrato"),
					leitor.lerData("Data de vencimento"),
					leitor.lerDataOpcional("Data de pagamento"),
					leitor.lerEnumOpcional("Forma de pagamento", FormaPagamento.class),
					leitor.lerEnumOpcional("Status", StatusCobranca.class)));
			case 4 -> System.out.println("Atualizada: " + ctx.getCobrancaService().atualizar(
					leitor.lerLong("ID da cobrança"),
					leitor.lerLong("ID do contrato"),
					leitor.lerData("Data de vencimento"),
					leitor.lerDataOpcional("Data de pagamento"),
					leitor.lerEnumOpcional("Forma de pagamento", FormaPagamento.class),
					leitor.lerEnumOpcional("Status", StatusCobranca.class)));
			case 5 -> System.out.println("Paga: " + ctx.getCobrancaService().pagar(
					leitor.lerLong("ID da cobrança"),
					leitor.lerData("Data de pagamento"),
					leitor.lerEnum("Forma de pagamento", FormaPagamento.class)));
			case 6 -> {
				ctx.getCobrancaService().excluir(leitor.lerLong("ID da cobrança"));
				System.out.println("Cobrança removida.");
			}
			case 7 -> {
				ctx.getCobrancaService().notificacoesAutomaticas();
				System.out.println("Rotina de notificações e atualização de atrasos executada.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuTreinos() {
		System.out.println("\n--- PROGRAMAS, DIAS E EXERCÍCIOS ---");
		System.out.println("1. Programas de Treino | 2. Dias de Treino | 3. Exercícios | 4. Exercícios do Dia | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> submenuProgramas();
			case 2 -> submenuDias();
			case 3 -> submenuExercicios();
			case 4 -> submenuDiaExercicios();
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void submenuProgramas() {
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getProgramaTreinoService().listar());
			case 2 -> System.out.println(ctx.getProgramaTreinoService().buscar(leitor.lerLong("ID do programa")));
			case 3 -> System.out.println("Criado: " + ctx.getProgramaTreinoService().criar(
					leitor.lerEnum("Nível de experiência", NivelExperiencia.class),
					leitor.lerInteiro("Frequência semanal (3 a 6)"),
					leitor.lerEnum("Tipo estrutural", TipoEstrutural.class),
					leitor.lerTexto("Descrição")));
			case 4 -> System.out.println("Atualizado: " + ctx.getProgramaTreinoService().atualizar(
					leitor.lerLong("ID do programa"),
					leitor.lerEnum("Nível de experiência", NivelExperiencia.class),
					leitor.lerInteiro("Frequência semanal (3 a 6)"),
					leitor.lerEnum("Tipo estrutural", TipoEstrutural.class),
					leitor.lerTexto("Descrição")));
			case 5 -> {
				ctx.getProgramaTreinoService().excluir(leitor.lerLong("ID do programa"));
				System.out.println("Programa removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void submenuDias() {
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getDiaTreinoService().listar());
			case 2 -> System.out.println(ctx.getDiaTreinoService().buscar(leitor.lerLong("ID do dia")));
			case 3 -> System.out.println("Criado: " + ctx.getDiaTreinoService().criar(
					leitor.lerLong("ID do programa"),
					leitor.lerTexto("Nome do dia"),
					leitor.lerInteiro("Ordem")));
			case 4 -> System.out.println("Atualizado: " + ctx.getDiaTreinoService().atualizar(
					leitor.lerLong("ID do dia"),
					leitor.lerLong("ID do programa"),
					leitor.lerTexto("Nome do dia"),
					leitor.lerInteiro("Ordem")));
			case 5 -> {
				ctx.getDiaTreinoService().excluir(leitor.lerLong("ID do dia"));
				System.out.println("Dia de treino removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void submenuExercicios() {
		System.out.println("1. Listar | 2. Buscar | 3. Criar | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getExercicioService().listar());
			case 2 -> System.out.println(ctx.getExercicioService().buscar(leitor.lerLong("ID do exercício")));
			case 3 -> System.out.println("Criado: " + ctx.getExercicioService().criar(
					leitor.lerTexto("Nome"),
					leitor.lerTexto("Musculatura alvo")));
			case 4 -> System.out.println("Atualizado: " + ctx.getExercicioService().atualizar(
					leitor.lerLong("ID do exercício"),
					leitor.lerTexto("Nome"),
					leitor.lerTexto("Musculatura alvo")));
			case 5 -> {
				ctx.getExercicioService().excluir(leitor.lerLong("ID do exercício"));
				System.out.println("Exercício removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void submenuDiaExercicios() {
		System.out.println("1. Listar | 2. Buscar | 3. Vincular | 4. Atualizar | 5. Excluir | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getDiaTreinoExercicioService().listar());
			case 2 -> System.out.println(ctx.getDiaTreinoExercicioService().buscar(leitor.lerLong("ID do vínculo")));
			case 3 -> System.out.println("Criado: " + ctx.getDiaTreinoExercicioService().criar(
					leitor.lerLong("ID do dia de treino"),
					leitor.lerLong("ID do exercício"),
					leitor.lerInteiro("Séries"),
					leitor.lerInteiro("Repetições"),
					leitor.lerInteiro("Ordem")));
			case 4 -> System.out.println("Atualizado: " + ctx.getDiaTreinoExercicioService().atualizar(
					leitor.lerLong("ID do vínculo"),
					leitor.lerLong("ID do dia de treino"),
					leitor.lerLong("ID do exercício"),
					leitor.lerInteiro("Séries"),
					leitor.lerInteiro("Repetições"),
					leitor.lerInteiro("Ordem")));
			case 5 -> {
				ctx.getDiaTreinoExercicioService().excluir(leitor.lerLong("ID do vínculo"));
				System.out.println("Vínculo removido.");
			}
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void menuTreinosAtivos() {
		System.out.println("\n--- TREINOS ATIVOS ---");
		System.out.println("1. Listar todos | 2. Buscar por ID | 3. Histórico por Aluno | 0. Voltar");
		switch (leitor.lerOpcao("Opção")) {
			case 1 -> imprimirLista(ctx.getTreinoAtivoService().listar());
			case 2 -> System.out.println(ctx.getTreinoAtivoService().buscar(leitor.lerLong("ID do treino ativo")));
			case 3 -> imprimirLista(ctx.getTreinoAtivoService().listarPorAluno(leitor.lerLong("ID do aluno")));
			case 0 -> { }
			default -> System.out.println("Opção inválida.");
		}
	}

	private void verificarAcessoCatraca() {
		ResultadoAcesso resultado = ctx.getAcessoService().verificar(leitor.lerLong("ID do aluno"));
		System.out.println(resultado);
	}

	private void imprimirLista(List<?> itens) {
		if (itens.isEmpty()) {
			System.out.println("(Nenhum registro encontrado)");
			return;
		}
		itens.forEach(System.out::println);
	}
}

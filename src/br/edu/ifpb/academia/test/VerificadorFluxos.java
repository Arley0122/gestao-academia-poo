package br.edu.ifpb.academia.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.academia.config.SistemaAcademiaContexto;
import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.ValidacaoException;
import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.model.Cobranca;
import br.edu.ifpb.academia.model.Contrato;
import br.edu.ifpb.academia.model.DiaTreino;
import br.edu.ifpb.academia.model.DiaTreinoExercicio;
import br.edu.ifpb.academia.model.Exercicio;
import br.edu.ifpb.academia.model.Plano;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.model.ResultadoAcesso;
import br.edu.ifpb.academia.model.TreinoAtivo;
import br.edu.ifpb.academia.model.Unidade;
import br.edu.ifpb.academia.service.NotificacaoService;

/**
 * Suíte determinística de testes em Java puro validando toda a lógica preservada da API.
 */
public final class VerificadorFluxos {

	private VerificadorFluxos() {
	}

	private static class NotificadorEspiao implements NotificacaoService {
		final List<Cobranca> vencimentosProximos = new ArrayList<>();
		final List<Cobranca> cobrancasAtrasadas = new ArrayList<>();

		@Override
		public void vencimentoProximo(Cobranca cobranca) {
			vencimentosProximos.add(cobranca);
		}

		@Override
		public void cobrancaAtrasada(Cobranca cobranca) {
			cobrancasAtrasadas.add(cobranca);
		}
	}

	public static void executarTodos() {
		System.out.println("=== INICIANDO VERIFICAÇÃO DOS FLUXOS POO ===");
		testarCargaInicial();
		testarValidacoesEUnicidade();
		testarAlunoESincronizacaoTreino();
		testarContratoCobrancaCatracaECancelamento();
		testarCarenciaExataENotificacoesAutomaticas();
		testarEstruturaProgramaTreino();
		testarRecursosNaoEncontrados();
		System.out.println("=== TODOS OS 7 BLOCOS DE TESTE PASSARAM COM SUCESSO ===");
	}

	private static void testarCargaInicial() {
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto();
		List<Plano> planos = ctx.getPlanoService().listar();
		List<ProgramaTreino> programas = ctx.getProgramaTreinoService().listar();
		verificar(planos.size() == 5, "Devem existir 5 planos iniciais.");
		verificar(programas.size() == 12, "Devem existir 12 programas de treino iniciais.");
		ProgramaTreino p = ctx.getProgramaTreinoService().buscarPorPerfil(NivelExperiencia.INICIANTE, 3);
		verificar(p.getTipoEstrutural() == TipoEstrutural.FULLBODY, "Frequência 3 deve ser FULLBODY.");
		System.out.println("[OK] Cenário 1: Carga inicial (5 planos e 12 programas).");
	}

	private static void testarValidacoesEUnicidade() {
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto();
		LocalDate nasc = LocalDate.of(2000, 5, 10);

		verificarExcecao(ValidacaoException.class, () -> ctx.getAlunoService().criar(
				" ", "111", "83999990000", "joao@email.com", nasc,
				new BigDecimal("75.0"), new BigDecimal("1.75"), NivelExperiencia.INICIANTE, 3, null));

		verificarExcecao(ValidacaoException.class, () -> ctx.getAlunoService().criar(
				"João", "111", "83999990000", "email-invalido", nasc,
				new BigDecimal("75.0"), new BigDecimal("1.75"), NivelExperiencia.INICIANTE, 3, null));

		verificarExcecao(ValidacaoException.class, () -> ctx.getAlunoService().criar(
				"João", "111", "83999990000", "joao@email.com", LocalDate.now(),
				new BigDecimal("75.0"), new BigDecimal("1.75"), NivelExperiencia.INICIANTE, 3, null));

		verificarExcecao(ValidacaoException.class, () -> ctx.getAlunoService().criar(
				"João", "111", "83999990000", "joao@email.com", nasc,
				new BigDecimal("75.0"), new BigDecimal("1.75"), NivelExperiencia.INICIANTE, 2, null));

		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getPlanoService().criar(
				TipoPlano.TRIMESTRAL, new BigDecimal("300.00"), 90, BigDecimal.ZERO, "Sem desconto"));

		ctx.getUnidadeService().criar("Unidade Centro", "Rua A, 100", "12.345.678/0001-90", "8332221111");
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getUnidadeService().criar(
				"Outra", "Rua B", "12.345.678/0001-90", "8332221112"));

		ctx.getExercicioService().criar("Supino Reto", "Peitoral");
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getExercicioService().criar(
				"Supino Reto", "Peito"));

		System.out.println("[OK] Cenário 2: Validações estruturais e regras de unicidade/desconto.");
	}

	private static void testarAlunoESincronizacaoTreino() {
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto();
		LocalDate nasc = LocalDate.of(1998, 3, 20);

		Aluno aluno = ctx.getAlunoService().criar(
				"Maria Silva", "222.222.222-22", "83988887777", "maria@email.com",
				nasc, new BigDecimal("62.0"), new BigDecimal("1.65"), NivelExperiencia.INICIANTE, 3, null);
		verificar(aluno.isAtivo(), "Aluno deve iniciar ATIVO por padrão.");

		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getAlunoService().criar(
				"Outro", "222.222.222-22", "83911112222", "outro@email.com",
				nasc, new BigDecimal("70.0"), new BigDecimal("1.70"), NivelExperiencia.INICIANTE, 3, null));

		List<TreinoAtivo> treinos = ctx.getTreinoAtivoService().listarPorAluno(aluno.getId());
		verificar(treinos.size() == 1, "Deve gerar 1 treino ativo na criação do aluno.");
		verificar(treinos.get(0).getProgramaTreino().getTipoEstrutural() == TipoEstrutural.FULLBODY,
				"Treino inicial deve ser FULLBODY.");

		// Atualiza peso sem mudar perfil de treino -> mantém mesmo treino ativo
		ctx.getAlunoService().atualizar(aluno.getId(), "Maria Silva", "222.222.222-22", "83988887777",
				"maria@email.com", nasc, new BigDecimal("60.5"), new BigDecimal("1.65"),
				NivelExperiencia.INICIANTE, 3, StatusAluno.ATIVO);
		verificar(ctx.getTreinoAtivoService().listarPorAluno(aluno.getId()).size() == 1,
				"Não deve criar novo treino se nível e frequência não mudarem.");

		// Muda nível e frequência -> encerra treino anterior e cria novo
		ctx.getAlunoService().atualizar(aluno.getId(), "Maria Silva", "222.222.222-22", "83988887777",
				"maria@email.com", nasc, new BigDecimal("60.5"), new BigDecimal("1.65"),
				NivelExperiencia.INTERMEDIARIO, 4, StatusAluno.ATIVO);
		List<TreinoAtivo> historico = ctx.getTreinoAtivoService().listarPorAluno(aluno.getId());
		verificar(historico.size() == 2, "Deve existir histórico com 2 treinos após mudança de perfil.");
		long ativos = historico.stream().filter(t -> t.getStatus() == StatusTreinoAtivo.ATIVO).count();
		long encerrados = historico.stream().filter(t -> t.getStatus() == StatusTreinoAtivo.ENCERRADO).count();
		verificar(ativos == 1 && encerrados == 1, "Deve haver 1 treino ATIVO e 1 ENCERRADO.");
		System.out.println("[OK] Cenário 3: Cadastro de aluno e sincronização automática de treino.");
	}

	private static void testarContratoCobrancaCatracaECancelamento() {
		NotificadorEspiao espiao = new NotificadorEspiao();
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto(espiao);

		Unidade unidade = ctx.getUnidadeService().criar("Unidade Manaíra", "Av. Edson Ramalho, 50",
				"98.765.432/0001-10", "8333334444");
		Plano mensal = ctx.getPlanoService().listar().stream()
				.filter(p -> p.getTipo() == TipoPlano.MENSAL)
				.findFirst().orElseThrow();
		Aluno aluno = ctx.getAlunoService().criar(
				"Carlos Souza", "333.333.333-33", "83977776666", "carlos@email.com",
				LocalDate.of(1995, 8, 12), new BigDecimal("82.0"), new BigDecimal("1.80"),
				NivelExperiencia.AVANCADO, 6, StatusAluno.INATIVO);

		// Aluno inativo na catraca lança RegraDeNegocioException
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getAcessoService().verificar(aluno.getId()));

		// Ativa aluno sem contrato -> acesso negado via ResultadoAcesso
		ctx.getAlunoService().ativar(aluno);
		ResultadoAcesso semContrato = ctx.getAcessoService().verificar(aluno.getId());
		verificar(!semContrato.liberado() && "Aluno sem contrato ativo.".equals(semContrato.motivo()),
				"Sem contrato deve negar acesso sem lançar exceção.");

		// Cria contrato com início há 3 dias (fora da carência de 2 dias) -> reativa aluno e gera cobrança vencida
		LocalDate inicio = LocalDate.now().minusDays(3);
		Contrato contrato = ctx.getContratoService().criar(
				aluno.getId(), unidade.getId(), mensal.getId(), inicio, null);
		verificar(contrato.getDataTermino().equals(inicio.plusDays(30)),
				"Data de término deve ser dataInicio + duração do plano.");
		verificar(aluno.isAtivo(), "Criação de contrato deve ativar o aluno.");

		// Bloqueia segundo contrato ativo
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getContratoService().criar(
				aluno.getId(), unidade.getId(), mensal.getId(), LocalDate.now(), null));

		// Catraca deve atualizar cobranças atrasadas, notificar e negar acesso
		ResultadoAcesso atrasado = ctx.getAcessoService().verificar(aluno.getId());
		verificar(!atrasado.liberado() && "Aluno possui cobrança atrasada.".equals(atrasado.motivo()),
				"Cobrança fora da carência deve bloquear acesso.");
		verificar(espiao.cobrancasAtrasadas.size() == 1, "Deve notificar cobrança atrasada.");

		Cobranca cobranca = ctx.getCobrancaService().listar().get(0);
		verificar(cobranca.getStatus() == StatusCobranca.ATRASADO, "Status da cobrança deve ser ATRASADO.");
		verificar(cobranca.getValor().compareTo(mensal.getValor()) == 0, "Valor deve derivar do plano.");

		// Pagar cobrança e tentar pagar novamente
		ctx.getCobrancaService().pagar(cobranca.getId(), LocalDate.now(), FormaPagamento.PIX);
		verificar(cobranca.isPaga(), "Cobrança deve estar PAGA.");
		verificarExcecao(RegraDeNegocioException.class,
				() -> ctx.getCobrancaService().pagar(cobranca.getId(), LocalDate.now(), FormaPagamento.DINHEIRO));

		// Catraca agora libera acesso
		ResultadoAcesso liberado = ctx.getAcessoService().verificar(aluno.getId());
		verificar(liberado.liberado() && "Acesso liberado.".equals(liberado.motivo()),
				"Após pagamento, acesso deve ser liberado.");

		// Cancelamento de contrato inativa aluno e bloqueia novo cancelamento
		ctx.getContratoService().cancelar(contrato.getId());
		verificar(contrato.getStatus() == StatusContrato.CANCELADO, "Contrato deve ficar CANCELADO.");
		verificar(!aluno.isAtivo(), "Aluno deve ficar INATIVO ao cancelar contrato.");
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getContratoService().cancelar(contrato.getId()));
		verificarExcecao(RegraDeNegocioException.class, () -> ctx.getAcessoService().verificar(aluno.getId()));

		// Novo contrato após cancelamento deve funcionar e reativar aluno
		Contrato renovacao = ctx.getContratoService().criar(
				aluno.getId(), unidade.getId(), mensal.getId(), LocalDate.now(), null);
		verificar(renovacao.isAtivo() && aluno.isAtivo(), "Novo contrato deve reativar aluno.");
		System.out.println("[OK] Cenário 4: Contrato, cobrança, catraca, pagamento e cancelamento.");
	}

	private static void testarCarenciaExataENotificacoesAutomaticas() {
		NotificadorEspiao espiao = new NotificadorEspiao();
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto(espiao);

		Unidade unidade = ctx.getUnidadeService().criar("Unidade Sul", "Av. Principal, 10",
				"11.222.333/0001-44", "8331112222");
		Plano diario = ctx.getPlanoService().listar().stream()
				.filter(p -> p.getTipo() == TipoPlano.DIARIO)
				.findFirst().orElseThrow();
		Aluno aluno = ctx.getAlunoService().criar(
				"Ana Costa", "444.444.444-44", "83966665555", "ana@email.com",
				LocalDate.of(1999, 1, 15), new BigDecimal("58.0"), new BigDecimal("1.62"),
				NivelExperiencia.INICIANTE, 5, null);

		// Início exatamente há 2 dias -> dentro do limite de carência (limite = hoje - 2; isBefore é falso)
		Contrato contrato = ctx.getContratoService().criar(
				aluno.getId(), unidade.getId(), diario.getId(), LocalDate.now().minusDays(2), null);

		ResultadoAcesso naCarencia = ctx.getAcessoService().verificar(aluno.getId());
		verificar(naCarencia.liberado(), "No último dia de carência (hoje - 2), acesso ainda deve ser liberado.");
		verificar(espiao.cobrancasAtrasadas.isEmpty(), "Não deve marcar atraso dentro da carência.");

		// Cobrança com vencimento amanhã -> notificacoesAutomaticas deve avisar vencimento próximo
		ctx.getCobrancaService().criar(contrato.getId(), LocalDate.now().plusDays(1), null, null, null);
		ctx.getCobrancaService().notificacoesAutomaticas();
		verificar(espiao.vencimentosProximos.size() == 1, "Deve notificar 1 cobrança com vencimento próximo.");
		System.out.println("[OK] Cenário 5: Carência exata de 2 dias e rotina de notificações automáticas.");
	}

	private static void testarEstruturaProgramaTreino() {
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto();
		ProgramaTreino programa = ctx.getProgramaTreinoService().buscarPorPerfil(NivelExperiencia.AVANCADO, 5);
		DiaTreino diaB = ctx.getDiaTreinoService().criar(programa.getId(), "Treino B - Pull", 2);
		DiaTreino diaA = ctx.getDiaTreinoService().criar(programa.getId(), "Treino A - Push", 1);

		Exercicio supino = ctx.getExercicioService().criar("Supino Inclinado", "Peitoral Superior");
		Exercicio desenvolvimento = ctx.getExercicioService().criar("Desenvolvimento", "Ombros");

		DiaTreinoExercicio item2 = ctx.getDiaTreinoExercicioService().criar(diaA.getId(), desenvolvimento.getId(), 3, 12, 2);
		ctx.getDiaTreinoExercicioService().criar(diaA.getId(), supino.getId(), 4, 10, 1);

		verificar(programa.getDiasTreino().size() == 2, "Programa deve conter 2 dias associados.");
		verificar(programa.getDiasTreino().get(0).equals(diaA), "Dias devem estar ordenados por ordem.");
		verificar(diaA.getExercicios().get(0).getExercicio().equals(supino),
				"Exercícios do dia devem estar ordenados por ordem.");

		ctx.getDiaTreinoExercicioService().excluir(item2.getId());
		verificar(diaA.getExercicios().size() == 1, "Exclusão deve atualizar coleção do dia.");
		ctx.getDiaTreinoService().excluir(diaB.getId());
		verificar(programa.getDiasTreino().size() == 1, "Exclusão de dia deve atualizar coleção do programa.");
		System.out.println("[OK] Cenário 6: Composição e ordenação de Programa, Dia e Exercícios.");
	}

	private static void testarRecursosNaoEncontrados() {
		SistemaAcademiaContexto ctx = new SistemaAcademiaContexto();
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getAlunoService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getUnidadeService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getPlanoService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getContratoService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getCobrancaService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getExercicioService().buscar(999L));
		verificarExcecao(RecursoNaoEncontradoException.class, () -> ctx.getProgramaTreinoService().buscar(999L));
		System.out.println("[OK] Cenário 7: Tratamento de recursos inexistentes.");
	}

	private static void verificar(boolean condicao, String mensagem) {
		if (!condicao) {
			throw new AssertionError("Falha na verificação: " + mensagem);
		}
	}

	private static void verificarExcecao(Class<? extends Throwable> esperada, Runnable acao) {
		try {
			acao.run();
		} catch (Throwable ex) {
			if (esperada.isInstance(ex)) {
				return;
			}
			throw new AssertionError("Esperava " + esperada.getSimpleName() + ", mas lançou "
					+ ex.getClass().getSimpleName() + ": " + ex.getMessage(), ex);
		}
		throw new AssertionError("Esperava lançamento de " + esperada.getSimpleName() + ", mas nenhuma exceção ocorreu.");
	}
}

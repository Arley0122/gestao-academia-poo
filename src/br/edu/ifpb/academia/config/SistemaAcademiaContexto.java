package br.edu.ifpb.academia.config;

import br.edu.ifpb.academia.repository.AlunoRepository;
import br.edu.ifpb.academia.repository.CobrancaRepository;
import br.edu.ifpb.academia.repository.ContratoRepository;
import br.edu.ifpb.academia.repository.DiaTreinoExercicioRepository;
import br.edu.ifpb.academia.repository.DiaTreinoRepository;
import br.edu.ifpb.academia.repository.ExercicioRepository;
import br.edu.ifpb.academia.repository.PlanoRepository;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;
import br.edu.ifpb.academia.repository.TreinoAtivoRepository;
import br.edu.ifpb.academia.repository.UnidadeRepository;
import br.edu.ifpb.academia.repository.memoria.AlunoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.CobrancaRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.ContratoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.DiaTreinoExercicioRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.DiaTreinoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.ExercicioRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.PlanoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.ProgramaTreinoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.TreinoAtivoRepositoryMemoria;
import br.edu.ifpb.academia.repository.memoria.UnidadeRepositoryMemoria;
import br.edu.ifpb.academia.service.AcessoService;
import br.edu.ifpb.academia.service.AlunoService;
import br.edu.ifpb.academia.service.CobrancaService;
import br.edu.ifpb.academia.service.ContratoService;
import br.edu.ifpb.academia.service.DiaTreinoExercicioService;
import br.edu.ifpb.academia.service.DiaTreinoService;
import br.edu.ifpb.academia.service.ExercicioService;
import br.edu.ifpb.academia.service.NotificacaoService;
import br.edu.ifpb.academia.service.NotificacaoWhatsAppService;
import br.edu.ifpb.academia.service.PlanoService;
import br.edu.ifpb.academia.service.ProgramaTreinoService;
import br.edu.ifpb.academia.service.TreinoAtivoService;
import br.edu.ifpb.academia.service.UnidadeService;

/**
 * Montagem manual de dependências (Injeção de Dependência em Java puro) e carga inicial.
 */
public class SistemaAcademiaContexto {

	private final AlunoService alunoService;
	private final UnidadeService unidadeService;
	private final PlanoService planoService;
	private final ContratoService contratoService;
	private final CobrancaService cobrancaService;
	private final ProgramaTreinoService programaTreinoService;
	private final DiaTreinoService diaTreinoService;
	private final ExercicioService exercicioService;
	private final DiaTreinoExercicioService diaTreinoExercicioService;
	private final TreinoAtivoService treinoAtivoService;
	private final AcessoService acessoService;

	public SistemaAcademiaContexto() {
		this(new NotificacaoWhatsAppService());
	}

	public SistemaAcademiaContexto(NotificacaoService notificacaoService) {
		AlunoRepository alunoRepository = new AlunoRepositoryMemoria();
		UnidadeRepository unidadeRepository = new UnidadeRepositoryMemoria();
		PlanoRepository planoRepository = new PlanoRepositoryMemoria();
		ContratoRepository contratoRepository = new ContratoRepositoryMemoria();
		CobrancaRepository cobrancaRepository = new CobrancaRepositoryMemoria();
		ProgramaTreinoRepository programaTreinoRepository = new ProgramaTreinoRepositoryMemoria();
		DiaTreinoRepository diaTreinoRepository = new DiaTreinoRepositoryMemoria();
		ExercicioRepository exercicioRepository = new ExercicioRepositoryMemoria();
		DiaTreinoExercicioRepository diaTreinoExercicioRepository = new DiaTreinoExercicioRepositoryMemoria();
		TreinoAtivoRepository treinoAtivoRepository = new TreinoAtivoRepositoryMemoria();

		this.unidadeService = new UnidadeService(unidadeRepository);
		this.planoService = new PlanoService(planoRepository);
		this.programaTreinoService = new ProgramaTreinoService(programaTreinoRepository);
		this.diaTreinoService = new DiaTreinoService(diaTreinoRepository, programaTreinoService);
		this.exercicioService = new ExercicioService(exercicioRepository);
		this.diaTreinoExercicioService = new DiaTreinoExercicioService(
				diaTreinoExercicioRepository, diaTreinoService, exercicioService);
		this.treinoAtivoService = new TreinoAtivoService(treinoAtivoRepository, programaTreinoService);
		this.alunoService = new AlunoService(alunoRepository, treinoAtivoService);
		this.cobrancaService = new CobrancaService(cobrancaRepository, contratoRepository, notificacaoService);
		this.contratoService = new ContratoService(
				contratoRepository, alunoService, unidadeService, planoService, cobrancaService, treinoAtivoService);
		this.acessoService = new AcessoService(alunoService, contratoRepository, cobrancaRepository, cobrancaService);

		new DataInitializer(planoRepository, programaTreinoRepository).inicializar();
	}

	public AlunoService getAlunoService() {
		return alunoService;
	}

	public UnidadeService getUnidadeService() {
		return unidadeService;
	}

	public PlanoService getPlanoService() {
		return planoService;
	}

	public ContratoService getContratoService() {
		return contratoService;
	}

	public CobrancaService getCobrancaService() {
		return cobrancaService;
	}

	public ProgramaTreinoService getProgramaTreinoService() {
		return programaTreinoService;
	}

	public DiaTreinoService getDiaTreinoService() {
		return diaTreinoService;
	}

	public ExercicioService getExercicioService() {
		return exercicioService;
	}

	public DiaTreinoExercicioService getDiaTreinoExercicioService() {
		return diaTreinoExercicioService;
	}

	public TreinoAtivoService getTreinoAtivoService() {
		return treinoAtivoService;
	}

	public AcessoService getAcessoService() {
		return acessoService;
	}
}

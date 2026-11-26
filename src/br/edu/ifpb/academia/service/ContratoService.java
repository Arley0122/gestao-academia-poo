package br.edu.ifpb.academia.service;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.model.Contrato;
import br.edu.ifpb.academia.model.Plano;
import br.edu.ifpb.academia.model.Unidade;
import br.edu.ifpb.academia.repository.ContratoRepository;
import br.edu.ifpb.academia.util.Validador;

public class ContratoService {

	private final ContratoRepository repository;
	private final AlunoService alunoService;
	private final UnidadeService unidadeService;
	private final PlanoService planoService;
	private final CobrancaService cobrancaService;
	private final TreinoAtivoService treinoAtivoService;

	public ContratoService(ContratoRepository repository, AlunoService alunoService, UnidadeService unidadeService,
			PlanoService planoService, CobrancaService cobrancaService, TreinoAtivoService treinoAtivoService) {
		this.repository = repository;
		this.alunoService = alunoService;
		this.unidadeService = unidadeService;
		this.planoService = planoService;
		this.cobrancaService = cobrancaService;
		this.treinoAtivoService = treinoAtivoService;
	}

	public List<Contrato> listar() {
		return repository.listarTodos();
	}

	public Contrato buscar(Long id) {
		return buscarEntidade(id);
	}

	public Contrato criar(Long alunoId, Long unidadeId, Long planoId, LocalDate dataInicio, StatusContrato status) {
		Validador.naoNulo(alunoId, "Aluno");
		Validador.naoNulo(unidadeId, "Unidade");
		Validador.naoNulo(planoId, "Plano");
		if (repository.existePorAlunoIdEStatus(alunoId, StatusContrato.ATIVO)) {
			throw new RegraDeNegocioException("Aluno já possui um contrato ativo.");
		}
		Aluno aluno = alunoService.buscarEntidade(alunoId);
		Unidade unidade = unidadeService.buscarEntidade(unidadeId);
		Plano plano = planoService.buscarEntidade(planoId);

		Contrato contrato = new Contrato(aluno, unidade, plano, dataInicio, status);
		contrato = repository.salvar(contrato);
		try {
			cobrancaService.gerarParaContrato(contrato);
			alunoService.ativar(contrato.getAluno());
			treinoAtivoService.sincronizar(contrato.getAluno());
		} catch (RuntimeException ex) {
			repository.excluir(contrato);
			throw ex;
		}
		return contrato;
	}

	public Contrato atualizar(Long id, Long alunoId, Long unidadeId, Long planoId, LocalDate dataInicio,
			StatusContrato status) {
		Contrato contrato = buscarEntidade(id);
		Validador.naoNulo(alunoId, "Aluno");
		Validador.naoNulo(unidadeId, "Unidade");
		Validador.naoNulo(planoId, "Plano");
		if (!contrato.getAluno().getId().equals(alunoId)
				&& repository.existePorAlunoIdEStatus(alunoId, StatusContrato.ATIVO)) {
			throw new RegraDeNegocioException("Aluno já possui um contrato ativo.");
		}
		Aluno aluno = alunoService.buscarEntidade(alunoId);
		Unidade unidade = unidadeService.buscarEntidade(unidadeId);
		Plano plano = planoService.buscarEntidade(planoId);

		contrato.atualizarDados(aluno, unidade, plano, dataInicio, status);
		return repository.salvar(contrato);
	}

	public Contrato cancelar(Long id) {
		Contrato contrato = buscarEntidade(id);
		contrato.cancelar();
		alunoService.inativar(contrato.getAluno());
		return repository.salvar(contrato);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Contrato buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado com ID: " + id));
	}
}

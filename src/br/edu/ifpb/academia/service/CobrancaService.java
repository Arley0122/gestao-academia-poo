package br.edu.ifpb.academia.service;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.model.Cobranca;
import br.edu.ifpb.academia.model.Contrato;
import br.edu.ifpb.academia.repository.CobrancaRepository;
import br.edu.ifpb.academia.repository.ContratoRepository;
import br.edu.ifpb.academia.util.Validador;

public class CobrancaService {

	private static final int DIAS_CARENCIA = 2;

	private final CobrancaRepository repository;
	private final ContratoRepository contratoRepository;
	private final NotificacaoService notificacaoService;

	public CobrancaService(CobrancaRepository repository, ContratoRepository contratoRepository,
			NotificacaoService notificacaoService) {
		this.repository = repository;
		this.contratoRepository = contratoRepository;
		this.notificacaoService = notificacaoService;
	}

	public List<Cobranca> listar() {
		return repository.listarTodos();
	}

	public Cobranca buscar(Long id) {
		return buscarEntidade(id);
	}

	public Cobranca criar(Long contratoId, LocalDate dataVencimento, LocalDate dataPagamento,
			FormaPagamento formaPagamento, StatusCobranca status) {
		Validador.naoNulo(contratoId, "Contrato");
		Contrato contrato = buscarContrato(contratoId);
		Cobranca cobranca = new Cobranca(contrato, dataVencimento, dataPagamento, formaPagamento, status);
		return repository.salvar(cobranca);
	}

	public Cobranca atualizar(Long id, Long contratoId, LocalDate dataVencimento, LocalDate dataPagamento,
			FormaPagamento formaPagamento, StatusCobranca status) {
		Cobranca cobranca = buscarEntidade(id);
		Validador.naoNulo(contratoId, "Contrato");
		Contrato contrato = buscarContrato(contratoId);
		cobranca.atualizarDados(contrato, dataVencimento, dataPagamento, formaPagamento, status);
		return repository.salvar(cobranca);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Cobranca pagar(Long id, LocalDate dataPagamento, FormaPagamento formaPagamento) {
		Cobranca cobranca = buscarEntidade(id);
		cobranca.registrarPagamento(dataPagamento, formaPagamento);
		return repository.salvar(cobranca);
	}

	public void gerarParaContrato(Contrato contrato) {
		Cobranca cobranca = new Cobranca(contrato, contrato.getDataInicio());
		repository.salvar(cobranca);
	}

	public void atualizarAtrasadas() {
		LocalDate limite = LocalDate.now().minusDays(DIAS_CARENCIA);
		List<Cobranca> vencidas = repository.buscarPorStatusEVencimentoAnterior(StatusCobranca.PENDENTE, limite);
		for (Cobranca cobranca : vencidas) {
			cobranca.marcarComoAtrasada();
			notificacaoService.cobrancaAtrasada(cobranca);
		}
		repository.salvarTodos(vencidas);
	}

	public void notificacoesAutomaticas() {
		LocalDate hoje = LocalDate.now();
		repository.buscarPorStatusEVencimentoEntre(StatusCobranca.PENDENTE, hoje, hoje.plusDays(2))
				.forEach(notificacaoService::vencimentoProximo);
		atualizarAtrasadas();
	}

	private Cobranca buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cobrança não encontrada com ID: " + id));
	}

	private Contrato buscarContrato(Long id) {
		return contratoRepository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado com ID: " + id));
	}
}

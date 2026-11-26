package br.edu.ifpb.academia.service;

import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.model.ResultadoAcesso;
import br.edu.ifpb.academia.repository.CobrancaRepository;
import br.edu.ifpb.academia.repository.ContratoRepository;

public class AcessoService {

	private final AlunoService alunoService;
	private final ContratoRepository contratoRepository;
	private final CobrancaRepository cobrancaRepository;
	private final CobrancaService cobrancaService;

	public AcessoService(AlunoService alunoService, ContratoRepository contratoRepository,
			CobrancaRepository cobrancaRepository, CobrancaService cobrancaService) {
		this.alunoService = alunoService;
		this.contratoRepository = contratoRepository;
		this.cobrancaRepository = cobrancaRepository;
		this.cobrancaService = cobrancaService;
	}

	public ResultadoAcesso verificar(Long alunoId) {
		cobrancaService.atualizarAtrasadas();
		Aluno aluno = alunoService.buscarEntidade(alunoId);
		if (!aluno.isAtivo()) {
			throw new RegraDeNegocioException("Aluno inativo. Acesso negado.");
		}
		if (contratoRepository
				.buscarPrimeiroPorAlunoIdEStatusOrdenadoPorInicioDesc(alunoId, StatusContrato.ATIVO)
				.isEmpty()) {
			return new ResultadoAcesso(alunoId, false, "Aluno sem contrato ativo.");
		}
		if (cobrancaRepository.existePorContratoAlunoIdEStatus(alunoId, StatusCobranca.ATRASADO)) {
			return new ResultadoAcesso(alunoId, false, "Aluno possui cobrança atrasada.");
		}
		return new ResultadoAcesso(alunoId, true, "Acesso liberado.");
	}
}

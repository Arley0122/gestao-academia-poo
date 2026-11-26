package br.edu.ifpb.academia.repository.memoria;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.model.Cobranca;
import br.edu.ifpb.academia.repository.CobrancaRepository;

public class CobrancaRepositoryMemoria extends RepositorioMemoria<Cobranca> implements CobrancaRepository {

	@Override
	public List<Cobranca> buscarPorStatusEVencimentoAnterior(StatusCobranca status, LocalDate dataLimite) {
		return registros().stream()
				.filter(cobranca -> cobranca.getStatus() == status
						&& cobranca.getDataVencimento().isBefore(dataLimite))
				.toList();
	}

	@Override
	public List<Cobranca> buscarPorStatusEVencimentoEntre(StatusCobranca status, LocalDate inicio, LocalDate fim) {
		return registros().stream()
				.filter(cobranca -> cobranca.getStatus() == status
						&& !cobranca.getDataVencimento().isBefore(inicio)
						&& !cobranca.getDataVencimento().isAfter(fim))
				.toList();
	}

	@Override
	public boolean existePorContratoAlunoIdEStatus(Long alunoId, StatusCobranca status) {
		return registros().stream()
				.anyMatch(cobranca -> cobranca.getContrato().getAluno().getId().equals(alunoId)
						&& cobranca.getStatus() == status);
	}
}

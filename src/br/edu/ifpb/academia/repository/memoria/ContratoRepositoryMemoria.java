package br.edu.ifpb.academia.repository.memoria;

import java.util.Comparator;
import java.util.Optional;

import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.model.Contrato;
import br.edu.ifpb.academia.repository.ContratoRepository;

public class ContratoRepositoryMemoria extends RepositorioMemoria<Contrato> implements ContratoRepository {

	@Override
	public boolean existePorAlunoIdEStatus(Long alunoId, StatusContrato status) {
		return registros().stream()
				.anyMatch(contrato -> contrato.getAluno().getId().equals(alunoId)
						&& contrato.getStatus() == status);
	}

	@Override
	public Optional<Contrato> buscarPrimeiroPorAlunoIdEStatusOrdenadoPorInicioDesc(Long alunoId,
			StatusContrato status) {
		return registros().stream()
				.filter(contrato -> contrato.getAluno().getId().equals(alunoId)
						&& contrato.getStatus() == status)
				.max(Comparator.comparing(Contrato::getDataInicio));
	}
}

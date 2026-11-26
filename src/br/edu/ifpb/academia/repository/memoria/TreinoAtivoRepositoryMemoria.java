package br.edu.ifpb.academia.repository.memoria;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.model.TreinoAtivo;
import br.edu.ifpb.academia.repository.TreinoAtivoRepository;

public class TreinoAtivoRepositoryMemoria extends RepositorioMemoria<TreinoAtivo>
		implements TreinoAtivoRepository {

	@Override
	public Optional<TreinoAtivo> buscarPorAlunoIdEStatus(Long alunoId, StatusTreinoAtivo status) {
		return registros().stream()
				.filter(treino -> treino.getAluno().getId().equals(alunoId) && treino.getStatus() == status)
				.findFirst();
	}

	@Override
	public List<TreinoAtivo> listarPorAlunoIdOrdenadoPorInicioDesc(Long alunoId) {
		return registros().stream()
				.filter(treino -> treino.getAluno().getId().equals(alunoId))
				.sorted(Comparator.comparing(TreinoAtivo::getDataInicio).reversed())
				.toList();
	}
}

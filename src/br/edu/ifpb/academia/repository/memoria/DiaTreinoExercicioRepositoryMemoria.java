package br.edu.ifpb.academia.repository.memoria;

import java.util.Comparator;
import java.util.List;

import br.edu.ifpb.academia.model.DiaTreinoExercicio;
import br.edu.ifpb.academia.repository.DiaTreinoExercicioRepository;

public class DiaTreinoExercicioRepositoryMemoria extends RepositorioMemoria<DiaTreinoExercicio>
		implements DiaTreinoExercicioRepository {

	@Override
	public List<DiaTreinoExercicio> listarPorDiaTreinoId(Long diaTreinoId) {
		return registros().stream()
				.filter(item -> item.getDiaTreino().getId().equals(diaTreinoId))
				.sorted(Comparator.comparing(DiaTreinoExercicio::getOrdem))
				.toList();
	}
}

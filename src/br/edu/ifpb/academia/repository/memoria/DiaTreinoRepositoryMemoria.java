package br.edu.ifpb.academia.repository.memoria;

import java.util.Comparator;
import java.util.List;

import br.edu.ifpb.academia.model.DiaTreino;
import br.edu.ifpb.academia.repository.DiaTreinoRepository;

public class DiaTreinoRepositoryMemoria extends RepositorioMemoria<DiaTreino> implements DiaTreinoRepository {

	@Override
	public List<DiaTreino> listarPorProgramaTreinoId(Long programaTreinoId) {
		return registros().stream()
				.filter(dia -> dia.getProgramaTreino().getId().equals(programaTreinoId))
				.sorted(Comparator.comparing(DiaTreino::getOrdem))
				.toList();
	}
}

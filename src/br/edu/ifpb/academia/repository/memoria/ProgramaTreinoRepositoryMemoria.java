package br.edu.ifpb.academia.repository.memoria;

import java.util.Optional;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;

public class ProgramaTreinoRepositoryMemoria extends RepositorioMemoria<ProgramaTreino>
		implements ProgramaTreinoRepository {

	@Override
	public Optional<ProgramaTreino> buscarPorNivelEFrequencia(NivelExperiencia nivelExperiencia,
			Integer frequenciaSemanal) {
		return registros().stream()
				.filter(programa -> programa.getNivelExperiencia() == nivelExperiencia
						&& programa.getFrequenciaSemanal().equals(frequenciaSemanal))
				.findFirst();
	}

	@Override
	public boolean existePorNivelEFrequencia(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal) {
		return buscarPorNivelEFrequencia(nivelExperiencia, frequenciaSemanal).isPresent();
	}
}

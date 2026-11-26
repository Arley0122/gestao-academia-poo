package br.edu.ifpb.academia.repository.memoria;

import br.edu.ifpb.academia.model.Exercicio;
import br.edu.ifpb.academia.repository.ExercicioRepository;

public class ExercicioRepositoryMemoria extends RepositorioMemoria<Exercicio> implements ExercicioRepository {

	@Override
	public boolean existePorNome(String nome) {
		return registros().stream().anyMatch(exercicio -> exercicio.getNome().equals(nome));
	}
}

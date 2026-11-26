package br.edu.ifpb.academia.repository.memoria;

import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.repository.AlunoRepository;

public class AlunoRepositoryMemoria extends RepositorioMemoria<Aluno> implements AlunoRepository {

	@Override
	public boolean existePorCpf(String cpf) {
		return registros().stream().anyMatch(aluno -> aluno.getCpf().equals(cpf));
	}

	@Override
	public boolean existePorEmail(String email) {
		return registros().stream().anyMatch(aluno -> aluno.getEmail().equals(email));
	}
}

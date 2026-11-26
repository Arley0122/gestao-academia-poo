package br.edu.ifpb.academia.repository;

import br.edu.ifpb.academia.model.Aluno;

public interface AlunoRepository extends Repositorio<Aluno> {

	boolean existePorCpf(String cpf);

	boolean existePorEmail(String email);
}

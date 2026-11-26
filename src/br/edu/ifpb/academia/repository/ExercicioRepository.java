package br.edu.ifpb.academia.repository;

import br.edu.ifpb.academia.model.Exercicio;

public interface ExercicioRepository extends Repositorio<Exercicio> {

	boolean existePorNome(String nome);
}

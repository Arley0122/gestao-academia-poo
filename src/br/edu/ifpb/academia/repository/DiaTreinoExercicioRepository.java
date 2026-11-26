package br.edu.ifpb.academia.repository;

import java.util.List;

import br.edu.ifpb.academia.model.DiaTreinoExercicio;

public interface DiaTreinoExercicioRepository extends Repositorio<DiaTreinoExercicio> {

	List<DiaTreinoExercicio> listarPorDiaTreinoId(Long diaTreinoId);
}

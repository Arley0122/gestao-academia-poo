package br.edu.ifpb.academia.repository;

import java.util.List;

import br.edu.ifpb.academia.model.DiaTreino;

public interface DiaTreinoRepository extends Repositorio<DiaTreino> {

	List<DiaTreino> listarPorProgramaTreinoId(Long programaTreinoId);
}

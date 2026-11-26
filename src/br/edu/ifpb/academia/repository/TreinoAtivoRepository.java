package br.edu.ifpb.academia.repository;

import java.util.List;
import java.util.Optional;

import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.model.TreinoAtivo;

public interface TreinoAtivoRepository extends Repositorio<TreinoAtivo> {

	Optional<TreinoAtivo> buscarPorAlunoIdEStatus(Long alunoId, StatusTreinoAtivo status);

	List<TreinoAtivo> listarPorAlunoIdOrdenadoPorInicioDesc(Long alunoId);
}

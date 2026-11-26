package br.edu.ifpb.academia.repository;

import java.util.Optional;

import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.model.Contrato;

public interface ContratoRepository extends Repositorio<Contrato> {

	boolean existePorAlunoIdEStatus(Long alunoId, StatusContrato status);

	Optional<Contrato> buscarPrimeiroPorAlunoIdEStatusOrdenadoPorInicioDesc(Long alunoId, StatusContrato status);
}

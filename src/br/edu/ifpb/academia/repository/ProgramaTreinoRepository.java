package br.edu.ifpb.academia.repository;

import java.util.Optional;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.model.ProgramaTreino;

public interface ProgramaTreinoRepository extends Repositorio<ProgramaTreino> {

	Optional<ProgramaTreino> buscarPorNivelEFrequencia(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal);

	boolean existePorNivelEFrequencia(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal);
}

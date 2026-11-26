package br.edu.ifpb.academia.repository;

import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.model.Plano;

public interface PlanoRepository extends Repositorio<Plano> {

	boolean existePorTipo(TipoPlano tipo);
}

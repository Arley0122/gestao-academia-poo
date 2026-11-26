package br.edu.ifpb.academia.repository;

import br.edu.ifpb.academia.model.Unidade;

public interface UnidadeRepository extends Repositorio<Unidade> {

	boolean existePorCnpj(String cnpj);
}

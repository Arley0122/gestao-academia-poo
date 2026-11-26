package br.edu.ifpb.academia.repository.memoria;

import br.edu.ifpb.academia.model.Unidade;
import br.edu.ifpb.academia.repository.UnidadeRepository;

public class UnidadeRepositoryMemoria extends RepositorioMemoria<Unidade> implements UnidadeRepository {

	@Override
	public boolean existePorCnpj(String cnpj) {
		return registros().stream().anyMatch(unidade -> unidade.getCnpj().equals(cnpj));
	}
}

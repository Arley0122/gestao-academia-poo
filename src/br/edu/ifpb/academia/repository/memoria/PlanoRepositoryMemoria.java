package br.edu.ifpb.academia.repository.memoria;

import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.model.Plano;
import br.edu.ifpb.academia.repository.PlanoRepository;

public class PlanoRepositoryMemoria extends RepositorioMemoria<Plano> implements PlanoRepository {

	@Override
	public boolean existePorTipo(TipoPlano tipo) {
		return registros().stream().anyMatch(plano -> plano.getTipo() == tipo);
	}
}

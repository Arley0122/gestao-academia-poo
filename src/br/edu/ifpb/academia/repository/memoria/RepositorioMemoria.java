package br.edu.ifpb.academia.repository.memoria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.edu.ifpb.academia.model.Identificavel;
import br.edu.ifpb.academia.repository.Repositorio;

/**
 * Implementação base em memória para os repositórios, simulando geração sequencial de IDs.
 */
public abstract class RepositorioMemoria<T extends Identificavel> implements Repositorio<T> {

	private final Map<Long, T> armazenamento = new LinkedHashMap<>();
	private long sequencia = 1L;

	@Override
	public T salvar(T entidade) {
		if (entidade.getId() == null) {
			entidade.setId(sequencia++);
		}
		armazenamento.put(entidade.getId(), entidade);
		return entidade;
	}

	@Override
	public List<T> salvarTodos(List<T> entidades) {
		for (T entidade : entidades) {
			salvar(entidade);
		}
		return entidades;
	}

	@Override
	public Optional<T> buscarPorId(Long id) {
		return Optional.ofNullable(armazenamento.get(id));
	}

	@Override
	public List<T> listarTodos() {
		return new ArrayList<>(armazenamento.values());
	}

	@Override
	public void excluir(T entidade) {
		if (entidade != null && entidade.getId() != null) {
			armazenamento.remove(entidade.getId());
		}
	}

	protected Collection<T> registros() {
		return armazenamento.values();
	}
}

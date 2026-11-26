package br.edu.ifpb.academia.repository;

import java.util.List;
import java.util.Optional;

import br.edu.ifpb.academia.model.Identificavel;

/**
 * Contrato genérico de persistência para entidades identificáveis.
 */
public interface Repositorio<T extends Identificavel> {

	T salvar(T entidade);

	List<T> salvarTodos(List<T> entidades);

	Optional<T> buscarPorId(Long id);

	List<T> listarTodos();

	void excluir(T entidade);
}

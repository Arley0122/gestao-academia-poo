package br.edu.ifpb.academia.service;

import java.util.List;

import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Exercicio;
import br.edu.ifpb.academia.repository.ExercicioRepository;

public class ExercicioService {

	private final ExercicioRepository repository;

	public ExercicioService(ExercicioRepository repository) {
		this.repository = repository;
	}

	public List<Exercicio> listar() {
		return repository.listarTodos();
	}

	public Exercicio buscar(Long id) {
		return buscarEntidade(id);
	}

	public Exercicio criar(String nome, String musculaturaAlvo) {
		Exercicio exercicio = new Exercicio(nome, musculaturaAlvo);
		if (repository.existePorNome(exercicio.getNome())) {
			throw new RegraDeNegocioException("Já existe exercício com este nome.");
		}
		return repository.salvar(exercicio);
	}

	public Exercicio atualizar(Long id, String nome, String musculaturaAlvo) {
		Exercicio exercicio = buscarEntidade(id);
		if (nome != null && !exercicio.getNome().equals(nome) && repository.existePorNome(nome)) {
			throw new RegraDeNegocioException("Já existe exercício com este nome.");
		}
		exercicio.atualizarDados(nome, musculaturaAlvo);
		return repository.salvar(exercicio);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Exercicio buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado com ID: " + id));
	}
}

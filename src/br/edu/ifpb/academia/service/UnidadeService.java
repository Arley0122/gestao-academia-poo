package br.edu.ifpb.academia.service;

import java.util.List;

import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Unidade;
import br.edu.ifpb.academia.repository.UnidadeRepository;

public class UnidadeService {

	private final UnidadeRepository repository;

	public UnidadeService(UnidadeRepository repository) {
		this.repository = repository;
	}

	public List<Unidade> listar() {
		return repository.listarTodos();
	}

	public Unidade buscar(Long id) {
		return buscarEntidade(id);
	}

	public Unidade criar(String nome, String endereco, String cnpj, String telefone) {
		Unidade unidade = new Unidade(nome, endereco, cnpj, telefone);
		if (repository.existePorCnpj(unidade.getCnpj())) {
			throw new RegraDeNegocioException("Já existe unidade com este CNPJ.");
		}
		return repository.salvar(unidade);
	}

	public Unidade atualizar(Long id, String nome, String endereco, String cnpj, String telefone) {
		Unidade unidade = buscarEntidade(id);
		if (cnpj != null && !unidade.getCnpj().equals(cnpj) && repository.existePorCnpj(cnpj)) {
			throw new RegraDeNegocioException("Já existe unidade com este CNPJ.");
		}
		unidade.atualizarDados(nome, endereco, cnpj, telefone);
		return repository.salvar(unidade);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Unidade buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + id));
	}
}

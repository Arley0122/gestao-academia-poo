package br.edu.ifpb.academia.service;

import java.math.BigDecimal;
import java.util.List;

import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Plano;
import br.edu.ifpb.academia.repository.PlanoRepository;

public class PlanoService {

	private final PlanoRepository repository;

	public PlanoService(PlanoRepository repository) {
		this.repository = repository;
	}

	public List<Plano> listar() {
		return repository.listarTodos();
	}

	public Plano buscar(Long id) {
		return buscarEntidade(id);
	}

	public Plano criar(TipoPlano tipo, BigDecimal valor, Integer duracao, BigDecimal percentualDesconto,
			String descricao) {
		Plano plano = new Plano(tipo, valor, duracao, percentualDesconto, descricao);
		if (repository.existePorTipo(plano.getTipo())) {
			throw new RegraDeNegocioException("Já existe plano deste tipo.");
		}
		return repository.salvar(plano);
	}

	public Plano atualizar(Long id, TipoPlano tipo, BigDecimal valor, Integer duracao, BigDecimal percentualDesconto,
			String descricao) {
		Plano plano = buscarEntidade(id);
		if (tipo != null && !plano.getTipo().equals(tipo) && repository.existePorTipo(tipo)) {
			throw new RegraDeNegocioException("Já existe plano deste tipo.");
		}
		plano.atualizarDados(tipo, valor, duracao, percentualDesconto, descricao);
		return repository.salvar(plano);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Plano buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));
	}
}

package br.edu.ifpb.academia.service;

import java.util.List;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;

public class ProgramaTreinoService {

	private final ProgramaTreinoRepository repository;

	public ProgramaTreinoService(ProgramaTreinoRepository repository) {
		this.repository = repository;
	}

	public List<ProgramaTreino> listar() {
		return repository.listarTodos();
	}

	public ProgramaTreino buscar(Long id) {
		return buscarEntidade(id);
	}

	public ProgramaTreino criar(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			TipoEstrutural tipoEstrutural, String descricao) {
		ProgramaTreino programa = new ProgramaTreino(nivelExperiencia, frequenciaSemanal, tipoEstrutural, descricao);
		if (repository.existePorNivelEFrequencia(programa.getNivelExperiencia(), programa.getFrequenciaSemanal())) {
			throw new RegraDeNegocioException("Já existe programa para este nível e frequência.");
		}
		return repository.salvar(programa);
	}

	public ProgramaTreino atualizar(Long id, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			TipoEstrutural tipoEstrutural, String descricao) {
		ProgramaTreino programa = buscarEntidade(id);
		boolean chaveMudou = (!programa.getNivelExperiencia().equals(nivelExperiencia)
				|| !programa.getFrequenciaSemanal().equals(frequenciaSemanal));
		if (chaveMudou && nivelExperiencia != null && frequenciaSemanal != null
				&& repository.existePorNivelEFrequencia(nivelExperiencia, frequenciaSemanal)) {
			throw new RegraDeNegocioException("Já existe programa para este nível e frequência.");
		}
		programa.atualizarDados(nivelExperiencia, frequenciaSemanal, tipoEstrutural, descricao);
		return repository.salvar(programa);
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public ProgramaTreino buscarEntidade(Long id) {
		return repository.buscarPorId(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Programa de treino não encontrado com ID: " + id));
	}

	public ProgramaTreino buscarPorPerfil(NivelExperiencia nivel, Integer frequencia) {
		return repository.buscarPorNivelEFrequencia(nivel, frequencia)
				.orElseThrow(() -> new RegraDeNegocioException("Não existe programa para o perfil do aluno."));
	}
}

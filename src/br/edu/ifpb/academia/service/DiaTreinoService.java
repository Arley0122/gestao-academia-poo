package br.edu.ifpb.academia.service;

import java.util.List;

import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.model.DiaTreino;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.repository.DiaTreinoRepository;
import br.edu.ifpb.academia.util.Validador;

public class DiaTreinoService {

	private final DiaTreinoRepository repository;
	private final ProgramaTreinoService programaTreinoService;

	public DiaTreinoService(DiaTreinoRepository repository, ProgramaTreinoService programaTreinoService) {
		this.repository = repository;
		this.programaTreinoService = programaTreinoService;
	}

	public List<DiaTreino> listar() {
		return repository.listarTodos();
	}

	public List<DiaTreino> listarPorPrograma(Long programaTreinoId) {
		programaTreinoService.buscarEntidade(programaTreinoId);
		return repository.listarPorProgramaTreinoId(programaTreinoId);
	}

	public DiaTreino buscar(Long id) {
		return buscarEntidade(id);
	}

	public DiaTreino criar(Long programaTreinoId, String nomeDia, Integer ordem) {
		Validador.naoNulo(programaTreinoId, "Programa de treino");
		ProgramaTreino programa = programaTreinoService.buscarEntidade(programaTreinoId);
		DiaTreino dia = new DiaTreino(programa, nomeDia, ordem);
		return repository.salvar(dia);
	}

	public DiaTreino atualizar(Long id, Long programaTreinoId, String nomeDia, Integer ordem) {
		DiaTreino dia = buscarEntidade(id);
		Validador.naoNulo(programaTreinoId, "Programa de treino");
		ProgramaTreino programa = programaTreinoService.buscarEntidade(programaTreinoId);
		dia.atualizarDados(programa, nomeDia, ordem);
		return repository.salvar(dia);
	}

	public void excluir(Long id) {
		DiaTreino dia = buscarEntidade(id);
		dia.getProgramaTreino().removerDiaTreino(dia);
		repository.excluir(dia);
	}

	public DiaTreino buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Dia de treino não encontrado com ID: " + id));
	}
}

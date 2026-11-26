package br.edu.ifpb.academia.service;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.model.TreinoAtivo;
import br.edu.ifpb.academia.repository.TreinoAtivoRepository;

public class TreinoAtivoService {

	private final TreinoAtivoRepository repository;
	private final ProgramaTreinoService programaTreinoService;

	public TreinoAtivoService(TreinoAtivoRepository repository, ProgramaTreinoService programaTreinoService) {
		this.repository = repository;
		this.programaTreinoService = programaTreinoService;
	}

	public List<TreinoAtivo> listar() {
		return repository.listarTodos();
	}

	public TreinoAtivo buscar(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Treino ativo não encontrado com ID: " + id));
	}

	public List<TreinoAtivo> listarPorAluno(Long alunoId) {
		return repository.listarPorAlunoIdOrdenadoPorInicioDesc(alunoId);
	}

	public void sincronizar(Aluno aluno) {
		ProgramaTreino programa = programaTreinoService.buscarPorPerfil(aluno.getNivelExperiencia(),
				aluno.getFrequenciaSemanal());
		repository.buscarPorAlunoIdEStatus(aluno.getId(), StatusTreinoAtivo.ATIVO).ifPresent(atual -> {
			if (!atual.pertenceAoPrograma(programa)) {
				atual.encerrar(LocalDate.now());
				repository.salvar(atual);
			}
		});
		if (repository.buscarPorAlunoIdEStatus(aluno.getId(), StatusTreinoAtivo.ATIVO).isEmpty()) {
			TreinoAtivo treino = new TreinoAtivo(aluno, programa, LocalDate.now());
			repository.salvar(treino);
		}
	}
}

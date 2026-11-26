package br.edu.ifpb.academia.service;

import java.util.List;

import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.model.DiaTreino;
import br.edu.ifpb.academia.model.DiaTreinoExercicio;
import br.edu.ifpb.academia.model.Exercicio;
import br.edu.ifpb.academia.repository.DiaTreinoExercicioRepository;
import br.edu.ifpb.academia.util.Validador;

public class DiaTreinoExercicioService {

	private final DiaTreinoExercicioRepository repository;
	private final DiaTreinoService diaTreinoService;
	private final ExercicioService exercicioService;

	public DiaTreinoExercicioService(DiaTreinoExercicioRepository repository, DiaTreinoService diaTreinoService,
			ExercicioService exercicioService) {
		this.repository = repository;
		this.diaTreinoService = diaTreinoService;
		this.exercicioService = exercicioService;
	}

	public List<DiaTreinoExercicio> listar() {
		return repository.listarTodos();
	}

	public List<DiaTreinoExercicio> listarPorDia(Long diaTreinoId) {
		diaTreinoService.buscarEntidade(diaTreinoId);
		return repository.listarPorDiaTreinoId(diaTreinoId);
	}

	public DiaTreinoExercicio buscar(Long id) {
		return buscarEntidade(id);
	}

	public DiaTreinoExercicio criar(Long diaTreinoId, Long exercicioId, Integer series, Integer repeticoes,
			Integer ordem) {
		Validador.naoNulo(diaTreinoId, "Dia de treino");
		Validador.naoNulo(exercicioId, "Exercício");
		DiaTreino dia = diaTreinoService.buscarEntidade(diaTreinoId);
		Exercicio exercicio = exercicioService.buscarEntidade(exercicioId);
		DiaTreinoExercicio item = new DiaTreinoExercicio(dia, exercicio, series, repeticoes, ordem);
		return repository.salvar(item);
	}

	public DiaTreinoExercicio atualizar(Long id, Long diaTreinoId, Long exercicioId, Integer series,
			Integer repeticoes, Integer ordem) {
		DiaTreinoExercicio item = buscarEntidade(id);
		Validador.naoNulo(diaTreinoId, "Dia de treino");
		Validador.naoNulo(exercicioId, "Exercício");
		DiaTreino dia = diaTreinoService.buscarEntidade(diaTreinoId);
		Exercicio exercicio = exercicioService.buscarEntidade(exercicioId);
		item.atualizarDados(dia, exercicio, series, repeticoes, ordem);
		return repository.salvar(item);
	}

	public void excluir(Long id) {
		DiaTreinoExercicio item = buscarEntidade(id);
		item.getDiaTreino().removerExercicio(item);
		repository.excluir(item);
	}

	private DiaTreinoExercicio buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado com ID: " + id));
	}
}

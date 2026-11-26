package br.edu.ifpb.academia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa um dia específico dentro de um programa de treino (ex.: Treino A).
 */
public class DiaTreino implements Identificavel {

	private Long id;
	private ProgramaTreino programaTreino;
	private String nomeDia;
	private Integer ordem;
	private final List<DiaTreinoExercicio> exercicios = new ArrayList<>();

	public DiaTreino(ProgramaTreino programaTreino, String nomeDia, Integer ordem) {
		atualizarDados(programaTreino, nomeDia, ordem);
	}

	public void atualizarDados(ProgramaTreino programaTreino, String nomeDia, Integer ordem) {
		ProgramaTreino novoPrograma = Validador.naoNulo(programaTreino, "Programa de treino");
		String novoNome = Validador.naoVazio(nomeDia, "Nome do dia");
		Integer novaOrdem = Validador.positivo(ordem, "Ordem do dia");

		if (this.programaTreino != null && this.programaTreino != novoPrograma) {
			this.programaTreino.removerDiaTreino(this);
		}
		this.programaTreino = novoPrograma;
		this.nomeDia = novoNome;
		this.ordem = novaOrdem;
		this.programaTreino.adicionarDiaTreino(this);
	}

	public void adicionarExercicio(DiaTreinoExercicio item) {
		if (item != null && !exercicios.contains(item)) {
			exercicios.add(item);
		}
	}

	public void removerExercicio(DiaTreinoExercicio item) {
		exercicios.remove(item);
	}

	public List<DiaTreinoExercicio> getExercicios() {
		List<DiaTreinoExercicio> ordenados = new ArrayList<>(exercicios);
		ordenados.sort(Comparator.comparing(DiaTreinoExercicio::getOrdem));
		return Collections.unmodifiableList(ordenados);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public ProgramaTreino getProgramaTreino() {
		return programaTreino;
	}

	public String getNomeDia() {
		return nomeDia;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return "DiaTreino{id=" + id + ", programaId=" + programaTreino.getId() + ", nomeDia='" + nomeDia
				+ "', ordem=" + ordem + "}";
	}
}

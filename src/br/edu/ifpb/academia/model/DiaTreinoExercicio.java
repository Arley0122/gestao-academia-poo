package br.edu.ifpb.academia.model;

import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade associativa que vincula um exercício a um dia de treino com séries, repetições e ordem.
 */
public class DiaTreinoExercicio implements Identificavel {

	private Long id;
	private DiaTreino diaTreino;
	private Exercicio exercicio;
	private Integer series;
	private Integer repeticoes;
	private Integer ordem;

	public DiaTreinoExercicio(DiaTreino diaTreino, Exercicio exercicio, Integer series, Integer repeticoes,
			Integer ordem) {
		atualizarDados(diaTreino, exercicio, series, repeticoes, ordem);
	}

	public void atualizarDados(DiaTreino diaTreino, Exercicio exercicio, Integer series, Integer repeticoes,
			Integer ordem) {
		DiaTreino novoDia = Validador.naoNulo(diaTreino, "Dia de treino");
		Exercicio novoExercicio = Validador.naoNulo(exercicio, "Exercício");
		Integer novasSeries = Validador.positivo(series, "Séries");
		Integer novasRepeticoes = Validador.positivo(repeticoes, "Repetições");
		Integer novaOrdem = Validador.positivo(ordem, "Ordem");

		if (this.diaTreino != null && this.diaTreino != novoDia) {
			this.diaTreino.removerExercicio(this);
		}
		this.diaTreino = novoDia;
		this.exercicio = novoExercicio;
		this.series = novasSeries;
		this.repeticoes = novasRepeticoes;
		this.ordem = novaOrdem;
		this.diaTreino.adicionarExercicio(this);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public DiaTreino getDiaTreino() {
		return diaTreino;
	}

	public Exercicio getExercicio() {
		return exercicio;
	}

	public Integer getSeries() {
		return series;
	}

	public Integer getRepeticoes() {
		return repeticoes;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return "DiaTreinoExercicio{id=" + id + ", diaId=" + diaTreino.getId() + ", exercicio='"
				+ exercicio.getNome() + "', " + series + "x" + repeticoes + ", ordem=" + ordem + "}";
	}
}

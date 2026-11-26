package br.edu.ifpb.academia.model;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que registra a vinculação vigente ou histórica de um programa de treino a um aluno.
 */
public class TreinoAtivo implements Identificavel {

	private Long id;
	private Aluno aluno;
	private ProgramaTreino programaTreino;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private StatusTreinoAtivo status = StatusTreinoAtivo.ATIVO;

	public TreinoAtivo(Aluno aluno, ProgramaTreino programaTreino, LocalDate dataInicio) {
		this.aluno = Validador.naoNulo(aluno, "Aluno");
		this.programaTreino = Validador.naoNulo(programaTreino, "Programa de treino");
		this.dataInicio = Validador.naoNulo(dataInicio, "Data de início");
		this.status = StatusTreinoAtivo.ATIVO;
	}

	public boolean isAtivo() {
		return status == StatusTreinoAtivo.ATIVO;
	}

	public boolean pertenceAoPrograma(ProgramaTreino programa) {
		return programa != null && this.programaTreino.getId() != null
				&& this.programaTreino.getId().equals(programa.getId());
	}

	public void encerrar(LocalDate dataFim) {
		this.status = StatusTreinoAtivo.ENCERRADO;
		this.dataFim = Validador.naoNulo(dataFim, "Data de encerramento");
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public ProgramaTreino getProgramaTreino() {
		return programaTreino;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public StatusTreinoAtivo getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "TreinoAtivo{id=" + id + ", aluno='" + aluno.getNome() + "', programa='"
				+ programaTreino.getTipoEstrutural() + "', inicio=" + dataInicio + ", fim=" + dataFim
				+ ", status=" + status + "}";
	}
}

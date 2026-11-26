package br.edu.ifpb.academia.model;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa a contratação de um plano por um aluno em uma unidade.
 */
public class Contrato implements Identificavel {

	private Long id;
	private Aluno aluno;
	private Unidade unidade;
	private Plano plano;
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private StatusContrato status = StatusContrato.ATIVO;

	public Contrato(Aluno aluno, Unidade unidade, Plano plano, LocalDate dataInicio) {
		this(aluno, unidade, plano, dataInicio, StatusContrato.ATIVO);
	}

	public Contrato(Aluno aluno, Unidade unidade, Plano plano, LocalDate dataInicio, StatusContrato status) {
		atualizarDados(aluno, unidade, plano, dataInicio, status);
	}

	public void atualizarDados(Aluno aluno, Unidade unidade, Plano plano, LocalDate dataInicio, StatusContrato status) {
		Aluno novoAluno = Validador.naoNulo(aluno, "Aluno");
		Unidade novaUnidade = Validador.naoNulo(unidade, "Unidade");
		Plano novoPlano = Validador.naoNulo(plano, "Plano");
		LocalDate novaDataInicio = Validador.dataNoPassadoOuPresente(dataInicio, "Data de início");

		this.aluno = novoAluno;
		this.unidade = novaUnidade;
		this.plano = novoPlano;
		this.dataInicio = novaDataInicio;
		this.status = status == null ? StatusContrato.ATIVO : status;
		recalcularDataTermino();
	}

	public void setPlano(Plano plano) {
		this.plano = Validador.naoNulo(plano, "Plano");
		recalcularDataTermino();
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = Validador.dataNoPassadoOuPresente(dataInicio, "Data de início");
		recalcularDataTermino();
	}

	public boolean isAtivo() {
		return status == StatusContrato.ATIVO;
	}

	public void cancelar() {
		if (!isAtivo()) {
			throw new RegraDeNegocioException("Este contrato já está cancelado.");
		}
		this.status = StatusContrato.CANCELADO;
		this.aluno.inativar();
	}

	private void recalcularDataTermino() {
		if (dataInicio != null && plano != null && plano.getDuracao() != null) {
			this.dataTermino = dataInicio.plusDays(plano.getDuracao());
		}
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

	public Unidade getUnidade() {
		return unidade;
	}

	public Plano getPlano() {
		return plano;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public StatusContrato getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Contrato{id=" + id + ", aluno='" + aluno.getNome() + "', unidade='" + unidade.getNome()
				+ "', plano=" + plano.getTipo() + ", inicio=" + dataInicio + ", termino=" + dataTermino
				+ ", status=" + status + "}";
	}
}

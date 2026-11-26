package br.edu.ifpb.academia.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa uma cobrança vinculada a um contrato, com valor derivado do plano.
 */
public class Cobranca implements Identificavel {

	private Long id;
	private Contrato contrato;
	private LocalDate dataVencimento;
	private BigDecimal valor;
	private LocalDate dataPagamento;
	private FormaPagamento formaPagamento;
	private StatusCobranca status = StatusCobranca.PENDENTE;

	public Cobranca(Contrato contrato, LocalDate dataVencimento) {
		this(contrato, dataVencimento, null, null, StatusCobranca.PENDENTE);
	}

	public Cobranca(Contrato contrato, LocalDate dataVencimento, LocalDate dataPagamento,
			FormaPagamento formaPagamento, StatusCobranca status) {
		atualizarDados(contrato, dataVencimento, dataPagamento, formaPagamento, status);
	}

	public void atualizarDados(Contrato contrato, LocalDate dataVencimento, LocalDate dataPagamento,
			FormaPagamento formaPagamento, StatusCobranca status) {
		Contrato novoContrato = Validador.naoNulo(contrato, "Contrato");
		LocalDate novoVencimento = Validador.naoNulo(dataVencimento, "Data de vencimento");

		this.contrato = novoContrato;
		this.dataVencimento = novoVencimento;
		this.valor = this.contrato.getPlano().getValor();
		this.dataPagamento = dataPagamento;
		this.formaPagamento = formaPagamento;
		if (status != null) {
			this.status = status;
		} else if (this.status == null) {
			this.status = StatusCobranca.PENDENTE;
		}
	}

	public boolean isPaga() {
		return status == StatusCobranca.PAGO;
	}

	public void registrarPagamento(LocalDate dataPagamento, FormaPagamento formaPagamento) {
		if (isPaga()) {
			throw new RegraDeNegocioException("Esta cobrança já foi paga.");
		}
		LocalDate dataValida = Validador.dataNoPassadoOuPresente(dataPagamento, "Data de pagamento");
		FormaPagamento formaValida = Validador.naoNulo(formaPagamento, "Forma de pagamento");
		this.dataPagamento = dataValida;
		this.formaPagamento = formaValida;
		this.status = StatusCobranca.PAGO;
	}

	public void marcarComoAtrasada() {
		this.status = StatusCobranca.ATRASADO;
	}

	public boolean estaVencidaAposCarencia(LocalDate dataLimite) {
		return status == StatusCobranca.PENDENTE && dataVencimento.isBefore(dataLimite);
	}

	public boolean estaComVencimentoProximo(LocalDate inicio, LocalDate fim) {
		return status == StatusCobranca.PENDENTE
				&& !dataVencimento.isBefore(inicio)
				&& !dataVencimento.isAfter(fim);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public StatusCobranca getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Cobranca{id=" + id + ", contratoId=" + contrato.getId() + ", vencimento=" + dataVencimento
				+ ", valor=R$ " + valor + ", pagamento=" + dataPagamento + ", forma=" + formaPagamento
				+ ", status=" + status + "}";
	}
}

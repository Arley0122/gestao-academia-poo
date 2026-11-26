package br.edu.ifpb.academia.model;

import java.math.BigDecimal;

import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa um plano de adesão comercializado pela academia.
 */
public class Plano implements Identificavel {

	private Long id;
	private TipoPlano tipo;
	private BigDecimal valor;
	private Integer duracao;
	private BigDecimal percentualDesconto;
	private String descricao;

	public Plano(TipoPlano tipo, BigDecimal valor, Integer duracao, BigDecimal percentualDesconto, String descricao) {
		atualizarDados(tipo, valor, duracao, percentualDesconto, descricao);
	}

	public void atualizarDados(TipoPlano tipo, BigDecimal valor, Integer duracao, BigDecimal percentualDesconto,
			String descricao) {
		Validador.naoNulo(tipo, "Tipo do plano");
		Validador.positivo(valor, "Valor do plano");
		Validador.positivo(duracao, "Duração do plano");
		Validador.naoNegativo(percentualDesconto, "Percentual de desconto");
		Validador.naoVazio(descricao, "Descrição do plano");
		validarDescontoProgressivo(tipo, percentualDesconto);

		this.tipo = tipo;
		this.valor = valor;
		this.duracao = duracao;
		this.percentualDesconto = percentualDesconto;
		this.descricao = descricao;
	}

	public boolean isPlanoLongo() {
		return tipo == TipoPlano.TRIMESTRAL || tipo == TipoPlano.SEMESTRAL || tipo == TipoPlano.ANUAL;
	}

	private static void validarDescontoProgressivo(TipoPlano tipo, BigDecimal percentualDesconto) {
		boolean longo = tipo == TipoPlano.TRIMESTRAL || tipo == TipoPlano.SEMESTRAL || tipo == TipoPlano.ANUAL;
		if (longo && BigDecimal.ZERO.compareTo(percentualDesconto) >= 0) {
			throw new RegraDeNegocioException("Planos longos devem possuir desconto progressivo.");
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

	public TipoPlano getTipo() {
		return tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Integer getDuracao() {
		return duracao;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return "Plano{id=" + id + ", tipo=" + tipo + ", valor=R$ " + valor + ", duracao=" + duracao
				+ " dias, desconto=" + percentualDesconto + "%}";
	}
}

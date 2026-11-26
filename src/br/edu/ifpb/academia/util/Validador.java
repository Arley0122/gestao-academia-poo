package br.edu.ifpb.academia.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import br.edu.ifpb.academia.exception.ValidacaoException;

/**
 * Utilitário de validações de domínio em Java puro, preservando as restrições
 * originalmente declaradas via Jakarta Bean Validation na API Spring.
 */
public final class Validador {

	private static final Pattern PADRAO_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	private Validador() {
	}

	public static String naoVazio(String valor, String campo) {
		if (valor == null || valor.isBlank()) {
			throw new ValidacaoException(campo + " não pode ser vazio.");
		}
		return valor;
	}

	public static <T> T naoNulo(T valor, String campo) {
		if (valor == null) {
			throw new ValidacaoException(campo + " é obrigatório.");
		}
		return valor;
	}

	public static String emailValido(String email, String campo) {
		naoVazio(email, campo);
		if (!PADRAO_EMAIL.matcher(email).matches()) {
			throw new ValidacaoException(campo + " deve ser um e-mail válido.");
		}
		return email;
	}

	public static LocalDate dataNoPassado(LocalDate data, String campo) {
		naoNulo(data, campo);
		if (!data.isBefore(LocalDate.now())) {
			throw new ValidacaoException(campo + " deve ser uma data no passado.");
		}
		return data;
	}

	public static LocalDate dataNoPassadoOuPresente(LocalDate data, String campo) {
		naoNulo(data, campo);
		if (data.isAfter(LocalDate.now())) {
			throw new ValidacaoException(campo + " deve estar no passado ou presente.");
		}
		return data;
	}

	public static BigDecimal positivo(BigDecimal valor, String campo) {
		naoNulo(valor, campo);
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidacaoException(campo + " deve ser maior que zero.");
		}
		return valor;
	}

	public static Integer positivo(Integer valor, String campo) {
		naoNulo(valor, campo);
		if (valor <= 0) {
			throw new ValidacaoException(campo + " deve ser maior que zero.");
		}
		return valor;
	}

	public static BigDecimal naoNegativo(BigDecimal valor, String campo) {
		naoNulo(valor, campo);
		if (valor.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidacaoException(campo + " deve ser maior ou igual a zero.");
		}
		return valor;
	}

	public static Integer entre(Integer valor, int min, int max, String campo) {
		naoNulo(valor, campo);
		if (valor < min || valor > max) {
			throw new ValidacaoException(campo + " deve estar entre " + min + " e " + max + ".");
		}
		return valor;
	}
}

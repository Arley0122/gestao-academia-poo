package br.edu.ifpb.academia.ui;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Auxiliar para leitura e conversão segura de entradas do usuário no terminal.
 */
public class LeitorConsole {

	private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final Scanner scanner;

	public LeitorConsole() {
		this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
	}

	public String lerTexto(String rotulo) {
		System.out.print(rotulo + ": ");
		return scanner.nextLine().trim();
	}

	public String lerTextoOpcional(String rotulo) {
		System.out.print(rotulo + " (Enter para manter/omitir): ");
		return scanner.nextLine().trim();
	}

	public int lerOpcao(String rotulo) {
		while (true) {
			System.out.print(rotulo + ": ");
			String entrada = scanner.nextLine().trim();
			try {
				return Integer.parseInt(entrada);
			} catch (NumberFormatException ex) {
				System.out.println("Valor inválido. Digite um número inteiro.");
			}
		}
	}

	public Long lerLong(String rotulo) {
		while (true) {
			System.out.print(rotulo + ": ");
			String entrada = scanner.nextLine().trim();
			try {
				return Long.parseLong(entrada);
			} catch (NumberFormatException ex) {
				System.out.println("Identificador inválido. Digite um número inteiro.");
			}
		}
	}

	public Integer lerInteiro(String rotulo) {
		return lerOpcao(rotulo);
	}

	public BigDecimal lerDecimal(String rotulo) {
		while (true) {
			System.out.print(rotulo + " (use ponto, ex: 75.50): ");
			String entrada = scanner.nextLine().trim().replace(",", ".");
			try {
				return new BigDecimal(entrada);
			} catch (NumberFormatException ex) {
				System.out.println("Número decimal inválido.");
			}
		}
	}

	public LocalDate lerData(String rotulo) {
		while (true) {
			System.out.print(rotulo + " (dd/MM/yyyy ou yyyy-MM-dd): ");
			String entrada = scanner.nextLine().trim();
			LocalDate data = tentarConverterData(entrada);
			if (data != null) {
				return data;
			}
			System.out.println("Data inválida. Use dd/MM/yyyy ou yyyy-MM-dd.");
		}
	}

	public LocalDate lerDataOpcional(String rotulo) {
		while (true) {
			System.out.print(rotulo + " (dd/MM/yyyy ou yyyy-MM-dd, Enter para vazio): ");
			String entrada = scanner.nextLine().trim();
			if (entrada.isEmpty()) {
				return null;
			}
			LocalDate data = tentarConverterData(entrada);
			if (data != null) {
				return data;
			}
			System.out.println("Data inválida. Use dd/MM/yyyy ou yyyy-MM-dd.");
		}
	}

	public <E extends Enum<E>> E lerEnum(String rotulo, Class<E> enumClass) {
		E[] valores = enumClass.getEnumConstants();
		while (true) {
			System.out.println(rotulo + ":");
			for (int i = 0; i < valores.length; i++) {
				System.out.println("  " + (i + 1) + " - " + valores[i].name());
			}
			int escolha = lerOpcao("Escolha uma opção");
			if (escolha >= 1 && escolha <= valores.length) {
				return valores[escolha - 1];
			}
			System.out.println("Opção fora do intervalo.");
		}
	}

	public <E extends Enum<E>> E lerEnumOpcional(String rotulo, Class<E> enumClass) {
		E[] valores = enumClass.getEnumConstants();
		while (true) {
			System.out.println(rotulo + " (0 para padrão/omitir):");
			for (int i = 0; i < valores.length; i++) {
				System.out.println("  " + (i + 1) + " - " + valores[i].name());
			}
			int escolha = lerOpcao("Escolha uma opção");
			if (escolha == 0) {
				return null;
			}
			if (escolha >= 1 && escolha <= valores.length) {
				return valores[escolha - 1];
			}
			System.out.println("Opção fora do intervalo.");
		}
	}

	private LocalDate tentarConverterData(String entrada) {
		try {
			if (entrada.contains("/")) {
				return LocalDate.parse(entrada, FORMATO_BR);
			}
			return LocalDate.parse(entrada);
		} catch (DateTimeParseException ex) {
			return null;
		}
	}
}

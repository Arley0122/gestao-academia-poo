package br.edu.ifpb.academia.model;

import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa um exercício físico disponível no catálogo da academia.
 */
public class Exercicio implements Identificavel {

	private Long id;
	private String nome;
	private String musculaturaAlvo;

	public Exercicio(String nome, String musculaturaAlvo) {
		atualizarDados(nome, musculaturaAlvo);
	}

	public void atualizarDados(String nome, String musculaturaAlvo) {
		String novoNome = Validador.naoVazio(nome, "Nome do exercício");
		String novaMusculatura = Validador.naoVazio(musculaturaAlvo, "Musculatura alvo");

		this.nome = novoNome;
		this.musculaturaAlvo = novaMusculatura;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public String getMusculaturaAlvo() {
		return musculaturaAlvo;
	}

	@Override
	public String toString() {
		return "Exercicio{id=" + id + ", nome='" + nome + "', musculaturaAlvo='" + musculaturaAlvo + "'}";
	}
}

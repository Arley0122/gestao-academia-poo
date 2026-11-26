package br.edu.ifpb.academia.model;

import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa uma unidade física da rede de academias.
 */
public class Unidade implements Identificavel {

	private Long id;
	private String nome;
	private String endereco;
	private String cnpj;
	private String telefone;

	public Unidade(String nome, String endereco, String cnpj, String telefone) {
		atualizarDados(nome, endereco, cnpj, telefone);
	}

	public void atualizarDados(String nome, String endereco, String cnpj, String telefone) {
		String novoNome = Validador.naoVazio(nome, "Nome da unidade");
		String novoEndereco = Validador.naoVazio(endereco, "Endereço");
		String novoCnpj = Validador.naoVazio(cnpj, "CNPJ");
		String novoTelefone = Validador.naoVazio(telefone, "Telefone");

		this.nome = novoNome;
		this.endereco = novoEndereco;
		this.cnpj = novoCnpj;
		this.telefone = novoTelefone;
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

	public String getEndereco() {
		return endereco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	@Override
	public String toString() {
		return "Unidade{id=" + id + ", nome='" + nome + "', cnpj='" + cnpj + "', telefone='" + telefone + "'}";
	}
}

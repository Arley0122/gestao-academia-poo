package br.edu.ifpb.academia.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa o aluno matriculado na academia e seu perfil de treino.
 */
public class Aluno implements Identificavel {

	private Long id;
	private String nome;
	private String cpf;
	private String telefone;
	private String email;
	private LocalDate dataNascimento;
	private BigDecimal peso;
	private BigDecimal altura;
	private NivelExperiencia nivelExperiencia;
	private Integer frequenciaSemanal;
	private StatusAluno status = StatusAluno.ATIVO;

	public Aluno(String nome, String cpf, String telefone, String email, LocalDate dataNascimento,
			BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal) {
		this(nome, cpf, telefone, email, dataNascimento, peso, altura, nivelExperiencia, frequenciaSemanal,
				StatusAluno.ATIVO);
	}

	public Aluno(String nome, String cpf, String telefone, String email, LocalDate dataNascimento,
			BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			StatusAluno status) {
		atualizarDados(nome, cpf, telefone, email, dataNascimento, peso, altura, nivelExperiencia,
				frequenciaSemanal, status);
	}

	/**
	 * Atualiza os dados cadastrais e físicos do aluno após validação.
	 *
	 * @return {@code true} se o nível de experiência ou a frequência semanal foram alterados.
	 */
	public boolean atualizarDados(String nome, String cpf, String telefone, String email, LocalDate dataNascimento,
			BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			StatusAluno status) {
		Validador.naoVazio(nome, "Nome");
		Validador.naoVazio(cpf, "CPF");
		Validador.naoVazio(telefone, "Telefone");
		Validador.emailValido(email, "E-mail");
		Validador.dataNoPassado(dataNascimento, "Data de nascimento");
		Validador.positivo(peso, "Peso");
		Validador.positivo(altura, "Altura");
		Validador.naoNulo(nivelExperiencia, "Nível de experiência");
		Validador.entre(frequenciaSemanal, 3, 6, "Frequência semanal");

		boolean perfilMudou = this.nivelExperiencia != null
				&& (!this.nivelExperiencia.equals(nivelExperiencia)
						|| !this.frequenciaSemanal.equals(frequenciaSemanal));

		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.peso = peso;
		this.altura = altura;
		this.nivelExperiencia = nivelExperiencia;
		this.frequenciaSemanal = frequenciaSemanal;
		this.status = status == null ? StatusAluno.ATIVO : status;
		return perfilMudou;
	}

	public void ativar() {
		this.status = StatusAluno.ATIVO;
	}

	public void inativar() {
		this.status = StatusAluno.INATIVO;
	}

	public boolean isAtivo() {
		return status == StatusAluno.ATIVO;
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

	public String getCpf() {
		return cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public NivelExperiencia getNivelExperiencia() {
		return nivelExperiencia;
	}

	public Integer getFrequenciaSemanal() {
		return frequenciaSemanal;
	}

	public StatusAluno getStatus() {
		return status;
	}

	public void setStatus(StatusAluno status) {
		this.status = Validador.naoNulo(status, "Status do aluno");
	}

	@Override
	public String toString() {
		return "Aluno{id=" + id + ", nome='" + nome + "', cpf='" + cpf + "', nivel=" + nivelExperiencia
				+ ", freq=" + frequenciaSemanal + "x/sem, status=" + status + "}";
	}
}

package br.edu.ifpb.academia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.util.Validador;

/**
 * Entidade que representa um programa de treinamento definido por nível de experiência
 * e frequência semanal, composto por dias de treino ordenados.
 */
public class ProgramaTreino implements Identificavel {

	private Long id;
	private NivelExperiencia nivelExperiencia;
	private Integer frequenciaSemanal;
	private TipoEstrutural tipoEstrutural;
	private String descricao;
	private final List<DiaTreino> diasTreino = new ArrayList<>();

	public ProgramaTreino(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			TipoEstrutural tipoEstrutural, String descricao) {
		atualizarDados(nivelExperiencia, frequenciaSemanal, tipoEstrutural, descricao);
	}

	public void atualizarDados(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			TipoEstrutural tipoEstrutural, String descricao) {
		NivelExperiencia novoNivel = Validador.naoNulo(nivelExperiencia, "Nível de experiência");
		Integer novaFrequencia = Validador.entre(frequenciaSemanal, 3, 6, "Frequência semanal");
		TipoEstrutural novoTipo = Validador.naoNulo(tipoEstrutural, "Tipo estrutural");
		String novaDescricao = Validador.naoVazio(descricao, "Descrição do programa");

		this.nivelExperiencia = novoNivel;
		this.frequenciaSemanal = novaFrequencia;
		this.tipoEstrutural = novoTipo;
		this.descricao = novaDescricao;
	}

	public void adicionarDiaTreino(DiaTreino dia) {
		if (dia != null && !diasTreino.contains(dia)) {
			diasTreino.add(dia);
		}
	}

	public void removerDiaTreino(DiaTreino dia) {
		diasTreino.remove(dia);
	}

	public List<DiaTreino> getDiasTreino() {
		List<DiaTreino> ordenados = new ArrayList<>(diasTreino);
		ordenados.sort(Comparator.comparing(DiaTreino::getOrdem));
		return Collections.unmodifiableList(ordenados);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public NivelExperiencia getNivelExperiencia() {
		return nivelExperiencia;
	}

	public Integer getFrequenciaSemanal() {
		return frequenciaSemanal;
	}

	public TipoEstrutural getTipoEstrutural() {
		return tipoEstrutural;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return "ProgramaTreino{id=" + id + ", nivel=" + nivelExperiencia + ", freq=" + frequenciaSemanal
				+ "x/sem, tipo=" + tipoEstrutural + ", descricao='" + descricao + "'}";
	}
}

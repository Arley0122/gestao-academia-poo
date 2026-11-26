package br.edu.ifpb.academia.model;

/**
 * Objeto de valor imutável representando a decisão de acesso do aluno na catraca.
 */
public record ResultadoAcesso(Long alunoId, boolean liberado, String motivo) {

	@Override
	public String toString() {
		return "ResultadoAcesso{alunoId=" + alunoId + ", liberado=" + liberado + ", motivo='" + motivo + "'}";
	}
}

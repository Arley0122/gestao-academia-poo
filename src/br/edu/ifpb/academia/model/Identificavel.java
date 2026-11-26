package br.edu.ifpb.academia.model;

/**
 * Contrato comum para entidades persistíveis com identificador numérico.
 */
public interface Identificavel {

	Long getId();

	void setId(Long id);
}

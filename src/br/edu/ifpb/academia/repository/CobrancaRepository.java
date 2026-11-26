package br.edu.ifpb.academia.repository;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.model.Cobranca;

public interface CobrancaRepository extends Repositorio<Cobranca> {

	List<Cobranca> buscarPorStatusEVencimentoAnterior(StatusCobranca status, LocalDate dataLimite);

	List<Cobranca> buscarPorStatusEVencimentoEntre(StatusCobranca status, LocalDate inicio, LocalDate fim);

	boolean existePorContratoAlunoIdEStatus(Long alunoId, StatusCobranca status);
}

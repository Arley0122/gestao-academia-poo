package br.edu.ifpb.academia.service;

import br.edu.ifpb.academia.model.Cobranca;

/**
 * Contrato de notificação para eventos financeiros do aluno.
 */
public interface NotificacaoService {

	void vencimentoProximo(Cobranca cobranca);

	void cobrancaAtrasada(Cobranca cobranca);
}

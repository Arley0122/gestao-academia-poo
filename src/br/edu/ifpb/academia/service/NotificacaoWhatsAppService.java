package br.edu.ifpb.academia.service;

import br.edu.ifpb.academia.model.Cobranca;

public class NotificacaoWhatsAppService implements NotificacaoService {

	@Override
	public void vencimentoProximo(Cobranca cobranca) {
		System.out.println("WhatsApp: vencimento próximo para " + cobranca.getContrato().getAluno().getTelefone());
	}

	@Override
	public void cobrancaAtrasada(Cobranca cobranca) {
		System.out.println("WhatsApp: cobrança atrasada para " + cobranca.getContrato().getAluno().getTelefone());
	}
}

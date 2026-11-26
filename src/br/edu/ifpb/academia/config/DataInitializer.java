package br.edu.ifpb.academia.config;

import java.math.BigDecimal;
import java.util.List;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.model.Plano;
import br.edu.ifpb.academia.model.ProgramaTreino;
import br.edu.ifpb.academia.repository.PlanoRepository;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;

/**
 * Carga inicial dos 5 planos e 12 programas de treino padrão da academia.
 */
public class DataInitializer {

	private final PlanoRepository planoRepository;
	private final ProgramaTreinoRepository programaTreinoRepository;

	public DataInitializer(PlanoRepository planoRepository, ProgramaTreinoRepository programaTreinoRepository) {
		this.planoRepository = planoRepository;
		this.programaTreinoRepository = programaTreinoRepository;
	}

	public void inicializar() {
		criarPlanos();
		criarProgramas();
	}

	private void criarPlanos() {
		List<Plano> planos = List.of(
				plano(TipoPlano.DIARIO, "20.00", 1, "0.00", "Acesso diário a qualquer unidade"),
				plano(TipoPlano.MENSAL, "120.00", 30, "0.00", "Acesso mensal a todas as unidades"),
				plano(TipoPlano.TRIMESTRAL, "330.00", 90, "8.33", "Plano trimestral com desconto"),
				plano(TipoPlano.SEMESTRAL, "600.00", 180, "16.67", "Plano semestral com desconto"),
				plano(TipoPlano.ANUAL, "1080.00", 365, "25.00", "Plano anual com maior desconto"));
		for (Plano plano : planos) {
			if (!planoRepository.existePorTipo(plano.getTipo())) {
				planoRepository.salvar(plano);
			}
		}
	}

	private void criarProgramas() {
		for (NivelExperiencia nivel : NivelExperiencia.values()) {
			criarPrograma(nivel, 3, TipoEstrutural.FULLBODY);
			criarPrograma(nivel, 4, TipoEstrutural.UPPER_LOWER);
			criarPrograma(nivel, 5, TipoEstrutural.PPL_UPPER_LOWER);
			criarPrograma(nivel, 6, TipoEstrutural.PPL_2X);
		}
	}

	private void criarPrograma(NivelExperiencia nivel, Integer frequencia, TipoEstrutural tipo) {
		if (!programaTreinoRepository.existePorNivelEFrequencia(nivel, frequencia)) {
			String descricao = "Programa " + tipo.name() + " para " + nivel.name()
					+ " com frequência de " + frequencia + " dias por semana";
			ProgramaTreino programa = new ProgramaTreino(nivel, frequencia, tipo, descricao);
			programaTreinoRepository.salvar(programa);
		}
	}

	private Plano plano(TipoPlano tipo, String valor, Integer duracao, String desconto, String descricao) {
		return new Plano(tipo, new BigDecimal(valor), duracao, new BigDecimal(desconto), descricao);
	}
}

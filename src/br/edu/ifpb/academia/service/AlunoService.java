package br.edu.ifpb.academia.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.model.Aluno;
import br.edu.ifpb.academia.repository.AlunoRepository;

public class AlunoService {

	private final AlunoRepository repository;
	private final TreinoAtivoService treinoAtivoService;

	public AlunoService(AlunoRepository repository, TreinoAtivoService treinoAtivoService) {
		this.repository = repository;
		this.treinoAtivoService = treinoAtivoService;
	}

	public List<Aluno> listar() {
		return repository.listarTodos();
	}

	public Aluno buscar(Long id) {
		return buscarEntidade(id);
	}

	public Aluno criar(String nome, String cpf, String telefone, String email, LocalDate dataNascimento,
			BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			StatusAluno status) {
		Aluno aluno = new Aluno(nome, cpf, telefone, email, dataNascimento, peso, altura, nivelExperiencia,
				frequenciaSemanal, status);
		if (repository.existePorCpf(aluno.getCpf())) {
			throw new RegraDeNegocioException("Já existe aluno com este CPF.");
		}
		if (repository.existePorEmail(aluno.getEmail())) {
			throw new RegraDeNegocioException("Já existe aluno com este e-mail.");
		}
		aluno = repository.salvar(aluno);
		try {
			treinoAtivoService.sincronizar(aluno);
		} catch (RuntimeException ex) {
			repository.excluir(aluno);
			throw ex;
		}
		return aluno;
	}

	public Aluno atualizar(Long id, String nome, String cpf, String telefone, String email, LocalDate dataNascimento,
			BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			StatusAluno status) {
		Aluno aluno = buscarEntidade(id);
		if (cpf != null && !aluno.getCpf().equals(cpf) && repository.existePorCpf(cpf)) {
			throw new RegraDeNegocioException("Já existe aluno com este CPF.");
		}
		if (email != null && !aluno.getEmail().equals(email) && repository.existePorEmail(email)) {
			throw new RegraDeNegocioException("Já existe aluno com este e-mail.");
		}
		boolean perfilMudou = aluno.atualizarDados(nome, cpf, telefone, email, dataNascimento, peso, altura,
				nivelExperiencia, frequenciaSemanal, status);
		aluno = repository.salvar(aluno);
		if (perfilMudou) {
			treinoAtivoService.sincronizar(aluno);
		}
		return aluno;
	}

	public void excluir(Long id) {
		repository.excluir(buscarEntidade(id));
	}

	public Aluno buscarEntidade(Long id) {
		return repository.buscarPorId(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
	}

	public void ativar(Aluno aluno) {
		aluno.ativar();
		repository.salvar(aluno);
	}

	public void inativar(Aluno aluno) {
		aluno.inativar();
		repository.salvar(aluno);
	}
}

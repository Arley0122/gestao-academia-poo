# Diagramas de Sequência — Sistema de Gestão de Academia (POO)

Abaixo estão os 6 principais fluxos de sequência alinhados diretamente aos métodos e interações entre objetos implementados em `POO/src/`.

---

## 1. Cadastrar Aluno e Gerar Treino Ativo

```mermaid
sequenceDiagram
    actor Recepcionista
    participant Menu as MenuConsole
    participant AS as AlunoService
    participant A as Aluno
    participant AR as AlunoRepository
    participant TAS as TreinoAtivoService
    participant PTS as ProgramaTreinoService
    participant TAR as TreinoAtivoRepository

    Recepcionista->>Menu: Informar dados do aluno
    Menu->>AS: criar(nome, cpf, ..., nivel, frequencia, status)
    AS->>A: new Aluno(...) [valida dados]
    AS->>AR: existePorCpf(cpf)
    AR-->>AS: false
    AS->>AR: existePorEmail(email)
    AR-->>AS: false
    AS->>AR: salvar(aluno)
    AR-->>AS: aluno (com ID)
    AS->>TAS: sincronizar(aluno)
    TAS->>PTS: buscarPorPerfil(nivel, frequencia)
    PTS-->>TAS: programa
    TAS->>TAR: buscarPorAlunoIdEStatus(alunoId, ATIVO)
    TAR-->>TAS: Optional.empty()
    TAS->>TAR: salvar(new TreinoAtivo(aluno, programa, hoje))
    TAR-->>TAS: treinoAtivo
    TAS-->>AS: void
    AS-->>Menu: aluno
    Menu-->>Recepcionista: Exibe aluno criado
```

---

## 2. Atualizar Perfil do Aluno e Resincronizar Treino

```mermaid
sequenceDiagram
    actor Recepcionista
    participant Menu as MenuConsole
    participant AS as AlunoService
    participant AR as AlunoRepository
    participant A as Aluno
    participant TAS as TreinoAtivoService
    participant PTS as ProgramaTreinoService
    participant TAR as TreinoAtivoRepository
    participant TA as TreinoAtivo

    Recepcionista->>Menu: Atualizar nível/frequência do aluno
    Menu->>AS: atualizar(id, ..., novoNivel, novaFrequencia, status)
    AS->>AR: buscarPorId(id)
    AR-->>AS: aluno
    AS->>A: atualizarDados(...)
    A-->>AS: perfilMudou = true
    AS->>AR: salvar(aluno)
    AR-->>AS: aluno
    opt perfilMudou == true
        AS->>TAS: sincronizar(aluno)
        TAS->>PTS: buscarPorPerfil(novoNivel, novaFrequencia)
        PTS-->>TAS: novoPrograma
        TAS->>TAR: buscarPorAlunoIdEStatus(id, ATIVO)
        TAR-->>TAS: treinoAtual
        alt !treinoAtual.pertenceAoPrograma(novoPrograma)
            TAS->>TA: encerrar(hoje)
            TAS->>TAR: salvar(treinoAtual)
        end
        TAS->>TAR: buscarPorAlunoIdEStatus(id, ATIVO)
        TAR-->>TAS: Optional.empty()
        TAS->>TAR: salvar(new TreinoAtivo(aluno, novoPrograma, hoje))
    end
    AS-->>Menu: aluno atualizado
```

---

## 3. Contratar Plano (com Cobrança, Ativação e Treino)

```mermaid
sequenceDiagram
    actor Recepcionista
    participant Menu as MenuConsole
    participant CS as ContratoService
    participant CR as ContratoRepository
    participant C as Contrato
    participant CobS as CobrancaService
    participant CobR as CobrancaRepository
    participant AS as AlunoService
    participant TAS as TreinoAtivoService

    Recepcionista->>Menu: Contratar plano para aluno
    Menu->>CS: criar(alunoId, unidadeId, planoId, dataInicio, status)
    CS->>CR: existePorAlunoIdEStatus(alunoId, ATIVO)
    CR-->>CS: false
    CS->>AS: buscarEntidade(alunoId)
    AS-->>CS: aluno
    Note over CS: Também busca Unidade e Plano
    CS->>C: new Contrato(aluno, unidade, plano, dataInicio, status)
    Note over C: recalcularDataTermino() = dataInicio + plano.duracao
    CS->>CR: salvar(contrato)
    CR-->>CS: contrato
    CS->>CobS: gerarParaContrato(contrato)
    CobS->>CobR: salvar(new Cobranca(contrato, dataInicio))
    CobR-->>CobS: cobranca (PENDENTE)
    CS->>AS: ativar(aluno)
    AS-->>CS: aluno ATIVO
    CS->>TAS: sincronizar(aluno)
    TAS-->>CS: void
    CS-->>Menu: contrato criado
```

---

## 4. Registrar Pagamento de Cobrança

```mermaid
sequenceDiagram
    actor Recepcionista
    participant Menu as MenuConsole
    participant CobS as CobrancaService
    participant CobR as CobrancaRepository
    participant Cob as Cobranca

    Recepcionista->>Menu: Registrar pagamento (id, data, forma)
    Menu->>CobS: pagar(id, dataPagamento, formaPagamento)
    CobS->>CobR: buscarPorId(id)
    CobR-->>CobS: cobranca
    CobS->>Cob: registrarPagamento(dataPagamento, formaPagamento)
    alt cobranca.isPaga()
        Cob-->>CobS: throws RegraDeNegocioException
    else pendente ou atrasada
        Note over Cob: Valida data <= hoje e define status = PAGO
        Cob-->>CobS: ok
        CobS->>CobR: salvar(cobranca)
        CobR-->>CobS: cobranca
        CobS-->>Menu: cobranca paga
    end
```

---

## 5. Verificar Acesso na Catraca

```mermaid
sequenceDiagram
    actor Catraca
    participant AcS as AcessoService
    participant CobS as CobrancaService
    participant CobR as CobrancaRepository
    participant Cob as Cobranca
    participant NS as NotificacaoService
    participant AS as AlunoService
    participant A as Aluno
    participant CR as ContratoRepository

    Catraca->>AcS: verificar(alunoId)
    AcS->>CobS: atualizarAtrasadas()
    CobS->>CobR: buscarPorStatusEVencimentoAnterior(PENDENTE, hoje - 2)
    CobR-->>CobS: vencidas
    loop para cada cobrança vencida
        CobS->>Cob: marcarComoAtrasada()
        CobS->>NS: cobrancaAtrasada(cobranca)
    end
    CobS->>CobR: salvarTodos(vencidas)
    CobS-->>AcS: void

    AcS->>AS: buscarEntidade(alunoId)
    AS-->>AcS: aluno
    AcS->>A: isAtivo()
    alt aluno inativo
        A-->>AcS: false
        AcS-->>Catraca: throws RegraDeNegocioException("Aluno inativo. Acesso negado.")
    else aluno ativo
        A-->>AcS: true
        AcS->>CR: buscarPrimeiroPorAlunoIdEStatusOrdenadoPorInicioDesc(alunoId, ATIVO)
        alt sem contrato ativo
            CR-->>AcS: Optional.empty()
            AcS-->>Catraca: ResultadoAcesso(alunoId, false, "Aluno sem contrato ativo.")
        else possui contrato ativo
            CR-->>AcS: contrato
            AcS->>CobR: existePorContratoAlunoIdEStatus(alunoId, ATRASADO)
            alt possui cobrança atrasada
                CobR-->>AcS: true
                AcS-->>Catraca: ResultadoAcesso(alunoId, false, "Aluno possui cobrança atrasada.")
            else em dia
                CobR-->>AcS: false
                AcS-->>Catraca: ResultadoAcesso(alunoId, true, "Acesso liberado.")
            end
        end
    end
```

---

## 6. Cancelar Contrato e Inativar Aluno

```mermaid
sequenceDiagram
    actor Recepcionista
    participant Menu as MenuConsole
    participant CS as ContratoService
    participant CR as ContratoRepository
    participant C as Contrato
    participant AS as AlunoService
    participant A as Aluno

    Recepcionista->>Menu: Cancelar contrato (id)
    Menu->>CS: cancelar(id)
    CS->>CR: buscarPorId(id)
    CR-->>CS: contrato
    CS->>C: cancelar()
    alt !contrato.isAtivo()
        C-->>CS: throws RegraDeNegocioException
    else contrato ativo
        Note over C: status = CANCELADO
        C->>A: inativar()
        C-->>CS: ok
        CS->>AS: inativar(aluno)
        CS->>CR: salvar(contrato)
        CR-->>CS: contrato
        CS-->>Menu: contrato cancelado
    end
```

# Diagrama de Classes — Sistema de Gestão de Academia (POO)

Os diagramas abaixo refletem **1:1** as classes, interfaces, enumerações, atributos, métodos e multiplicidades implementadas em `POO/src/`.

## 1. Diagrama de Domínio e Enumerações (Mermaid)

```mermaid
classDiagram
    class Identificavel {
        <<interface>>
        +getId() Long
        +setId(Long id) void
    }

    class Aluno {
        -Long id
        -String nome
        -String cpf
        -String telefone
        -String email
        -LocalDate dataNascimento
        -BigDecimal peso
        -BigDecimal altura
        -NivelExperiencia nivelExperiencia
        -Integer frequenciaSemanal
        -StatusAluno status
        +atualizarDados(...) boolean
        +ativar() void
        +inativar() void
        +isAtivo() boolean
    }

    class Unidade {
        -Long id
        -String nome
        -String endereco
        -String cnpj
        -String telefone
        +atualizarDados(...) void
    }

    class Plano {
        -Long id
        -TipoPlano tipo
        -BigDecimal valor
        -Integer duracao
        -BigDecimal percentualDesconto
        -String descricao
        +atualizarDados(...) void
        +isPlanoLongo() boolean
    }

    class Contrato {
        -Long id
        -Aluno aluno
        -Unidade unidade
        -Plano plano
        -LocalDate dataInicio
        -LocalDate dataTermino
        -StatusContrato status
        +atualizarDados(...) void
        +setPlano(Plano plano) void
        +setDataInicio(LocalDate dataInicio) void
        +isAtivo() boolean
        +cancelar() void
        -recalcularDataTermino() void
    }

    class Cobranca {
        -Long id
        -Contrato contrato
        -LocalDate dataVencimento
        -BigDecimal valor
        -LocalDate dataPagamento
        -FormaPagamento formaPagamento
        -StatusCobranca status
        +atualizarDados(...) void
        +isPaga() boolean
        +registrarPagamento(LocalDate, FormaPagamento) void
        +marcarComoAtrasada() void
        +estaVencidaAposCarencia(LocalDate) boolean
        +estaComVencimentoProximo(LocalDate, LocalDate) boolean
    }

    class ProgramaTreino {
        -Long id
        -NivelExperiencia nivelExperiencia
        -Integer frequenciaSemanal
        -TipoEstrutural tipoEstrutural
        -String descricao
        -List~DiaTreino~ diasTreino
        +atualizarDados(...) void
        +adicionarDiaTreino(DiaTreino) void
        +removerDiaTreino(DiaTreino) void
        +getDiasTreino() List~DiaTreino~
    }

    class DiaTreino {
        -Long id
        -ProgramaTreino programaTreino
        -String nomeDia
        -Integer ordem
        -List~DiaTreinoExercicio~ exercicios
        +atualizarDados(...) void
        +adicionarExercicio(DiaTreinoExercicio) void
        +removerExercicio(DiaTreinoExercicio) void
        +getExercicios() List~DiaTreinoExercicio~
    }

    class Exercicio {
        -Long id
        -String nome
        -String musculaturaAlvo
        +atualizarDados(...) void
    }

    class DiaTreinoExercicio {
        -Long id
        -DiaTreino diaTreino
        -Exercicio exercicio
        -Integer series
        -Integer repeticoes
        -Integer ordem
        +atualizarDados(...) void
    }

    class TreinoAtivo {
        -Long id
        -Aluno aluno
        -ProgramaTreino programaTreino
        -LocalDate dataInicio
        -LocalDate dataFim
        -StatusTreinoAtivo status
        +isAtivo() boolean
        +pertenceAoPrograma(ProgramaTreino) boolean
        +encerrar(LocalDate dataFim) void
    }

    class ResultadoAcesso {
        <<record>>
        +Long alunoId
        +boolean liberado
        +String motivo
    }

    Identificavel <|.. Aluno
    Identificavel <|.. Unidade
    Identificavel <|.. Plano
    Identificavel <|.. Contrato
    Identificavel <|.. Cobranca
    Identificavel <|.. ProgramaTreino
    Identificavel <|.. DiaTreino
    Identificavel <|.. Exercicio
    Identificavel <|.. DiaTreinoExercicio
    Identificavel <|.. TreinoAtivo

    Contrato "0..*" --> "1" Aluno
    Contrato "0..*" --> "1" Unidade
    Contrato "0..*" --> "1" Plano
    Cobranca "0..*" --> "1" Contrato
    TreinoAtivo "0..*" --> "1" Aluno
    TreinoAtivo "0..*" --> "1" ProgramaTreino
    ProgramaTreino "1" *-- "0..*" DiaTreino : possui
    DiaTreino "1" *-- "0..*" DiaTreinoExercicio : contém
    DiaTreinoExercicio "0..*" --> "1" Exercicio
```

---

## 2. Diagrama da Camada de Serviços, Repositórios e Exceções (Mermaid)

```mermaid
classDiagram
    class DominioException {
        <<abstract>>
    }
    class ValidacaoException
    class RegraDeNegocioException
    class RecursoNaoEncontradoException

    DominioException <|-- ValidacaoException
    DominioException <|-- RegraDeNegocioException
    DominioException <|-- RecursoNaoEncontradoException

    class Repositorio~T~ {
        <<interface>>
        +salvar(T) T
        +salvarTodos(List~T~) List~T~
        +buscarPorId(Long) Optional~T~
        +listarTodos() List~T~
        +excluir(T) void
    }

    class RepositorioMemoria~T~ {
        <<abstract>>
        -Map~Long, T~ armazenamento
        -long sequencia
    }

    Repositorio <|.. RepositorioMemoria

    class NotificacaoService {
        <<interface>>
        +vencimentoProximo(Cobranca) void
        +cobrancaAtrasada(Cobranca) void
    }

    class NotificacaoWhatsAppService {
        +vencimentoProximo(Cobranca) void
        +cobrancaAtrasada(Cobranca) void
    }

    NotificacaoService <|.. NotificacaoWhatsAppService

    class AlunoService {
        +listar() List~Aluno~
        +buscar(Long) Aluno
        +criar(...) Aluno
        +atualizar(...) Aluno
        +excluir(Long) void
        +ativar(Aluno) void
        +inativar(Aluno) void
    }

    class ContratoService {
        +listar() List~Contrato~
        +buscar(Long) Contrato
        +criar(...) Contrato
        +atualizar(...) Contrato
        +cancelar(Long) Contrato
        +excluir(Long) void
    }

    class CobrancaService {
        -int DIAS_CARENCIA = 2
        +listar() List~Cobranca~
        +buscar(Long) Cobranca
        +criar(...) Cobranca
        +atualizar(...) Cobranca
        +pagar(Long, LocalDate, FormaPagamento) Cobranca
        +gerarParaContrato(Contrato) void
        +atualizarAtrasadas() void
        +notificacoesAutomaticas() void
    }

    class TreinoAtivoService {
        +listar() List~TreinoAtivo~
        +buscar(Long) TreinoAtivo
        +listarPorAluno(Long) List~TreinoAtivo~
        +sincronizar(Aluno) void
    }

    class AcessoService {
        +verificar(Long alunoId) ResultadoAcesso
    }

    AlunoService --> AlunoRepository
    AlunoService --> TreinoAtivoService
    TreinoAtivoService --> TreinoAtivoRepository
    TreinoAtivoService --> ProgramaTreinoService
    ContratoService --> ContratoRepository
    ContratoService --> AlunoService
    ContratoService --> UnidadeService
    ContratoService --> PlanoService
    ContratoService --> CobrancaService
    ContratoService --> TreinoAtivoService
    CobrancaService --> CobrancaRepository
    CobrancaService --> ContratoRepository
    CobrancaService --> NotificacaoService
    AcessoService --> AlunoService
    AcessoService --> ContratoRepository
    AcessoService --> CobrancaRepository
    AcessoService --> CobrancaService
    AcessoService ..> ResultadoAcesso
```

---

## 3. Versão Completa em PlantUML (Domínio + Serviços)

```plantuml
@startuml
skinparam classAttributeIconSize 0

package "enums" {
  enum StatusAluno { ATIVO; INATIVO }
  enum StatusContrato { ATIVO; CANCELADO }
  enum StatusCobranca { PENDENTE; PAGO; ATRASADO }
  enum StatusTreinoAtivo { ATIVO; ENCERRADO }
  enum FormaPagamento { PIX; CARTAO_CREDITO; CARTAO_DEBITO; DINHEIRO }
  enum NivelExperiencia { INICIANTE; INTERMEDIARIO; AVANCADO }
  enum TipoEstrutural { FULLBODY; UPPER_LOWER; PPL_UPPER_LOWER; PPL_2X }
  enum TipoPlano { DIARIO; MENSAL; TRIMESTRAL; SEMESTRAL; ANUAL }
}

package "model" {
  interface Identificavel {
    + getId() : Long
    + setId(id : Long) : void
  }

  class Aluno {
    - id : Long
    - nome : String
    - cpf : String
    - telefone : String
    - email : String
    - dataNascimento : LocalDate
    - peso : BigDecimal
    - altura : BigDecimal
    - nivelExperiencia : NivelExperiencia
    - frequenciaSemanal : Integer
    - status : StatusAluno
    + atualizarDados(...) : boolean
    + ativar() : void
    + inativar() : void
    + isAtivo() : boolean
  }

  class Unidade {
    - id : Long
    - nome : String
    - endereco : String
    - cnpj : String
    - telefone : String
    + atualizarDados(...) : void
  }

  class Plano {
    - id : Long
    - tipo : TipoPlano
    - valor : BigDecimal
    - duracao : Integer
    - percentualDesconto : BigDecimal
    - descricao : String
    + atualizarDados(...) : void
    + isPlanoLongo() : boolean
  }

  class Contrato {
    - id : Long
    - dataInicio : LocalDate
    - dataTermino : LocalDate
    - status : StatusContrato
    + isAtivo() : boolean
    + cancelar() : void
    - recalcularDataTermino() : void
  }

  class Cobranca {
    - id : Long
    - dataVencimento : LocalDate
    - valor : BigDecimal
    - dataPagamento : LocalDate
    - formaPagamento : FormaPagamento
    - status : StatusCobranca
    + isPaga() : boolean
    + registrarPagamento(data : LocalDate, forma : FormaPagamento) : void
    + marcarComoAtrasada() : void
  }

  class ProgramaTreino {
    - id : Long
    - nivelExperiencia : NivelExperiencia
    - frequenciaSemanal : Integer
    - tipoEstrutural : TipoEstrutural
    - descricao : String
    + getDiasTreino() : List<DiaTreino>
  }

  class DiaTreino {
    - id : Long
    - nomeDia : String
    - ordem : Integer
    + getExercicios() : List<DiaTreinoExercicio>
  }

  class Exercicio {
    - id : Long
    - nome : String
    - musculaturaAlvo : String
  }

  class DiaTreinoExercicio {
    - id : Long
    - series : Integer
    - repeticoes : Integer
    - ordem : Integer
  }

  class TreinoAtivo {
    - id : Long
    - dataInicio : LocalDate
    - dataFim : LocalDate
    - status : StatusTreinoAtivo
    + isAtivo() : boolean
    + encerrar(dataFim : LocalDate) : void
  }

  class ResultadoAcesso <<record>> {
    + alunoId : Long
    + liberado : boolean
    + motivo : String
  }
}

Contrato "0..*" --> "1" Aluno
Contrato "0..*" --> "1" Unidade
Contrato "0..*" --> "1" Plano
Cobranca "0..*" --> "1" Contrato
TreinoAtivo "0..*" --> "1" Aluno
TreinoAtivo "0..*" --> "1" ProgramaTreino
ProgramaTreino "1" *-- "0..*" DiaTreino
DiaTreino "1" *-- "0..*" DiaTreinoExercicio
DiaTreinoExercicio "0..*" --> "1" Exercicio
@enduml
```

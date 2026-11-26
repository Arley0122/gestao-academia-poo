# Diagrama de Casos de Uso — Sistema de Gestão de Academia

Este documento apresenta os atores, os casos de uso e suas relações (`<<include>>` e `<<extend>>`) extraídos fielmente da lógica do projeto POO em `POO/src/`.

## 1. Atores Identificados

| Ator | Tipo | Responsabilidade Principal |
| :--- | :--- | :--- |
| **Recepcionista / Atendente** | Primário | Cadastra e atualiza alunos, contrata e cancela planos, registra pagamentos e consulta treinos ativos. |
| **Administrador / Instrutor** | Primário | Mantém unidades, planos, programas de treino, dias de treino, exercícios e vínculos de exercícios por dia. |
| **Catraca / Aluno** | Primário | Solicita liberação de entrada na academia informando o identificador do aluno. |
| **Rotina Automática (Agendador)** | Primário (Temporal) | Dispara periodicamente a verificação de cobranças próximas do vencimento e em atraso (`notificacoesAutomaticas`). |
| **Serviço WhatsApp** | Secundário (Sistema Externo) | Recebe os disparos de notificação de vencimento próximo e cobrança atrasada (`NotificacaoService`). |

---

## 2. Diagrama em PlantUML (Notação UML Padrão)

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "Recepcionista" as Recep
actor "Administrador / Instrutor" as Admin
actor "Catraca / Aluno" as Catraca
actor "Rotina Automática" as Scheduler
actor "Serviço WhatsApp" as WhatsApp

rectangle "Sistema de Gestão de Academia (POO)" {
  usecase "UC01 - Gerenciar Alunos" as UC01
  usecase "UC02 - Gerenciar Unidades" as UC02
  usecase "UC03 - Gerenciar Planos" as UC03
  usecase "UC04 - Contratar Plano" as UC04
  usecase "UC05 - Cancelar Contrato" as UC05
  usecase "UC06 - Gerenciar Cobranças" as UC06
  usecase "UC07 - Registrar Pagamento" as UC07
  usecase "UC08 - Consultar Treinos Ativos" as UC08
  usecase "UC09 - Gerenciar Programas, Dias e Exercícios" as UC09
  usecase "UC10 - Verificar Acesso na Catraca" as UC10
  usecase "UC11 - Executar Rotina de Notificações" as UC11

  usecase "Sincronizar Treino Ativo" as INC_TREINO
  usecase "Gerar Cobrança Inicial" as INC_COBRANCA
  usecase "Ativar / Inativar Aluno" as INC_STATUS
  usecase "Atualizar Cobranças Atrasadas" as INC_ATRASO
  usecase "Enviar Notificação WhatsApp" as EXT_WHATS
}

Recep --> UC01
Recep --> UC04
Recep --> UC05
Recep --> UC06
Recep --> UC07
Recep --> UC08

Admin --> UC02
Admin --> UC03
Admin --> UC09

Catraca --> UC10
Scheduler --> UC11

UC01 ..> INC_TREINO : <<include>>\n(criação ou mudança de perfil)
UC04 ..> INC_COBRANCA : <<include>>
UC04 ..> INC_STATUS : <<include>> (ativar)
UC04 ..> INC_TREINO : <<include>>
UC05 ..> INC_STATUS : <<include>> (inativar)

UC10 ..> INC_ATRASO : <<include>>
UC11 ..> INC_ATRASO : <<include>>
EXT_WHATS ..> INC_ATRASO : <<extend>>\n(se houver atrasos)
EXT_WHATS ..> UC11 : <<extend>>\n(vencimento próximo)
EXT_WHATS --> WhatsApp

@enduml
```

---

## 3. Visão Rápida em Mermaid

```mermaid
flowchart LR
    Recep["Recepcionista"]
    Admin["Administrador / Instrutor"]
    Catraca["Catraca / Aluno"]
    Sched["Rotina Automática"]
    Whats["Serviço WhatsApp"]

    subgraph Sistema["Sistema de Gestão de Academia"]
        UC01(["UC01 - Gerenciar Alunos"])
        UC02(["UC02 - Gerenciar Unidades"])
        UC03(["UC03 - Gerenciar Planos"])
        UC04(["UC04 - Contratar Plano"])
        UC05(["UC05 - Cancelar Contrato"])
        UC06(["UC06 - Gerenciar Cobranças"])
        UC07(["UC07 - Registrar Pagamento"])
        UC08(["UC08 - Consultar Treinos Ativos"])
        UC09(["UC09 - Programas, Dias e Exercícios"])
        UC10(["UC10 - Verificar Acesso na Catraca"])
        UC11(["UC11 - Rotina de Notificações"])
        INC1(["Sincronizar Treino Ativo"])
        INC2(["Gerar Cobrança Inicial"])
        INC3(["Atualizar Cobranças Atrasadas"])
    end

    Recep --> UC01
    Recep --> UC04
    Recep --> UC05
    Recep --> UC06
    Recep --> UC07
    Recep --> UC08
    Admin --> UC02
    Admin --> UC03
    Admin --> UC09
    Catraca --> UC10
    Sched --> UC11

    UC01 -. "<<include>>" .-> INC1
    UC04 -. "<<include>>" .-> INC1
    UC04 -. "<<include>>" .-> INC2
    UC10 -. "<<include>>" .-> INC3
    UC11 -. "<<include>>" .-> INC3
    INC3 -. "<<extend>>" .-> Whats
    UC11 -. "<<extend>>" .-> Whats
```

---

## 4. Resumo dos Principais Casos de Uso e Regras

| Código | Caso de Uso | Regras / Fluxos Principais | Exceções Previstas |
| :--- | :--- | :--- | :--- |
| **UC01** | Gerenciar Alunos | Valida dados obrigatórios, CPF/e-mail únicos; na criação e na mudança de nível/frequência, sincroniza `TreinoAtivo`. | `ValidacaoException`, `RegraDeNegocioException`, `RecursoNaoEncontradoException` |
| **UC02** | Gerenciar Unidades | CRUD de unidade física com unicidade de CNPJ. | `ValidacaoException`, `RegraDeNegocioException`, `RecursoNaoEncontradoException` |
| **UC03** | Gerenciar Planos | CRUD de planos com tipo único e exigência de desconto progressivo (> 0) para `TRIMESTRAL`, `SEMESTRAL` e `ANUAL`. | `ValidacaoException`, `RegraDeNegocioException`, `RecursoNaoEncontradoException` |
| **UC04** | Contratar Plano | Garante no máximo 1 contrato `ATIVO` por aluno, calcula `dataTermino = dataInicio + duracao`, gera cobrança `PENDENTE`, ativa o aluno e sincroniza treino. | `RegraDeNegocioException`, `RecursoNaoEncontradoException`, `ValidacaoException` |
| **UC05** | Cancelar Contrato | Marca contrato como `CANCELADO` e inativa o aluno vinculado. | `RegraDeNegocioException` (já cancelado), `RecursoNaoEncontradoException` |
| **UC06/07** | Cobranças e Pagamento | Valor sempre derivado do `Plano` do contrato; pagamento exige data válida (passado/presente) e forma de pagamento, impedindo pagamento duplicado. | `RegraDeNegocioException`, `ValidacaoException`, `RecursoNaoEncontradoException` |
| **UC10** | Verificar Acesso | Atualiza cobranças vencidas há mais de 2 dias (`ATRASADO`); exige aluno `ATIVO`, contrato `ATIVO` e ausência de cobrança `ATRASADO`. | `RegraDeNegocioException` (aluno inativo), `RecursoNaoEncontradoException` |
| **UC11** | Rotina de Notificações | Notifica cobranças `PENDENTE` com vencimento entre hoje e `hoje + 2` e atualiza cobranças atrasadas. | — |

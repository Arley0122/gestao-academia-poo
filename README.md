# Sistema de Gestão de Academia — Projeto Puramente POO

Conversão da API REST em Spring Boot ([gestao-academia-spring](../gestao-academia-spring)) para um projeto Java **puramente orientado a objetos (POO)**, sem frameworks ou dependências externas, preservando integralmente toda a lógica de negócio para apoio à disciplina de **Análise e Projeto de Sistemas**.

## Mapeamento da Conversão (Spring -> POO)

| Camada na API Spring | Equivalente no Projeto POO |
| :--- | :--- |
| `@Entity` + Lombok | Entidades ricas em `model/` com construtores explícitos, encapsulamento, validações e métodos de domínio |
| `jakarta.validation` nos `*Request` | `util/Validador.java` + `exception/ValidacaoException.java` |
| `@RestControllerAdvice` | Hierarquia `DominioException` (`ValidacaoException`, `RegraDeNegocioException`, `RecursoNaoEncontradoException`) |
| `JpaRepository` + PostgreSQL | Interfaces em `repository/` + implementações em memória (`repository/memoria/RepositorioMemoria`) |
| `@Service` / `@Transactional` | Serviços em `service/` com atomicidade em memória na criação de aluno/contrato |
| `@RestController` / Swagger | Interface interativa de terminal (`ui/MenuConsole`) + suíte de testes (`test/VerificadorFluxos`) |
| `DataInitializer` (`CommandLineRunner`) | `config/DataInitializer` invocado por `config/SistemaAcademiaContexto` |

## Estrutura dos Documentos UML (`docs/`)

- [Diagrama de Casos de Uso](docs/diagrama-casos-de-uso.md)
- [Diagrama de Classes](docs/diagrama-classes.md)
- [Diagramas de Sequência](docs/diagramas-sequencia.md)

## Como Compilar e Executar (Java 17+)

No diretório raiz do workspace (`gestão de academia`):

```powershell
New-Item -ItemType Directory -Force -Path "POO\out" | Out-Null
$files = Get-ChildItem -Path "POO\src" -Recurse -Filter "*.java" | ForEach-Object { '"' + ($_.FullName -replace '\\','/') + '"' }
[System.IO.File]::WriteAllLines((Resolve-Path "POO").Path + "\sources.txt", $files, [System.Text.UTF8Encoding]::new($false))
javac -encoding UTF-8 -d "POO\out" "@POO\sources.txt"
Remove-Item "POO\sources.txt"
```

### Executar Suíte Automatizada de Fluxos

```powershell
java -cp "POO\out" br.edu.ifpb.academia.Main --test
```

### Executar Menu Interativo no Console

```powershell
java -cp "POO\out" br.edu.ifpb.academia.Main
```

# Locadora de Video Games (API REST — Spring Boot)

API REST para gestão de uma locadora: cadastro de usuários, login, jogos, clientes e locações (com devolução). O banco é **H2 em memória**: ao parar a aplicação, os dados somem.

**Trabalho acadêmico (Unisales):** checklist de conformidade ao enunciado, esqueleto do artigo (GitHub, descrição, arquitetura, vulnerabilidades, SonarQube, ZAP) e **o que cada pasta do projeto faz** está em [`docs/ARTIGO_BASE_UNISALES.md`](docs/ARTIGO_BASE_UNISALES.md).

---

## 1. O que você precisa

| Item | Observação |
|------|------------|
| **Java** | 21 ou superior (no ambiente de teste rodou com Java 22) |
| **Maven** | Instalado no PATH ou integrado à IDE (IntelliJ, Eclipse, VS Code) |
| **Rede** | Apenas para baixar dependências na primeira execução |

---

## 2. Como executar o projeto

No diretório raiz do projeto (`Trabalho1`):

```bash
mvn spring-boot:run
```

- A API sobe em: **http://localhost:8080**
- Para compilar sem subir o servidor:

```bash
mvn -DskipTests package
```

**Pela IDE:** abra a pasta como projeto Maven e execute a classe  
`br.com.unisales.locadora.LocadoraApplication`.

---

## 3. Fluxo sugerido de uso (ordem lógica)

1. Cadastrar um **usuário** → `POST /usuarios`
2. Fazer **login** → `POST /login` (retorna um token fictício `dummy-...`)
3. Cadastrar **clientes** → `POST /clientes`
4. Cadastrar **jogos** → `POST /jogos`
5. Criar **locação** → `POST /locacoes`
6. Registrar **devolução** → `PUT /locacoes/{id}/devolucao`

Os endpoints de jogos e clientes podem ser usados em qualquer ordem após existir cliente e jogo para a locação.

---

## 4. Endpoints e corpos JSON

### 4.1 Usuários

**`POST /usuarios`** — cadastro  

| Campo | Tipo | Regras |
|-------|------|--------|
| `nome` | string | obrigatório, 2–120 caracteres |
| `email` | string | obrigatório, e-mail válido |
| `senha` | string | obrigatório, 8–72 caracteres |

Resposta típica: **201 Created** com `id`, `nome`, `email`, `criadoEm`.

### 4.2 Login

**`POST /login`**

| Campo | Tipo |
|-------|------|
| `email` | string |
| `senha` | string |

Resposta: `usuarioId`, `email`, `token` (valor de demonstração, não é JWT real).

### 4.3 Jogos

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/jogos` | Lista todos |
| `POST` | `/jogos` | Cria |
| `PUT` | `/jogos/{id}` | Atualiza |
| `DELETE` | `/jogos/{id}` | Remove (resposta **204** sem corpo) |

**Corpo (POST/PUT):**

| Campo | Tipo | Regras |
|-------|------|--------|
| `titulo` | string | obrigatório, até 180 caracteres |
| `plataforma` | string | obrigatório, 2–80 caracteres |
| `precoDiaria` | número | obrigatório, ≥ 0,01 |
| `ativo` | boolean | opcional (padrão ao criar: `true`) |

### 4.4 Clientes

| Método | Caminho |
|--------|---------|
| `GET` | `/clientes` |
| `POST` | `/clientes` |

**Corpo (POST):**

| Campo | Tipo | Regras |
|-------|------|--------|
| `nome` | string | 2–140 caracteres |
| `documento` | string | 5–40 caracteres (ex.: CPF formatado ou número) |

### 4.5 Locações

| Método | Caminho |
|--------|---------|
| `POST` | `/locacoes` |
| `GET` | `/locacoes` |
| `PUT` | `/locacoes/{id}/devolucao` |

**Corpo — criar locação (`POST /locacoes`):**

| Campo | Tipo | Observação |
|-------|------|------------|
| `clienteId` | número | ID existente em `/clientes` |
| `jogoId` | número | ID existente em `/jogos` |
| `dataLocacao` | data (`YYYY-MM-DD`) | deve ser hoje ou futura (`@FutureOrPresent`) |
| `dataPrevistaDevolucao` | data | deve ser **≥** `dataLocacao` |

**Corpo — devolução (`PUT /locacoes/{id}/devolucao`):**

| Campo | Tipo |
|-------|------|
| `dataDevolucao` | data (`YYYY-MM-DD`) |

---

## 5. Exemplos no Windows (PowerShell)

Substitua as datas pelas válidas **no dia em que você testar** (o cadastro exige datas presentes ou futuras conforme validação).

**Base:**

```powershell
$base = "http://localhost:8080"
```

**Cadastrar usuário:**

```powershell
$body = @{
  nome = "Maria Silva"
  email = "maria@exemplo.com"
  senha = "senha12345"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$base/usuarios" -Body $body -ContentType "application/json"
```

**Login:**

```powershell
$body = @{
  email = "maria@exemplo.com"
  senha = "senha12345"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$base/login" -Body $body -ContentType "application/json"
```

**Cliente e jogo:**

```powershell
$c = @{ nome = "João"; documento = "12345678901" } | ConvertTo-Json
$cliente = Invoke-RestMethod -Method Post -Uri "$base/clientes" -Body $c -ContentType "application/json"

$j = @{
  titulo = "The Last of Us Part II"
  plataforma = "PS5"
  precoDiaria = 15.50
} | ConvertTo-Json
$jogo = Invoke-RestMethod -Method Post -Uri "$base/jogos" -Body $j -ContentType "application/json"
```

**Locação (ajuste as datas):**

```powershell
$hoje = (Get-Date).ToString("yyyy-MM-dd")
$amanha = (Get-Date).AddDays(1).ToString("yyyy-MM-dd")

$l = @{
  clienteId = $cliente.id
  jogoId = $jogo.id
  dataLocacao = $hoje
  dataPrevistaDevolucao = $amanha
} | ConvertTo-Json

$loc = Invoke-RestMethod -Method Post -Uri "$base/locacoes" -Body $l -ContentType "application/json"
```

**Devolução:**

```powershell
$dataDev = (Get-Date).ToString("yyyy-MM-dd")
$d = @{ dataDevolucao = $dataDev } | ConvertTo-Json
Invoke-RestMethod -Method Put -Uri "$base/locacoes/$($loc.id)/devolucao" -Body $d -ContentType "application/json"
```

**Listagens:**

```powershell
Invoke-RestMethod -Uri "$base/jogos"
Invoke-RestMethod -Uri "$base/clientes"
Invoke-RestMethod -Uri "$base/locacoes"
```

---

## 6. Erros e validação

Em falhas de validação ou regras de negócio, a API tende a responder com JSON no formato de erro (status **400** ou **404**), com `message`, `path` e, quando aplicável, lista de `fieldErrors` (campo + mensagem). Erros internos genéricos retornam **500** sem expor detalhes técnicos ao cliente.

---

## 7. Console H2 (inspecionar o banco)

1. Com a aplicação rodando, abra: **http://localhost:8080/h2-console**
2. **JDBC URL:** `jdbc:h2:mem:locadora`
3. **User Name:** `sa`
4. **Password:** deixe em branco (conforme `application.yml`)

Você pode executar `SELECT` nas tabelas geradas pelo JPA (por exemplo `usuarios`, `jogos`, `clientes`, `locacoes`).

---

## 8. Estrutura do código (visão geral)

| Pacote / pasta | Função |
|----------------|--------|
| `domain` | Entidades JPA (Usuario, Jogo, Cliente, Locacao) |
| `repository` | Interfaces Spring Data JPA |
| `service` | Regras de negócio e transações |
| `api.controller` | REST controllers e mapeamento HTTP |
| `api.dto` | Objetos de entrada/saída (records) |
| `api.error` | Tratamento global de exceções |

---

## 9. Dicas para o trabalho acadêmico

- **SonarQube (SAST):** apontar a análise para este repositório Maven; o `pom.xml` é o arquivo de projeto.
- **OWASP ZAP (DAST):** com a API em `http://localhost:8080`, configure o alvo para essa URL e rode o spider + scan após criar dados de teste pelos endpoints acima.

Se quiser, no próximo passo dá para acrescentar ao documento só a seção **“como rodar SonarQube/ZAP”** com os comandos que o seu grupo escolher (Docker ou instalador).

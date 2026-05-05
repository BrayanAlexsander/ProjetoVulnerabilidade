# SonarQube SAST Integration Guide

## 📋 Visão Geral

Este projeto agora possui integração com **SonarQube** para análise estática de segurança (SAST - Static Application Security Testing). O SonarQube escaneará o código em busca de:

- 🐛 **Bugs** - Problemas que podem causar erros em tempo de execução
- 🔒 **Vulnerabilidades de Segurança** - Falhas que podem ser exploradas
- 🧹 **Code Smells** - Código de baixa qualidade e manutenibilidade
- 📊 **Duplicação de Código** - Código repetido
- 📈 **Cobertura de Testes** - Métrica de cobertura de testes unitários

## 🚀 Como Executar a Análise

### Pré-requisitos

1. **SonarQube Server** instalado e executando (ou acesso a SonarCloud)
2. **Maven** 3.6.0 ou superior
3. **Token de Autenticação do SonarQube**

### Opção 1: Análise Local (SonarQube Community)

```bash
# 1. Instale o SonarQube localmente (Docker recomendado)
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLED=true \
  -p 9000:9000 sonarqube:latest

# 2. Acesse http://localhost:9000 (admin/admin por padrão)

# 3. Crie um token de autenticação no SonarQube

# 4. Execute a análise
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=locadora-videogames \
  -Dsonar.sources=src/main \
  -Dsonar.tests=src/test \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<SEU_TOKEN>
```

### Opção 2: Análise em Nuvem (SonarCloud)

```bash
# 1. Acesse https://sonarcloud.io e crie uma conta

# 2. Crie um projeto

# 3. Execute a análise
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=locadora-videogames \
  -Dsonar.organization=<sua_organizacao> \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=<SEU_TOKEN>
```

### Opção 3: Executar com Maven Wrapper

```bash
# No Windows
mvnw.cmd clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<TOKEN>

# No Linux/Mac
./mvnw clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<TOKEN>
```

## 🔧 Configuração

### Arquivos de Configuração

- **`pom.xml`** - Contém as propriedades do SonarQube e plugins Maven
- **`sonar-project.properties`** - Arquivo de configuração adicional (opcional)

### Propriedades Principais

```xml
<!-- Identificação do Projeto -->
<sonar.projectKey>locadora-videogames</sonar.projectKey>
<sonar.projectName>Locadora de Videogames</sonar.projectName>

<!-- Diretórios -->
<sonar.sources>src/main</sonar.sources>
<sonar.tests>src/test</sonar.tests>

<!-- Cobertura de Código (JaCoCo) -->
<sonar.coverage.jacoco.xmlReportPaths>target/site/jacoco/jacoco.xml</sonar.coverage.jacoco.xmlReportPaths>

<!-- Exclusões -->
<sonar.exclusions>**/dto/**,**/entity/**,**/model/**</sonar.exclusions>
```

## 📊 Cobertura de Código (JaCoCo)

O plugin **JaCoCo** foi adicionado para gerar relatórios de cobertura de testes:

```bash
# Executar testes com cobertura
mvn clean test

# Visualizar relatório em target/site/jacoco/index.html
```

## 🔐 Segurança

### Variáveis de Ambiente Recomendadas

Em vez de passar o token via linha de comando, use variáveis de ambiente:

```bash
# Linux/Mac
export SONAR_TOKEN=<seu_token>
mvn clean verify sonar:sonar

# Windows (PowerShell)
$env:SONAR_TOKEN="<seu_token>"
mvn clean verify sonar:sonar

# Windows (CMD)
set SONAR_TOKEN=<seu_token>
mvn clean verify sonar:sonar
```

No arquivo `pom.xml`:
```xml
<sonar.login>${env.SONAR_TOKEN}</sonar.login>
```

## 🔍 Interpretando Resultados

### Severidade das Issues

- 🔴 **BLOCKER** - Erro crítico
- 🟠 **CRITICAL** - Problema grave
- 🟡 **MAJOR** - Problema importante
- 🟢 **MINOR** - Problema pequeno
- ⚪ **INFO** - Informação

### Quality Gate

O projeto pode estar configurado com um "Quality Gate" que define critérios de sucesso/falha para a análise.

## 📝 Exclusões Padrão

As seguintes pastas são excluídas da análise por padrão:

- `**/dto/**` - Data Transfer Objects
- `**/entity/**` - Entidades JPA
- `**/model/**` - Modelos de dados

Você pode modificar estas exclusões no `pom.xml` ou `sonar-project.properties`.

## 🚀 CI/CD Integration (GitHub Actions)

Veja `.github/workflows/sonarqube.yml` para a configuração automática de análise no pipeline CI/CD.

## 📚 Referências

- [SonarQube Documentação](https://docs.sonarqube.org)
- [SonarCloud](https://sonarcloud.io)
- [Maven SonarQube Scanner](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/)
- [JaCoCo Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

## ❓ Troubleshooting

### Erro: "Cannot connect to SonarQube"
- Verifique se o SonarQube está rodando: `docker ps` (se usar Docker)
- Valide a URL: `-Dsonar.host.url=http://localhost:9000`

### Erro: "Invalid or expired token"
- Regenere o token no SonarQube
- Use variáveis de ambiente em vez de hardcoding

### Sem dados de cobertura
- Certifique-se que JaCoCo está gerando arquivos em `target/site/jacoco/`
- Verifique se o caminho em `sonar.coverage.jacoco.xmlReportPaths` está correto

---

**Última atualização:** Maio 2026

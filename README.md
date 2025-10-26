# Projeto de Testes Automatizados - Gestão de Energia (ESG)

Este repositório contém o projeto de testes automatizados desenvolvido para a atividade de "Testes Automatizados", focado em um sistema de Gestão de Energia com pilares de ESG.

O projeto cumpre dois requisitos principais:
1.  **Testes BDD (Requisito 1):** Cenários de comportamento do usuário escritos em Gherkin e implementados com Cucumber.
2.  **Testes de API (Requisito 2):** Testes técnicos para todos os endpoints, validando Status Code, Corpo da Resposta e Contrato (JSON Schema).


## Pré-requisitos

Para executar este projeto de testes, os seguintes componentes são necessários:

1.  **A API de Gestão de Energia (Docker):**
    * A aplicação principal (o "Sistema Sob Teste") é executada dentro de um container Docker.
    * O container com a API **DEVE** estar em execução antes de rodar os testes.
    * Os testes estão configurados para atacar a URL base exposta pelo container: `http://localhost:8080`.
    * *(Nota: Os Dockerfiles e scripts de configuração pertencem ao projeto da API, não a este projeto de teste).*

2.  **Ambiente de Teste (Máquina Local):**
    * Java JDK 21 (ou superior, conforme definido no `pom.xml`).
    * Apache Maven 3.8 (ou superior).
    * Docker Desktop (para executar o container da API).


## Como Configurar e Executar os Testes

Siga estes passos para configurar e executar os testes na sua máquina:

1.  **Iniciar a Aplicação (API):**
    * Navegue até o diretório da sua **API de Gestão de Energia**.
    * Execute o comando para subir o container (ex: `docker-compose up -d` ou `docker run ...`).
    * Verifique se a API está respondendo em `http://localhost:8080`.

2.  **Configurar o Projeto de Teste:**
    * Clone ou baixe **este** projeto de teste.
    * Abra o projeto em uma IDE (como IntelliJ ou VS Code).
    * Aguarde o Maven baixar todas as dependências listadas no `pom.xml`.

### Executando os Testes pela IDE

Com a API rodando no Docker, agora você pode executar os testes:

1.  **Para rodar os Testes BDD (Requisito 1):**
    * Navegue até `src/test/java/runner/TestRunner.java`.
    * Clique com o botão direito no arquivo e selecione **"Run 'TestRunner'"**.
    * *Nota: O `TestRunner` está configurado para rodar apenas a tag `@regressivo`.*

2.  **Para rodar os Testes de API (Requisito 2):**
    * Navegue até `src/test/java/tests/ApiValidationTests.java`.
    * Clique com o botão direito no arquivo e selecione **"Run 'ApiValidationTests'"**.

### Executando os Testes pelo Terminal

Você também pode rodar todos os testes (BDD e API) de uma vez usando o Maven.

1.  Abra um terminal na pasta raiz **deste projeto de teste** (onde está o `pom.xml`).
2.  Execute o comando:
    ```bash
    mvn clean test
    ```


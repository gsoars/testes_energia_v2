#language: pt
Funcionalidade: Gerenciamento e Monitoramento de Sistema de Energia
  Como usuario do sistema, eu quero interagir com os endpoints para
  garantir que o login, registro de medicoes, ocorrencias e
  consulta de dados estejam funcionando corretamente.

  @regressivo
  Cenário: Autenticacao com sucesso (Caminho Feliz)
    Quando eu faco login com o email "usuario1@email.com" e senha "123456"
    Entao o status da resposta deve ser 200
    E a resposta deve conter um "token" de autenticacao

  @regressivo
  Cenário: Tentativa de autenticacao com senha invalida (Caminho Negativo)
    Quando eu faco login com o email "usuario1@email.com" e senha "senhaErrada"
    Entao o status da resposta deve ser 500

  @regressivo
  Cenário: Administrador registra nova medicao com sucesso (Governanca e Eficiencia)
    Dado que eu estou autenticado como "ADMIN"
    Quando eu envio uma nova medicao para a unidade "5" com os seguintes dados:
      | idUnidade        | 5           |
      | dataLeitura      | 2025-10-24  |
      | leituraAnterior  | 1700        |
      | leituraAtual     | 1800        |
      | observacao       | "teste BDD" |
    Entao o status da resposta deve ser 201
    E a resposta deve conter o "consumoKwh" calculado como 100

  @regressivo
  Cenário: Usuario comum tenta registrar uma ocorrencia tecnica e e bloqueado (Seguranca)
    Dado que eu estou autenticado como "USER"
    Quando eu tento registrar uma ocorrencia tecnica para a unidade "6"
    Entao o status da resposta deve ser 403

  @regressivo
  Cenário: Consulta de historico de consumo para unidade valida (Monitoramento)
    Dado que eu estou autenticado
    Quando eu consulto o historico de consumo da unidade "5"
    Entao o status da resposta deve ser 200
    E a resposta deve ser uma lista de consumos
    E cada item da lista deve conter o "idUnidade" como 5
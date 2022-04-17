# DESAFIO TÉCNICO - SICREDI

## Ferramentas utilizadas
- Java 11
- Spring
    - Boot
    - WebFlux
    - Data MongoDb
    - AMQP
    - OpenFeign
- MongoDB
- RabbitMQ
- Jacoco
- Docker
- Docker-compose
- Lombok
- SpringDoc/Swagger

## Breve explicação da escolha das ferramentas
Além das ferramentas pedidas no desafio, foi utilizado alguns outros recursos para facilitar o desenvolvimento do projeto, como:
- Docker-compose: Um orquestrador de containers que permite criar toda a infraestrutura de serviços do projeto.
- Lombok: Para a diminuição de código e foco no que é importante.
- OpenFeing: Um cliente para facilitar o consumo das APIs.
- Jacoco: Um gerador de relatório de cobertura de testes que facilita a visualização dos mesmos.

## Sobre a API
---
Entende-se que em uma votação, o passo inicial é criar a pauta, seguido do inicio da sessão e depois vem os votos dos associados. Foi seguido essa mesma lógica para a implementação do projeto, que consiste em:
- Criar a pauta
- Criar e iniciar a sessão
- Realizar a votação
- Publicar o resultado da votação
  Ao iniciar a sessão, uma agenda é criada para a data e hora atual mais a quantidade de minutos informados para a sessão. Após o encerramento da sessão, a nenhum associado é permitido votar.

## Instalação
---
Para iniciar o projeto é necessário executar alguns comandos:
### Criação dos containers
    docker-compose up -d --build
### Visualizar logs da aplicação
    docker-compose logs -f app
## Tipos de dados
| Coleção | Campo | Tipo | Descrição |
|---|---|---|---|
|PautaDocument|Id|String|Chave única da pauta|
|PautaDocument|Nome|String|Nome da pauta|
|SessaoDocument|Id|String|Chave única da sessão|
|SessaoDocument|Pauta|PautaDocument|Pauta associada a sessão|
|SessaoDocument|Time|LocalDateTime|Tempo em minutos que a sessão vai ficar ativa. Padrão: 1|
|AssociadoDocument|Id|String|Chave única do usuário|
|AssociadoDocument|Cpf|String|Documento de identificação do usuário|
|VotoDocument|sessao|SessaoDocument|Sessão ao qual o voto está vinculado|
|VotoDocument|associado|AssociadoDocument|Associado ao qual o voto está vinculado|
|VotoDocument|voto|String|Opção de voto do associado. Tipos permitidos: SIM, NAO|
## Url da applicação
- Local

  [http://localhost/](http://localhost/)
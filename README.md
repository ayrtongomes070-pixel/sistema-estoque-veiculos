# Sistema de Estoque de Veículos

Projeto Integrador da disciplina de Programação Orientada a Objetos (UNICAP).

## Descrição

Sistema de gerenciamento de estoque de veículos para lojas de automóveis. Nesta primeira etapa, o foco é o sistema "bruto" (sem o módulo de contrato/assinatura de lojas).

## Tecnologias

- Java 17
- JavaFX (interface gráfica)
- Repositório em memória nesta primeira versão (sem configuração de banco)
- JDBC + MySQL planejado para a próxima etapa
- Maven (gerenciador de dependências)

## Arquitetura

MVC (Model, View, Controller), organizado em camadas:

- `model` — entidades (Veiculo, Carro, Caminhonete, Cliente, Vendedor, Venda, Estoque)
- `repository` — acesso a dados (interface `IVeiculoRepository` + implementação JDBC)
- `service` — regras de negócio
- `controller` — liga a interface JavaFX aos Services
- `exception` — exceções customizadas (ex: `VeiculoNaoEncontradoException`)
- `util` — utilitários (ex: conexão com banco de dados)

## Conceitos de POO aplicados

Classes e objetos, encapsulamento, herança, polimorfismo, abstração, interfaces, classes abstratas, coleções, tratamento de exceções e persistência de dados.

## Funcionalidades atuais

- Listagem e pesquisa por marca, modelo ou chassi
- Cadastro de carros e caminhonetes
- Edição e remoção de veículos
- Resumo com quantidade e valor total do estoque
- Registros de demonstração carregados ao iniciar

Os dados são mantidos em memória durante a execução. Ao fechar e abrir o programa, os registros voltam aos exemplos iniciais. A integração com MySQL fica para uma próxima etapa.

## Como rodar

1. Instale o Java 17 e o Maven.
2. Na pasta do projeto, rode `mvn clean install` para instalar as dependências.
3. Rode `mvn javafx:run` para iniciar a aplicação.

## Autores

- Ayrton Gomes 
- Nelson Spinelli


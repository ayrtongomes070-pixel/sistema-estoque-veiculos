# Sistema de Estoque de Veículos

Projeto Integrador da disciplina de Programação Orientada a Objetos (UNICAP).

## Descrição

Sistema de gerenciamento de estoque de veículos para lojas de automóveis. Nesta primeira etapa, o foco é o sistema "bruto" (sem o módulo de contrato/assinatura de lojas).

## Tecnologias

- Java 17
- JavaFX (interface gráfica)
- JDBC + MySQL (persistência)
- Maven (gerenciador de dependências)

## Arquitetura

MVC (Model, View, Controller), organizado em camadas:

- `model` — entidades (Veiculo, Carro, Caminhonete, Cliente, Vendedor, Venda, Estoque)
- `repository` — acesso a dados (interface `IVeiculoRepository` + implementação JDBC)
- `service` — regras de negócio
- `controller` — liga a View (FXML) aos Services
- `exception` — exceções customizadas (ex: `VeiculoNaoEncontradoException`)
- `util` — utilitários (ex: conexão com banco de dados)

## Conceitos de POO aplicados

Classes e objetos, encapsulamento, herança, polimorfismo, abstração, interfaces, classes abstratas, coleções, tratamento de exceções e persistência de dados.

## Como rodar

1. Configure o banco de dados MySQL e copie `src/main/resources/db.properties.example` para `db.properties`, preenchendo suas credenciais.
2. Rode `mvn clean install` para instalar as dependências.
3. Rode `mvn javafx:run` para iniciar a aplicação.

## Autores

- Ayrton

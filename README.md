# Sistema de Estoque de Veículos

Projeto Integrador da disciplina de Programação Orientada a Objetos (UNICAP).

## Descrição

Sistema de gerenciamento de estoque de veículos para lojas de automóveis. Nesta primeira etapa, o foco é o sistema "bruto" (sem o módulo de contrato/assinatura de lojas).

## Tecnologias

- Java 17
- JavaFX (interface gráfica)
- Arquivo local `dados/veiculos.properties` para manter o estoque entre execuções
- Repositório em memória disponível para testes
- JDBC + MySQL permanece como implementação planejada; ainda não está conectado à aplicação
- Maven (gerenciador de dependências)

## Arquitetura

MVC (Model, View, Controller), organizado em camadas:

- `model` — entidades (Veiculo, Carro, Caminhonete, Cliente, Vendedor, Venda, Estoque)
- `repository` — acesso a dados (interface `IVeiculoRepository` + implementações em arquivo, memória e JDBC ainda não concluída)
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
- Registros de demonstração adicionados na primeira execução
- Estoque salvo automaticamente em arquivo e carregado nas próximas execuções

O arquivo `dados/veiculos.properties` é criado na pasta de execução do projeto. Ele é local e está ignorado pelo Git para não enviar os dados de demonstração ou da máquina ao repositório.

## Requisitos funcionais

- **RF01:** Cadastrar veículos dos tipos carro e caminhonete, informando chassi, marca, modelo, ano, preço e os dados específicos de cada tipo.
- **RF02:** Listar os veículos cadastrados no estoque.
- **RF03:** Pesquisar veículos por marca, modelo ou chassi.
- **RF04:** Editar os dados de um veículo cadastrado.
- **RF05:** Remover um veículo do estoque após confirmação do usuário.
- **RF06:** Exibir a quantidade de veículos e o valor total do estoque.
- **RF07:** Salvar alterações no arquivo local e recuperar os veículos ao iniciar o sistema novamente.
- **RF08:** Validar os dados informados e avisar o usuário quando houver campos inválidos ou uma operação não puder ser concluída.

## Requisitos não funcionais

- **RNF01:** O sistema deve ser implementado em Java e oferecer interface gráfica desktop com JavaFX.
- **RNF02:** O projeto deve ser compilável e executável com JDK 17 ou superior e Maven 3.9 ou superior.
- **RNF03:** Os dados do estoque devem ser armazenados localmente em `dados/veiculos.properties`, sem exigir configuração de banco de dados nesta versão.
- **RNF04:** O código deve manter separação de responsabilidades entre modelos, controllers, serviços e repositórios.
- **RNF05:** As entidades devem aplicar encapsulamento e validar atributos para preservar a consistência dos dados.
- **RNF06:** As operações principais devem ter testes unitários com JUnit 5.
- **RNF07:** Falhas de leitura e gravação do arquivo devem gerar mensagens compreensíveis, sem descartar silenciosamente os dados.
- **RNF08:** O arquivo local de dados deve permanecer fora do controle de versão para não misturar dados de execução com os arquivos do projeto.

## Como rodar

1. Instale um JDK 17 ou superior e o Maven 3.9 ou superior.
2. Na pasta do projeto, rode `mvn clean test` para compilar e executar os testes unitários.
3. Rode `mvn javafx:run` para iniciar a aplicação.

## Testes

Os testes JUnit cobrem o repositório em memória e em arquivo, as operações do estoque, o registro de vendas e os casos de erro. Execute-os com `mvn test`.

## Autores

- Ayrton Gomes 
- Nelson Spinelli


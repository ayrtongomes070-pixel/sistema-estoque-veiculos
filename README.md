# Sistema de Estoque de Veículos

Projeto Integrador da disciplina de Programação Orientada a Objetos (UNICAP).

## Descrição

Sistema desktop de gerenciamento de estoque de veículos para lojas de automóveis. Permite cadastrar carros e caminhonetes, pesquisar, editar, remover e acompanhar o resumo do estoque (quantidade, valor total e marcas). Os dados ficam salvos em um arquivo local e são recuperados quando o sistema é aberto novamente. Esta versão não inclui o módulo de contrato/assinatura de lojas.

## Tecnologias

- Java 17
- JavaFX 21 (interface gráfica)
- Maven (build e dependências)
- JUnit 5 (testes unitários)
- Arquivo local `dados/veiculos.properties` para manter o estoque entre execuções
- JDBC + MySQL: implementação planejada, ainda não conectada à aplicação

## Como executar

**Pré-requisitos:** JDK 17 ou superior e Maven 3.9 ou superior instalados (`java -version` e `mvn -v` devem funcionar no terminal).

1. Clone o repositório:
   ```bash
   git clone https://github.com/ayrtongomes070-pixel/sistema-estoque-veiculos.git
   cd sistema-estoque-veiculos
   ```
2. Compile e rode os testes:
   ```bash
   mvn clean test
   ```
3. Inicie a aplicação:
   ```bash
   mvn javafx:run
   ```

Na primeira execução são cadastrados três veículos de demonstração. O arquivo `dados/veiculos.properties` é criado na pasta do projeto; para recomeçar do zero, basta apagá-lo. Ele está no `.gitignore`, para não misturar dados de execução com o código.

## Arquitetura

MVC organizado em camadas:

| Pacote | Responsabilidade |
|---|---|
| `model` | Entidades: `Veiculo` (abstrata), `Carro`, `Caminhonete`, `Cliente`, `Vendedor`, `Venda` |
| `repository` | Acesso a dados: interface `IVeiculoRepository` e implementações em memória, em arquivo e JDBC (planejada) |
| `service` | Regras de negócio: `EstoqueService`, `VendaService`, `SessaoUsuario` |
| `controller` | Liga a interface aos services: `EstoqueController`, `VendaController` |
| `exception` | Exceções específicas do sistema |
| `util` | Utilitários: `ConversorNumero` (leitura dos campos numéricos) e `ConexaoBD` |
| `App` | Interface gráfica JavaFX |

## Funcionalidades

- Listagem e pesquisa por marca, modelo ou chassi
- Cadastro de carros e caminhonetes
- Edição e remoção (com confirmação) de veículos
- Resumo com quantidade de veículos, valor total do estoque e número de marcas
- Estoque salvo automaticamente em arquivo e carregado nas próximas execuções
- Registro de vendas com desconto por tipo de veículo (camada de serviço)

## Requisitos funcionais

- **RF01:** Cadastrar veículos dos tipos carro e caminhonete, informando chassi, marca, modelo, ano, preço e os dados específicos de cada tipo.
- **RF02:** Listar os veículos cadastrados no estoque.
- **RF03:** Pesquisar veículos por marca, modelo ou chassi.
- **RF04:** Editar os dados de um veículo cadastrado.
- **RF05:** Remover um veículo do estoque após confirmação do usuário.
- **RF06:** Exibir a quantidade de veículos, o valor total do estoque e a quantidade de marcas.
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
- **RNF08:** O arquivo local de dados deve permanecer fora do controle de versão.

## Recursos desta etapa

### Tratamento de exceções

| Exceção | Quando ocorre |
|---|---|
| `VeiculoNaoEncontradoException` (checada) | Busca, edição, remoção ou venda de um chassi que não existe |
| `VeiculoDuplicadoException` | Cadastro de um chassi que já está no estoque |
| `DadosInvalidosException` | Dado inválido: campo vazio, preço negativo, ano fora do intervalo, texto em campo numérico, período invertido |
| `ErroPersistenciaException` | Falha ao ler ou gravar o arquivo, ou arquivo corrompido |

As mensagens indicam o campo ou o chassi envolvido (ex.: *"Ano deve ser um número inteiro. Valor informado: "abc"."*) e são exibidas em janelas de aviso na interface. Se a gravação no arquivo falhar, a alteração é desfeita em memória, para que a tela e o arquivo nunca fiquem diferentes.

### Coleções

- **Map** (`LinkedHashMap<String, Veiculo>`): o repositório indexa os veículos pelo chassi. A busca é direta, a duplicidade fica impedida e a ordem de cadastro é preservada.
- **List**: listagem e resultado da pesquisa (`EstoqueService.pesquisar`), histórico de vendas (`VendaService`).
- **Set** (`TreeSet<String>`): `EstoqueService.listarMarcas()` devolve as marcas sem repetição e em ordem alfabética (usado no card "Marcas").
- **Map** (`TreeMap<String, Long>`): `EstoqueService.quantidadePorTipo()` conta os veículos por tipo.

### Leitura e escrita de arquivos

`VeiculoRepositoryArquivo` usa `java.nio.file` e `Properties` (UTF-8). Ele grava primeiro em um arquivo temporário e depois o move por cima do original, para que o arquivo nunca fique gravado pela metade. A leitura valida cada registro e informa exatamente o que está corrompido.

### Testes unitários

37 testes JUnit 5, organizados no mesmo pacote da classe testada e documentados com `@DisplayName`:

| Classe de teste | O que cobre |
|---|---|
| `model/VeiculoTest` | Validações, desconto por tipo (polimorfismo), igualdade por chassi |
| `repository/VeiculoRepositoryMemoriaTest` | CRUD, chassi duplicado, chassi inexistente, veículo nulo |
| `repository/VeiculoRepositoryArquivoTest` | Gravação e leitura, pasta criada automaticamente, arquivo corrompido, falha de gravação desfeita |
| `service/EstoqueServiceTest` | Valor total, pesquisa, marcas (Set), contagem por tipo (Map), exceções |
| `service/VendaServiceTest` | Venda com desconto, veículo inexistente, venda sem cliente, período inválido |
| `util/ConversorNumeroTest` | Formatos de número, mensagens de erro e o caso do preço na edição |

Execute com `mvn test`.

### Organização e código sem duplicação

- `VeiculoRepositoryArquivo` herda as regras de `VeiculoRepositoryMemoria` e só acrescenta a gravação em disco.
- Tipo e detalhe de cada veículo vêm dos métodos polimórficos `getTipo()` e `getDescricaoDetalhe()`, em vez de vários `instanceof` espalhados.
- A pesquisa ficou na camada de serviço (`EstoqueService.pesquisar`), e não mais na interface.
- A classe `Estoque`, que não era usada e repetia o papel do repositório, foi removida.
- `VeiculoRepositoryJDBC`, ainda não implementado, lança `UnsupportedOperationException` em vez de ignorar os dados silenciosamente.

## Autores

- Ayrton Gomes
- Nelson Spinelli

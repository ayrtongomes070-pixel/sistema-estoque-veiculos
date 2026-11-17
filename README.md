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
| `factory` | Criação de veículos (Factory Method): `VeiculoFactory`, `CarroFactory`, `CaminhoneteFactory` |
| `exception` | Exceções específicas do sistema |
| `util` | Utilitários: `ConversorNumero` (leitura dos campos numéricos) e `ConexaoBD` |
| `App` | Interface gráfica JavaFX |

## Padrões de Projeto GoF aplicados

Foram aplicados três padrões GoF, cada um escolhido para resolver um problema concreto que já existia no código, e não apenas para cumprir o requisito.

| Padrão | Categoria | Onde está | Problema resolvido |
|---|---|---|---|
| **Strategy** | Comportamental | `repository/IVeiculoRepository` + implementações | Trocar a forma de armazenar veículos sem mexer nas regras de negócio |
| **Observer** | Comportamental | `service/EstoqueObserver`, `service/EstoqueService`, `App` | Manter a tela sincronizada com o estoque sem chamadas de atualização espalhadas |
| **Factory Method** | Criacional | `factory/VeiculoFactory`, `CarroFactory`, `CaminhoneteFactory` | Criar o tipo certo de veículo a partir de texto (formulário e arquivo) sem `switch`/`instanceof` duplicados |

### 1. Strategy — estratégia de persistência

**Problema.** O sistema precisa guardar veículos de formas diferentes: em memória (testes), em arquivo local (versão atual) e, no futuro, em MySQL. Se `EstoqueService` e `VendaService` criassem diretamente um repositório específico, cada troca de armazenamento obrigaria a alterar as regras de negócio.

**Aplicação.**
- *Strategy (contrato):* `IVeiculoRepository` define `salvar`, `buscarPorChassi`, `listarTodos`, `atualizar` e `remover`.
- *Estratégias concretas:* `VeiculoRepositoryMemoria`, `VeiculoRepositoryArquivo` (herda da memória e acrescenta a gravação em disco) e `VeiculoRepositoryJDBC` (planejada).
- *Contexto:* `EstoqueService` e `VendaService` recebem a interface no construtor. A `App` escolhe `VeiculoRepositoryArquivo`; os testes usam `VeiculoRepositoryMemoria` ou o arquivo em pasta temporária.

**Por que é adequado.** O algoritmo de armazenamento varia, mas o uso é o mesmo. Com o Strategy a troca é feita em uma única linha (`new EstoqueService(repositorio)`), os testes rodam sem disco nem banco, e a implementação JDBC poderá ser plugada sem alterar os serviços (princípio aberto/fechado).

### 2. Observer — atualização da tela quando o estoque muda

**Problema.** Depois de cadastrar, editar ou remover um veículo, a tabela e os três cards de resumo (quantidade, valor total e marcas) precisam ser recalculados. Sem o padrão, cada ação da interface teria que lembrar de chamar a atualização, e qualquer nova operação que alterasse o estoque poderia deixar a tela desatualizada.

**Aplicação.**
- *Observer:* interface funcional `EstoqueObserver` com o método `aoAlterarEstoque()`.
- *Subject:* `EstoqueService` mantém a lista de observadores (`adicionarObservador` / `removerObservador`) e chama `notificarAlteracao()` **somente após** a operação ter sido concluída com sucesso, ou seja, depois de gravada no arquivo.
- *Observador concreto:* a `App` se registra com `controller.adicionarObservador(this::atualizarTabela)`.

**Por que é adequado.** O serviço não conhece JavaFX, apenas avisa que algo mudou; a camada de negócio continua independente da interface. Como a notificação não acontece quando a operação falha, a tela nunca mostra um estado que não foi salvo. Outros observadores (um log, um alerta de estoque baixo) podem ser adicionados sem modificar o serviço. Coberto pelo teste *"notifica observadores após alterações bem-sucedidas"* em `EstoqueServiceTest`.

### 3. Factory Method — criação de veículos

**Problema.** Veículos são criados a partir de texto em dois lugares: no formulário da `App` (campos digitados) e na leitura do `dados/veiculos.properties`. Antes da refatoração, cada lugar tinha seu próprio `if`/`switch` para decidir entre `Carro` e `Caminhonete`, sua própria conversão do campo específico (portas ou capacidade de carga) e cadeias de `instanceof` para fazer o caminho inverso (preencher o formulário de edição e gravar o arquivo). Incluir um novo tipo, como `Moto`, exigiria alterar todos esses pontos.

**Aplicação.**
- *Creator:* `VeiculoFactory` (abstrata). O método público `criar(...)` converte os dados comuns (ano e preço, com `ConversorNumero`) e chama o **método-fábrica** `instanciar(...)`, que é abstrato.
- *Concrete Creators:* `CarroFactory` e `CaminhoneteFactory` implementam `instanciar(...)`, convertem o detalhe do seu tipo e informam o rótulo do campo (`getRotuloDetalhe()`) e o detalhe em texto (`detalheComoTexto(...)`).
- *Products:* `Carro` e `Caminhonete` (subclasses de `Veiculo`).
- *Clientes:* `App` (tipos do combo, rótulo do campo, valor na edição e criação do veículo) e `VeiculoRepositoryArquivo` (leitura e gravação), que usam apenas `VeiculoFactory.paraTipo(...)` e `VeiculoFactory.paraVeiculo(...)`.

```java
// Antes, na App e, de forma parecida, no repositório em arquivo
return tipo.getValue().equals("Carro")
        ? new Carro(chassi, modelo, marca, ano, preco, ConversorNumero.paraInteiro(detalhe, "Número de portas"))
        : new Caminhonete(chassi, modelo, marca, ano, preco, ConversorNumero.paraDecimal(detalhe, "Capacidade de carga"));

// Depois
return VeiculoFactory.paraTipo(tipo.getValue()).criar(chassi, modelo, marca, ano, preco, detalhe);
```

**Por que é adequado.** A decisão de qual classe instanciar fica em um único lugar, e a interface gráfica e o repositório deixaram de conhecer as classes concretas: não há mais `instanceof` de `Carro`/`Caminhonete` nesses arquivos. Para criar um novo tipo basta escrever uma nova subclasse de `Veiculo`, uma nova fábrica e registrá-la em `VeiculoFactory`. O combo da tela, o rótulo do campo e o arquivo passam a aceitá-lo automaticamente. As mensagens de validação ficam iguais no formulário e na leitura do arquivo. Coberto por `factory/VeiculoFactoryTest`.

### Documentação (Javadoc)

Todas as classes e todos os métodos e construtores públicos e protegidos possuem Javadoc com descrição, `@param`, `@return` e `@throws` quando aplicável. A documentação foi verificada com o *doclint* do JDK (`javadoc -Xdoclint:all`), sem avisos.

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

42 testes JUnit 5, organizados no mesmo pacote da classe testada e documentados com `@DisplayName`:

| Classe de teste | O que cobre |
|---|---|
| `model/VeiculoTest` | Validações, desconto por tipo (polimorfismo), igualdade por chassi |
| `repository/VeiculoRepositoryMemoriaTest` | CRUD, chassi duplicado, chassi inexistente, veículo nulo |
| `repository/VeiculoRepositoryArquivoTest` | Gravação e leitura, pasta criada automaticamente, arquivo corrompido, falha de gravação desfeita |
| `service/EstoqueServiceTest` | Valor total, pesquisa, marcas (Set), contagem por tipo (Map), exceções |
| `service/VendaServiceTest` | Venda com desconto, veículo inexistente, venda sem cliente, período inválido |
| `factory/VeiculoFactoryTest` | Escolha da fábrica pelo tipo, criação a partir de texto, conversão inversa, tipo desconhecido |
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

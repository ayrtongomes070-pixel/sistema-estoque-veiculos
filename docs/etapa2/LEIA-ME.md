# Etapa 2 — Modelagem de Classes

O arquivo `Diagrama_Classes_UML.puml` contém o **diagrama de classes UML** do StockMaster. Ele representa classes e interfaces implementadas no projeto, com atributos, operações, herança, realizações de interface e associações.

## Como visualizar ou exportar

Abra o arquivo `.puml` no Visual Studio Code com uma extensão PlantUML instalada. Use a opção de pré-visualização para conferir o diagrama e exporte como PNG ou SVG para anexar na atividade, caso a plataforma solicite uma imagem.

## Decisões representadas

- `Veiculo` é abstrata e concentra os dados compartilhados. `Carro` e `Caminhonete` especializam o modelo e implementam suas próprias regras de desconto e detalhes.
- `EstoqueController` e `VendaController` encaminham operações para serviços, que concentram regras da aplicação.
- `IVeiculoRepository` separa os serviços da forma de armazenamento. Há implementações em memória, em arquivo e via JDBC.
- `Venda` associa um veículo, um cliente e um vendedor, registrando data e valor final.
- `VeiculoFactory` e suas fábricas concretas centralizam a criação de carros e caminhonetes.

O diagrama é de **classes** e não de casos de uso: ele descreve a estrutura estática do software, não os atores e objetivos do usuário. As relações refletem o código atual; a persistência JDBC está disponível como implementação, enquanto a aplicação pode usar outra implementação do repositório.

package com.estoqueveiculos;

import com.estoqueveiculos.controller.EstoqueController;
import com.estoqueveiculos.exception.ErroPersistenciaException;
import com.estoqueveiculos.exception.VeiculoDuplicadoException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.repository.VeiculoRepositoryArquivo;
import com.estoqueveiculos.service.EstoqueService;
import com.estoqueveiculos.util.ConversorNumero;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Optional;

/** Aplicação desktop JavaFX para gerenciamento básico de veículos. */
public class App extends Application {
    private EstoqueController controller;
    private final TableView<Veiculo> tabela = new TableView<>();
    private final Label quantidade = new Label("0");
    private final Label valor = new Label("R$ 0,00");
    private final Label marcas = new Label("0");
    private final TextField busca = new TextField();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    @Override public void start(Stage stage) {
        try {
            Path arquivoEstoque = Path.of("dados", "veiculos.properties");
            boolean primeiraExecucao = Files.notExists(arquivoEstoque);
            var repositorio = new VeiculoRepositoryArquivo(arquivoEstoque);
            controller = new EstoqueController(new EstoqueService(repositorio));
            if (primeiraExecucao) carregarExemplos();
        } catch (ErroPersistenciaException ex) {
            mostrarErro("Não foi possível abrir o estoque", ex.getMessage());
            return;
        }
        BorderPane root = new BorderPane();
        root.setTop(cabecalho());
        root.setCenter(conteudo());
        root.setStyle("-fx-background-color: #f3f6fa; -fx-font-family: 'Arial';");
        atualizarTabela();
        Scene scene = new Scene(root, 1120, 720);
        stage.setTitle("AutoStock | Estoque de veículos");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    private VBox cabecalho() {
        Label marca = new Label("AUTOSTOCK"); marca.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label legenda = new Label("GESTÃO DE VEÍCULOS"); legenda.setStyle("-fx-text-fill: #9aabc2; -fx-font-size: 10px; -fx-font-weight: bold;");
        VBox logo = new VBox(3, marca, legenda);
        Label conta = new Label("●  Estoque local"); conta.setStyle("-fx-text-fill: #dce6f3; -fx-font-size: 12px;");
        HBox barra = new HBox(logo, new Region(), conta); HBox.setHgrow(barra.getChildren().get(1), Priority.ALWAYS);
        barra.setAlignment(Pos.CENTER_LEFT); barra.setPadding(new Insets(18, 30, 18, 30));
        barra.setStyle("-fx-background-color: #14243a;");
        return new VBox(barra);
    }

    private VBox conteudo() {
        Label titulo = new Label("Visão geral"); titulo.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #172b46;");
        Label subtitulo = new Label("Acompanhe e gerencie os veículos disponíveis no pátio."); subtitulo.setStyle("-fx-text-fill: #708098; -fx-font-size: 13px;");
        VBox intro = new VBox(6, titulo, subtitulo);
        HBox cards = new HBox(14, card("VEÍCULOS CADASTRADOS", quantidade, "Unidades no estoque"), card("VALOR EM ESTOQUE", valor, "Soma dos preços cadastrados"), card("MARCAS", marcas, "Marcas diferentes no estoque"));
        busca.setPromptText("Buscar por marca, modelo ou chassi..."); busca.setPrefWidth(340);
        busca.textProperty().addListener((o, anterior, texto) -> atualizarTabela());
        Button novo = new Button("＋  Cadastrar veículo"); novo.setDefaultButton(true); novo.setStyle(botaoPrimario());
        novo.setOnAction(e -> abrirFormulario(null));
        HBox ferramentas = new HBox(12, busca, new Region(), novo); HBox.setHgrow(ferramentas.getChildren().get(1), Priority.ALWAYS); ferramentas.setAlignment(Pos.CENTER_LEFT);
        configurarTabela();
        Button editar = new Button("Editar selecionado"); editar.setOnAction(e -> { Veiculo v = tabela.getSelectionModel().getSelectedItem(); if (v == null) aviso("Selecione um veículo na tabela."); else abrirFormulario(v); });
        Button excluir = new Button("Remover"); excluir.setStyle("-fx-text-fill: #b42318; -fx-background-color: white; -fx-border-color: #e5e9f0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 9 15;");
        excluir.setOnAction(e -> removerSelecionado());
        HBox acoes = new HBox(9, editar, excluir); acoes.setAlignment(Pos.CENTER_RIGHT);
        VBox painel = new VBox(12, ferramentas, tabela, acoes); painel.setPadding(new Insets(18)); painel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5eaf1; -fx-border-radius: 10;");
        VBox corpo = new VBox(22, intro, cards, painel); corpo.setPadding(new Insets(30));
        return corpo;
    }

    private VBox card(String nome, Label dado, String detalhe) {
        Label rotulo = new Label(nome); rotulo.setStyle("-fx-text-fill: #718096; -fx-font-size: 10px; -fx-font-weight: bold;");
        dado.setStyle("-fx-text-fill: #172b46; -fx-font-size: 24px; -fx-font-weight: bold;");
        Label desc = new Label(detalhe); desc.setStyle("-fx-text-fill: #8a98aa; -fx-font-size: 11px;");
        VBox box = new VBox(8, rotulo, dado, desc); box.setPadding(new Insets(18)); box.setPrefWidth(270); box.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-color: #e5eaf1; -fx-border-radius: 9;"); return box;
    }

    private void configurarTabela() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPlaceholder(new Label("Nenhum veículo encontrado. Cadastre o primeiro veículo."));
        tabela.setPrefHeight(370);
        tabela.getColumns().setAll(coluna("TIPO", Veiculo::getTipo, 105),
                coluna("MARCA / MODELO", v -> v.getMarca() + " " + v.getModelo(), 230),
                coluna("ANO", v -> String.valueOf(v.getAnoFabricacao()), 75),
                coluna("CHASSI", Veiculo::getChassi, 190),
                coluna("DETALHES", Veiculo::getDescricaoDetalhe, 140),
                coluna("PREÇO", v -> moeda.format(v.getPreco()), 130));
    }

    private TableColumn<Veiculo, String> coluna(String titulo, java.util.function.Function<Veiculo, String> valor, double largura) {
        TableColumn<Veiculo, String> c = new TableColumn<>(titulo); c.setCellValueFactory(d -> new ReadOnlyStringWrapper(valor.apply(d.getValue()))); c.setPrefWidth(largura); return c;
    }

    /** Recarrega a tabela respeitando o texto da busca e atualiza os cards de resumo. */
    private void atualizarTabela() {
        tabela.setItems(FXCollections.observableArrayList(controller.pesquisar(busca.getText())));
        quantidade.setText(String.valueOf(controller.listarVeiculos().size()));
        valor.setText(moeda.format(controller.valorTotalEstoque()));
        marcas.setText(String.valueOf(controller.listarMarcas().size()));
    }

    private void abrirFormulario(Veiculo atual) {
        Dialog<Veiculo> dialog = new Dialog<>(); dialog.setTitle(atual == null ? "Cadastrar veículo" : "Editar veículo"); dialog.setHeaderText("Preencha as informações do veículo");
        ButtonType salvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE); dialog.getDialogPane().getButtonTypes().addAll(salvar, ButtonType.CANCEL);
        ComboBox<String> tipo = new ComboBox<>(FXCollections.observableArrayList("Carro", "Caminhonete")); tipo.setValue(atual instanceof Caminhonete ? "Caminhonete" : "Carro");
        TextField chassi = campo(atual == null ? null : atual.getChassi(), "Ex.: 9BWZZZ377VT004251"); chassi.setDisable(atual != null);
        TextField marca = campo(atual == null ? null : atual.getMarca(), "Ex.: Volkswagen");
        TextField modelo = campo(atual == null ? null : atual.getModelo(), "Ex.: Polo");
        TextField ano = campo(atual == null ? null : String.valueOf(atual.getAnoFabricacao()), "Ex.: 2024");
        // Preço e carga são exibidos no formato brasileiro ("89900,00"), o mesmo que o ConversorNumero lê de volta
        TextField preco = campo(atual == null ? null : ConversorNumero.paraTexto(atual.getPreco()), "Ex.: 85000,00");
        TextField detalhe = campo(atual instanceof Carro c ? String.valueOf(c.getNumeroPortas()) : atual instanceof Caminhonete c ? ConversorNumero.paraTexto(c.getCapacidadeCargaKg()) : null, "Ex.: 4");
        Label detalheLabel = new Label("Número de portas");
        Runnable ajustar = () -> detalheLabel.setText(tipo.getValue().equals("Carro") ? "Número de portas" : "Capacidade (kg)"); tipo.valueProperty().addListener((o,a,b) -> ajustar.run());
        GridPane grid = new GridPane(); grid.setHgap(12); grid.setVgap(10); grid.addRow(0, new Label("Tipo"), tipo); grid.addRow(1, new Label("Chassi"), chassi); grid.addRow(2, new Label("Marca"), marca); grid.addRow(3, new Label("Modelo"), modelo); grid.addRow(4, new Label("Ano"), ano); grid.addRow(5, new Label("Preço (R$)"), preco); grid.addRow(6, detalheLabel, detalhe);
        grid.getColumnConstraints().addAll(new ColumnConstraints(125), new ColumnConstraints(260)); dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().lookupButton(salvar).disableProperty().bind(chassi.textProperty().isEmpty().or(marca.textProperty().isEmpty()).or(modelo.textProperty().isEmpty()).or(ano.textProperty().isEmpty()).or(preco.textProperty().isEmpty()).or(detalhe.textProperty().isEmpty()));
        dialog.setResultConverter(bt -> {
            if (bt != salvar) return null;
            try {
                String placaChassi = chassi.getText().trim();
                int anoNum = ConversorNumero.paraInteiro(ano.getText(), "Ano");
                double precoNum = ConversorNumero.paraDecimal(preco.getText(), "Preço");
                return tipo.getValue().equals("Carro")
                        ? new Carro(placaChassi, modelo.getText(), marca.getText(), anoNum, precoNum, ConversorNumero.paraInteiro(detalhe.getText(), "Número de portas"))
                        : new Caminhonete(placaChassi, modelo.getText(), marca.getText(), anoNum, precoNum, ConversorNumero.paraDecimal(detalhe.getText(), "Capacidade de carga"));
            } catch (IllegalArgumentException ex) { aviso(ex.getMessage()); return null; } // inclui DadosInvalidosException
        });
        Optional<Veiculo> resultado = dialog.showAndWait();
        resultado.ifPresent(v -> {
            try {
                if (atual == null) controller.cadastrarVeiculo(v);
                else controller.atualizarVeiculo(v);
                atualizarTabela();
            } catch (VeiculoDuplicadoException | IllegalArgumentException | ErroPersistenciaException | VeiculoNaoEncontradoException ex) {
                mostrarErro("Não foi possível salvar o veículo", ex.getMessage());
            }
        });
    }

    /** Campo de texto com o valor atual (na edição) e um exemplo em cinza (no cadastro). */
    private TextField campo(String valorAtual, String exemplo) { TextField f = new TextField(valorAtual); f.setPromptText(exemplo); f.setPrefWidth(260); return f; }
    private String botaoPrimario() { return "-fx-background-color: #1769aa; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 10 16;"; }
    private void removerSelecionado() {
        Veiculo v = tabela.getSelectionModel().getSelectedItem(); if (v == null) { aviso("Selecione um veículo na tabela."); return; }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Remover " + v.getMarca() + " " + v.getModelo() + " do estoque?", ButtonType.CANCEL, ButtonType.OK);
        confirmacao.setHeaderText("Confirmar remoção");
        confirmacao.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            try {
                controller.removerVeiculo(v.getChassi());
                atualizarTabela();
            } catch (ErroPersistenciaException | VeiculoNaoEncontradoException ex) {
                mostrarErro("Não foi possível remover o veículo", ex.getMessage());
            }
        });
    }
    private void aviso(String mensagem) { new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK).showAndWait(); }
    private void mostrarErro(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alerta.setTitle("Erro");
        alerta.setHeaderText(titulo);
        alerta.showAndWait();
    }
    private void carregarExemplos() {
        controller.cadastrarVeiculo(new Carro("9BWZZZ377VT004251", "Polo", "Volkswagen", 2023, 89900, 4));
        controller.cadastrarVeiculo(new Carro("9BGKS48U0FG123456", "Onix", "Chevrolet", 2022, 75900, 4));
        controller.cadastrarVeiculo(new Caminhonete("1FTFW1ET5EFA12345", "Ranger", "Ford", 2024, 239900, 1100));
    }
    public static void main(String[] args) { launch(args); }
}

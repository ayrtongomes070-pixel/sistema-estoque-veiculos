package com.estoqueveiculos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Ponto de entrada da aplicação JavaFX.
 * TODO: substituir a tela inicial simples por FXMLLoader carregando view/estoque.fxml.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Sistema de Estoque de Veículos");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Sistema de Estoque de Veículos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.estoqueveiculos.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gerencia a conexão com o banco de dados MySQL, lendo as
 * configurações do arquivo db.properties (em src/main/resources).
 */
public class ConexaoBD {

    private static Properties carregarProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = ConexaoBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException(
                        "Arquivo db.properties não encontrado em src/main/resources. " +
                        "Copie db.properties.example e preencha suas credenciais.");
            }
            props.load(input);
        }
        return props;
    }

    public static Connection conectar() throws SQLException, IOException {
        Properties props = carregarProperties();
        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.usuario");
        String senha = props.getProperty("db.senha");
        return DriverManager.getConnection(url, usuario, senha);
    }
}

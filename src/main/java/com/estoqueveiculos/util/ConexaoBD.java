package com.estoqueveiculos.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Utilitário para abrir uma conexão MySQL configurada em {@code db.properties}. */
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

    /** Abre uma conexão usando as credenciais do arquivo de configuração.
     * @return conexão JDBC aberta
     * @throws SQLException se o driver não conseguir estabelecer a conexão
     * @throws IOException se o arquivo de configuração estiver ausente ou ilegível
     */
    public static Connection conectar() throws SQLException, IOException {
        Properties props = carregarProperties();
        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.usuario");
        String senha = props.getProperty("db.senha");
        return DriverManager.getConnection(url, usuario, senha);
    }
}

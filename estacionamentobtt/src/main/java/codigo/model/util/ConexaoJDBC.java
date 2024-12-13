package codigo.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ConexaoJDBC {
    // URL de conexão com o banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "estacionamentodbb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Conexão única para o padrão Singleton
    private static Connection connection;

    // Método para obter a conexão com o banco de dados
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (ConexaoJDBC.class) { // Sincronização para garantir segurança em multi-thread
                if (connection == null) {
                    try {
                        // Inicializa o banco de dados antes de estabelecer a conexão
                        initializeDatabase();

                        // Carrega o driver do MySQL
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(URL + DB_NAME + "?useTimezone=true&serverTimezone=UTC", USER, PASSWORD);
                        System.out.println("Conexão estabelecida com sucesso.");
                    } catch (SQLException e) {
                        System.err.println("Erro ao estabelecer a conexão com o banco de dados: " + e.getMessage());
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        System.err.println("Driver JDBC não encontrado.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    // Método para fechar a conexão explicitamente
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada com sucesso.");
                connection = null; // Reseta a conexão para permitir nova conexão no futuro
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Método para inicializar o banco de dados
    private static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Cria o banco de dados se não existir
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Banco de dados '" + DB_NAME + "' verificado/criado com sucesso.");

            // Conecta ao banco de dados específico
            try (Connection dbConnection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
                 Statement dbStatement = dbConnection.createStatement()) {

                // Criação das tabelas na ordem correta
                dbStatement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS estacionamentos (
                        nome VARCHAR(120) NOT NULL,
                        linha INT DEFAULT NULL,
                        coluna INT DEFAULT NULL,
                        PRIMARY KEY (nome)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

                dbStatement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS clientes (
                        id VARCHAR(11) NOT NULL,
                        nome VARCHAR(120) DEFAULT NULL,
                        PRIMARY KEY (id),
                        UNIQUE KEY id_UNIQUE (id)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

                dbStatement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS veiculos (
                        placa CHAR(8) NOT NULL,
                        id_cliente VARCHAR(11) DEFAULT NULL,
                        PRIMARY KEY (placa),
                        KEY id_cliente_idx (id_cliente),
                        CONSTRAINT id_cliente FOREIGN KEY (id_cliente) REFERENCES clientes (id)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

                dbStatement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS vagas (
                        id CHAR(2) NOT NULL,
                        nome_estacionamento VARCHAR(120) NOT NULL,
                        estado TINYINT NOT NULL DEFAULT 1,
                        PRIMARY KEY (id),
                        KEY nome_estacionamento_vaga_idx (nome_estacionamento),
                        CONSTRAINT nome_estacionamento_vaga FOREIGN KEY (nome_estacionamento) REFERENCES estacionamentos (nome)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

                dbStatement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS usoVaga (
                        id INT NOT NULL AUTO_INCREMENT,
                        id_vaga CHAR(2) NOT NULL,
                        nome_estacionamento VARCHAR(120) NOT NULL,
                        placa_veiculo CHAR(8) NOT NULL,
                        entrada CHAR(29) NOT NULL,
                        saida CHAR(29) DEFAULT NULL,
                        PRIMARY KEY (id),
                        KEY id_veiculo_idx (placa_veiculo),
                        KEY id_vaga_uso_idx (id_vaga),
                        KEY nome_estacionamento_uso_idx (nome_estacionamento),
                        CONSTRAINT id_vaga_uso FOREIGN KEY (id_vaga) REFERENCES vagas (id),
                        CONSTRAINT nome_estacionamento_uso FOREIGN KEY (nome_estacionamento) REFERENCES estacionamentos (nome),
                        CONSTRAINT placa_veiculo_uso FOREIGN KEY (placa_veiculo) REFERENCES veiculos (placa)
                    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

                System.out.println("Tabelas criadas/verificadas com sucesso.");

                // População das tabelas
                dbStatement.executeUpdate("""
                    INSERT IGNORE INTO estacionamentos (nome, linha, coluna) VALUES
                    ('estacionamentoTeste', 3, 8);
                """);

                dbStatement.executeUpdate("""
                    INSERT IGNORE INTO vagas (id, nome_estacionamento, estado) VALUES
                    ('A2', 'estacionamentoTeste', 1),
                    ('A3', 'estacionamentoTeste', 1),
                    ('A4', 'estacionamentoTeste', 1),
                    ('A5', 'estacionamentoTeste', 1),
                    ('B1', 'estacionamentoTeste', 1),
                    ('B2', 'estacionamentoTeste', 1),
                    ('B3', 'estacionamentoTeste', 1),
                    ('B4', 'estacionamentoTeste', 1),
                    ('B5', 'estacionamentoTeste', 1),
                    ('C1', 'estacionamentoTeste', 1),
                    ('C2', 'estacionamentoTeste', 1),
                    ('C3', 'estacionamentoTeste', 1),
                    ('C4', 'estacionamentoTeste', 1),
                    ('C5', 'estacionamentoTeste', 1),
                    ('D1', 'estacionamentoTeste', 1),
                    ('D2', 'estacionamentoTeste', 1),
                    ('D3', 'estacionamentoTeste', 1),
                    ('D4', 'estacionamentoTeste', 1),
                    ('D5', 'estacionamentoTeste', 1),
                    ('E1', 'estacionamentoTeste', 1),
                    ('E2', 'estacionamentoTeste', 1),
                    ('E3', 'estacionamentoTeste', 1),
                    ('E4', 'estacionamentoTeste', 1),
                    ('E5', 'estacionamentoTeste', 1);
                """);

                // Gerar 20 clientes aleatórios
                Random random = new Random();
                for (int i = 1; i <= 20; i++) {
                    String id = String.format("%011d", random.nextInt(1_000_000_000));
                    String nome = "Cliente" + i;
                    dbStatement.executeUpdate(String.format("""
                        INSERT IGNORE INTO clientes (id, nome) VALUES ('%s', '%s');
                    """, id, nome));
                }

                // Gerar 10 veículos aleatórios
                for (int i = 1; i <= 10; i++) {
                    String placa = String.format("ABC%04d", random.nextInt(10_000));
                    String clienteId = String.format("%011d", random.nextInt(1_000_000_000));
                    dbStatement.executeUpdate(String.format("""
                        INSERT IGNORE INTO veiculos (placa, id_cliente) VALUES ('%s', '%s');
                    """, placa, clienteId));
                }

                System.out.println("Dados populados com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
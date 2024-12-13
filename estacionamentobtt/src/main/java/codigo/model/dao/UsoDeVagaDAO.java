package codigo.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import codigo.model.util.ConexaoJDBC;
import codigo.model.entidades.UsoDeVaga;
import codigo.model.entidades.Vaga;

public class UsoDeVagaDAO {


    private static final double fracao_uso_minutos = 15;
    private static final double valor_fracao = 4.0;
    private static final double valor_maximo = 50.0;

    private Vaga vaga;
    private LocalDateTime entrada;
    private LocalDateTime saida;


    public LocalDateTime getEntrada() {
        return entrada;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void UsoDeVaga(Vaga vaga) {
        if (vaga != null && vaga.disponivel()) {
            vaga.estacionar();
            this.vaga = vaga;
            entrada = LocalDateTime.now();
        } else {
            throw new Error("Erro: vaga ocupada / inexistente");
        }
    }

    public void cadastrarUsoDeVaga(UsoDeVaga usoDeVaga, String nomeEstacionamento, String placaVeiculo) {
        String sql = "INSERT INTO usovaga (ID_VAGA, NOME_ESTACIONAMENTO, PLACA_VEICULO, ENTRADA, SAIDA) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.setString(1, usoDeVaga.getVaga().getId());
            ps.setString(2, nomeEstacionamento);
            ps.setString(3, placaVeiculo);
            ps.setString(4, usoDeVaga.getEntrada().toString());

            if(usoDeVaga.getSaida() == null) {
                ps.setString(5, "null");
            } else {
                ps.setString(5, usoDeVaga.getSaida().toString());
            }

            ps.execute();

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void UsoDeVaga(Vaga vaga, LocalDateTime entrada, LocalDateTime saida) {
        this.vaga = vaga;
        this.entrada = entrada;
        this.saida = saida;
    }


    public double valorPago() {
        LocalDateTime horaSaida = (saida == null) ? LocalDateTime.now() : saida;

        long tempoTotalMinutos = ChronoUnit.MINUTES.between(entrada, horaSaida);
        double fracoesUsadas = Math.ceil((double) tempoTotalMinutos / fracao_uso_minutos);
        double precoEstimado = valor_fracao * fracoesUsadas;


        precoEstimado = Math.min(precoEstimado, valor_maximo);


        precoEstimado = ajustarPrecoPorTipoDeVaga(precoEstimado, vaga.getId());

        return precoEstimado;
    }

    public boolean sair() {
        saida = LocalDateTime.now();
        vaga.sair();
        return true;
    }



    private double ajustarPrecoPorTipoDeVaga(double preco, String idVaga) {
        if (idVaga == null || idVaga.isEmpty()) {
            System.out.println("ID da vaga inválido, sem ajuste aplicado.");
            return preco;
        }


        idVaga = idVaga.toUpperCase();


        switch (idVaga.charAt(0)) {
            case 'A':
                return preco;
            case 'B':
                return preco * 0.85;
            case 'C':
                return preco * 0.87;
            case 'D':
                return preco * 1.20;
            default:
                System.out.println("Tipo de vaga desconhecido. Sem ajuste.");
                return preco;
        }
    }

    public boolean estaEstacionado(String placa) {
        String sql = "SELECT MAX(PLACA_VEICULO = ? AND SAIDA = 'null') AS 'ESTA_ESTACIONADO' FROM usovaga";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.setString(1, placa);

            ps.execute();

            ResultSet rs = ps.getResultSet();
            rs.next();

            if(rs.getInt("esta_estacionado") == 1) {
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }



    public String[] consultarInfoUsoAtual(String idVaga, String nomeEstacionamento) {
        String[] dados = new String[2];
        String placa_veiculo, entrada;

        String sql = "SELECT PLACA_VEICULO, ENTRADA FROM usovaga WHERE ID_VAGA = ? AND NOME_ESTACIONAMENTO = ? AND SAIDA = 'null'";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.setString(1, idVaga);
            ps.setString(2, nomeEstacionamento);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                placa_veiculo = rs.getString("placa_veiculo");
                entrada = rs.getString("entrada");

                dados[0] = placa_veiculo;
                dados[1] = entrada;
            }

            return dados;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<UsoDeVaga> lerUsosDeVaga(String nomeEstacionamento, String placa) {
        ArrayList<UsoDeVaga> usosDeVaga = new ArrayList<UsoDeVaga>();
        String id_vaga, entrada, saida;
        Vaga vagaAux;
        LocalDateTime saidaTratada;
        boolean estadoVaga;

        String sql = "SELECT ID_VAGA, ENTRADA, SAIDA FROM usovaga WHERE NOME_ESTACIONAMENTO = ? AND PLACA_VEICULO = ?";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.setString(1, nomeEstacionamento);
            ps.setString(2, placa);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                id_vaga = rs.getString("id_vaga");
                entrada = rs.getString("entrada");
                saida = rs.getString("saida");

                if(saida.equalsIgnoreCase("null")) {
                    estadoVaga = false;
                    saidaTratada = null;
                } else {
                    estadoVaga = true;
                    saidaTratada = LocalDateTime.parse(saida);
                }
                vagaAux = new Vaga(id_vaga, estadoVaga);
                usosDeVaga.add(new UsoDeVaga(vagaAux, LocalDateTime.parse(entrada), saidaTratada));
            }

            return usosDeVaga;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registrarSaida(String placa, String entrada, String saida) {
        String sql = "UPDATE usovaga SET SAIDA = ? WHERE PLACA_VEICULO = ? AND ENTRADA = ?";

        try{
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.setString(1, saida);
            ps.setString(2, placa);
            ps.setString(3, entrada);

            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }


}

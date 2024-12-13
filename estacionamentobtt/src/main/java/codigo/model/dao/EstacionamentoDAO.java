package codigo.model.dao;

import codigo.model.util.ConexaoJDBC;
import codigo.model.entidades.Estacionamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EstacionamentoDAO {

    public ArrayList<String> lerNomesEstacionamentos() {
        ArrayList<String> nomes = new ArrayList<String>();
        String sql = "SELECT nome FROM ESTACIONAMENTOS";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                nomes.add(rs.getString("nome"));
            }

            return nomes;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Estacionamento lerEstacionamento(String nomeEstacionamento) {
        Estacionamento estacionamentoAtual;
        String sql = "SELECT * FROM ESTACIONAMENTOS WHERE nome = ?";
        int quantidade_fileiras, vagas_por_fileira;

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);
            ps.setString(1, nomeEstacionamento);
            ps.execute();
            ResultSet result = ps.getResultSet();

            if(result.next()) {
                quantidade_fileiras = result.getInt("linha");
                vagas_por_fileira = result.getInt("coluna");
                estacionamentoAtual = new Estacionamento(nomeEstacionamento, quantidade_fileiras, vagas_por_fileira);
                estacionamentoAtual.setVagas(new VagaDAO().lerVagas(estacionamentoAtual.getNome()));
                estacionamentoAtual.setClientes(new ClienteDAO().lerClientes(nomeEstacionamento));

                return estacionamentoAtual;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void cadastrarEstacionamento(Estacionamento estacionamento) {
        String sql = "INSERT INTO ESTACIONAMENTOS (nome, quantidade_fileiras, vagas_por_fileira) VALUES (?, ?, ?)";

        try{
            PreparedStatement prepare = ConexaoJDBC.getConnection().prepareStatement(sql);
            prepare.setString(1, estacionamento.getNome());
            prepare.setInt(2, estacionamento.getQuantFileiras());
            prepare.setInt(3, estacionamento.getVagasPorFileira());
            prepare.execute();

            prepare.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

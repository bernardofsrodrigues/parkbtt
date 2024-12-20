package codigo.model.dao;

import codigo.model.util.ConexaoJDBC;
import codigo.model.entidades.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VeiculoDAO {

    public void cadastrarVeiculo(Veiculo veiculo, String idCliente) {
        String sql = "INSERT INTO VEICULOS (placa, ID_CLIENTE) VALUES (?, ?)";

        try{
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);
            ps.setString(1, veiculo.getPlaca());
            ps.setString(2, idCliente);
            ps.execute();

            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean veiculoRegistrado(String placa) {
        String sql = "SELECT MAX(placa = ?) AS 'POSSUI_VEICULO' FROM VEICULOS";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);
            ps.setString(1, placa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();

            if(rs.getInt("possui_veiculo") == 1) {
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean temVeiculos(String cpfCliente) {
        String sql = "SELECT MAX(ID_CLIENTE = ?) AS 'POSSUI_VEICULO' FROM VEICULOS";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);
            ps.setString(1, cpfCliente);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();

            if(rs.getInt("possui_veiculo") == 1) {
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
    public ArrayList<Veiculo> lerVeiculos(String idCliente, String nomeEstacionamento) {
        ArrayList<Veiculo> veiculos = new ArrayList<Veiculo>();
        String placa_veiculo;

        String sql = "SELECT placa FROM VEICULOS WHERE ID_CLIENTE = ?";

        try {
            PreparedStatement ps = ConexaoJDBC.getConnection().prepareStatement(sql);
            ps.setString(1, idCliente);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                placa_veiculo = rs.getString("placa");
                veiculos.add(new Veiculo(placa_veiculo, new UsoDeVagaDAO().lerUsosDeVaga(nomeEstacionamento, placa_veiculo)));
            }

            return veiculos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



}

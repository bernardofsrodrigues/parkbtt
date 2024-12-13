package codigo.controller;

import codigo.model.entidades.*;
import codigo.model.dao.VeiculoDAO;
import codigo.view.Veiculos;
import codigo.view.CadastrarVeiculo;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Iterator;

public class VeiculosController {
    public Estacionamento estacionamentoAtual;
    public Veiculos view;
    public CadastrarVeiculo viewDois;
    public codigo.model.entidades.Estacionamento estacionamentoDois;

    public VeiculosController(Veiculos view, Estacionamento estacionamentoAtual) {
        this.view = view;
        this.estacionamentoAtual = estacionamentoAtual;
    }
    public VeiculosController(CadastrarVeiculo view, codigo.model.entidades.Estacionamento estacionamentoAtual) {
        this.viewDois = view;
        this.estacionamentoDois = estacionamentoAtual;
    }

    public void popularTabela(DefaultTableModel tableModel, String cpfCliente) {
        Veiculo auxVeiculo;
        ArrayList<Veiculo> auxVeiculos;
        ArrayList<UsoDeVaga> auxUsosVeiculo;
        UsoDeVaga auxUltimoUso;

        auxVeiculos = new VeiculoDAO().lerVeiculos(cpfCliente, estacionamentoAtual.getNome());

        Iterator<Veiculo> iteratorVeiculos = auxVeiculos.iterator();

        while (iteratorVeiculos.hasNext()) {
            auxVeiculo = iteratorVeiculos.next();
             auxUsosVeiculo = auxVeiculo.getUsos();
             if (!auxUsosVeiculo.isEmpty()) {
                 auxUltimoUso = auxUsosVeiculo.get(auxUsosVeiculo.size() - 1);
                 if (auxUltimoUso.getSaida() == null) {
                     tableModel.addRow(new Object[]{auxVeiculo.getPlaca(), auxUltimoUso.getVaga().getId() + ", " + estacionamentoAtual.getNome()});
                 } else {
                     tableModel.addRow(new Object[]{auxVeiculo.getPlaca(), "-"});
                 }
             } else {
                 tableModel.addRow(new Object[]{auxVeiculo.getPlaca(), "-"});
             }
        }
    }

    public void registrarVeiculo(String placa, String cpfCliente) {
        Veiculo veiculo = new Veiculo(placa);
        Iterator<Cliente> iteratorClientes = estacionamentoDois.getClientes().iterator();
        Cliente auxCliente;

        while(iteratorClientes.hasNext()) {
            auxCliente = iteratorClientes.next();

            if(auxCliente.getId().equalsIgnoreCase(cpfCliente)) {
                auxCliente.addVeiculo(veiculo);
                new VeiculoDAO().cadastrarVeiculo(veiculo, cpfCliente);
                viewDois.dispose(); new codigo.view.Estacionamento(estacionamentoDois).setVisible(true);
            }
        }

    }
}

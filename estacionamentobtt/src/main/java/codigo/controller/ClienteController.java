package codigo.controller;

import codigo.model.dao.VeiculoDAO;
import codigo.model.entidades.Cliente;
import codigo.model.entidades.Estacionamento;
import codigo.model.entidades.Veiculo;
import codigo.view.PainelDeTodosClientes;
import codigo.view.CadastrarCliente;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Iterator;

public class ClienteController {
    public CadastrarCliente view;
    public Estacionamento estacionamentoAtual;
    public PainelDeTodosClientes viewDois;
    public Estacionamento estacionamentoDois;

    public ClienteController(PainelDeTodosClientes view, Estacionamento estacionamentoAtual) {
        this.viewDois = view; this.estacionamentoDois = estacionamentoAtual;
    }

    public ClienteController(CadastrarCliente view, Estacionamento estacionamentoAtual) {
        this.view = view;
        this.estacionamentoAtual = estacionamentoAtual;
    }

    public void registrarCliente() {
        estacionamentoAtual.addCliente(new Cliente(view.getTextId().getText(), view.getTextName().getText(), new ArrayList<Veiculo>()));
    }

    public void popularTabela(DefaultTableModel tableModel) {
        Iterator<Cliente> iteratorClientes = estacionamentoDois.getClientes().iterator();
        Cliente auxCliente;

        while(iteratorClientes.hasNext()) {
            auxCliente = iteratorClientes.next();

            tableModel.addRow(new Object[]{auxCliente.getNome(), auxCliente.getId()});
        }
    }

    public boolean clienteTemVeiculos(String cpfCliente) {
        return new VeiculoDAO().temVeiculos(cpfCliente);
    }
}

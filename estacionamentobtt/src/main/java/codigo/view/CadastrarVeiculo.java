package codigo.view;

import codigo.controller.VeiculosController;
import codigo.model.entidades.Estacionamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastrarVeiculo extends JFrame {

    private JPanel panel1;
    private JTextField textFieldPlaca;
    private JButton btnCadastrar;
    private JLabel labelPlaca;
    private JTextField textFieldCpfCliente;
    private JLabel labelCpfCliente;
    private final VeiculosController controller;

    public CadastrarVeiculo(Estacionamento estacionamentoAtual, String placa) {
        this.controller = new VeiculosController(this, estacionamentoAtual);

        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textFieldPlaca.setText(placa);
        textFieldPlaca.setEnabled(false);

        add(panel1);


        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.registrarVeiculo(placa, textFieldCpfCliente.getText());
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

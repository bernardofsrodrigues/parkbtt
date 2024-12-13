package codigo.view;

import codigo.controller.ClienteController;
import codigo.model.entidades.Estacionamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastrarCliente extends JFrame {

    private JPanel panel1;
    private JTextField textId;
    private JButton salvarButton;
    private JTextField textName;
    private JButton btnCancelar;
    private ClienteController controller;

    public CadastrarCliente(Estacionamento estacionamentoAtual) {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        add(panel1);

        controller = new ClienteController(this, estacionamentoAtual);
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.registrarCliente();
                estacionamentoAtual.gerar();
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); new PainelDeClientes(estacionamentoAtual).setVisible(true);
            }
        });
    }

    public JTextField getTextId() {
        return textId;
    }

    public JTextField getTextName() {
        return textName;
    }
}

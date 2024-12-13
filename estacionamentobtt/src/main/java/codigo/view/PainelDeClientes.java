package codigo.view;


import codigo.model.entidades.Estacionamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PainelDeClientes extends JFrame {

    private JButton btnCadastrar;
    private JPanel panel1;
    private JButton btnListagem;
    private JLabel labelClientes;
    private JButton btnVoltar;

    public PainelDeClientes(Estacionamento estacionamentoAtual) {
        labelClientes.setText( estacionamentoAtual.getNome());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel1);

        setVisible(true);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastrarCliente(estacionamentoAtual).setVisible(true);
            }
        });

        btnListagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); new PainelDeTodosClientes(estacionamentoAtual).setVisible(true);
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); new PainelDeControle(estacionamentoAtual).setVisible(true);
            }
        });
    }
}

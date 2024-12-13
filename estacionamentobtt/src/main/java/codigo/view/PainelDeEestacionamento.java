package codigo.view;

import javax.swing.*;

import codigo.controller.EstacionamentoController;

public class PainelDeEestacionamento extends JFrame {
    private JPanel panel1;
    private JButton btnCarregar;
    private JTextField textField1;
    private JComboBox comboBoxEstacionamentos;
    private JLabel labelCarregarEstacionamento;
    private final EstacionamentoController controller;

    public JComboBox getComboBoxEstacionamentos() {
        return comboBoxEstacionamentos;
    }

    public void setComboBoxEstacionamentos(JComboBox comboBoxEstacionamentos) {
        this.comboBoxEstacionamentos = comboBoxEstacionamentos;
    }

    public PainelDeEestacionamento() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel1);

        setVisible(true);

        controller = new EstacionamentoController(this);

        controller.carregarComboBox();

        btnCarregar.addActionListener(e -> {
            dispose();
            new PainelDeControle(controller.carregarEstacionamento());
        });
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public void exibeMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem);
    }

}

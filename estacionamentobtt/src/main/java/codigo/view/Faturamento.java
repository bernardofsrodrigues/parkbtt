package codigo.view;

import codigo.controller.FaturamentoController;
import codigo.model.entidades.Estacionamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Faturamento extends JFrame {

    private FaturamentoController controller;
    private JPanel panel1;
    private JLabel labelEstatisticasEstacionamento;
    private JComboBox comboBoxMes;
    private JLabel labelMes;
    private JTextArea textAreaEstatisticas;
    private JButton btnVoltar;

    public JTextArea getTextAreaEstatisticas() {
        return textAreaEstatisticas;
    }

    public Faturamento(Estacionamento estacionamentoAtual) {
        this.controller = new FaturamentoController(this, estacionamentoAtual);

        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        labelEstatisticasEstacionamento.setText( estacionamentoAtual.getNome());

        add(panel1);

        setVisible(true);

        controller.recuperarEstatisticas("Todos");

        textAreaEstatisticas.setEditable(false);
        comboBoxMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaEstatisticas.setText("");
                controller.recuperarEstatisticas(comboBoxMes.getSelectedItem().toString());
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

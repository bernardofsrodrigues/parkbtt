package codigo.view;

import codigo.controller.VagaController;
import codigo.model.entidades.Vaga;
import codigo.model.entidades.Estacionamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OcuparVaga extends JFrame{
    private JLabel tituloJanela;
    private JTextField textFieldPlaca;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JLabel labelTextPlaca;
    private JLabel idVaga;
    private JPanel panel1;
    private VagaController controller;

    public JTextField getTextFieldPlaca() {
        return textFieldPlaca;
    }

    public OcuparVaga(Estacionamento estacionamentoAtual, Vaga vaga) {
        controller = new VagaController(this, estacionamentoAtual);
        idVaga.setText(vaga.getId());

        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel1);


        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(vaga.disponivel()) {
                    if(controller.registrar(textFieldPlaca.getText(), vaga.getId())) {
                        dispose();
                        new PainelDeControle(estacionamentoAtual).setVisible(true);
                    } else {
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vaga indisponível!");
                }
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new codigo.view.Estacionamento(estacionamentoAtual).setVisible(true);
            }
        });
    }

    public void exibeMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem);
    }

    public int exibeDialogo(String mensagem) { // retorna a opcao escolhida
        return JOptionPane.showConfirmDialog(this, mensagem, "Confirmação", JOptionPane.YES_NO_OPTION);
    }
}

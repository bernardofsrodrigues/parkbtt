package codigo.view;

import codigo.controller.EstacionamentoController;
import codigo.model.entidades.Vaga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Estacionamento extends JFrame {
    private JLabel nomeEstacionamento;
    private JButton btnReturn;
    private final EstacionamentoController controller;

    public Estacionamento(codigo.model.entidades.Estacionamento estacionamentoAtual) {
        int counter = 0;
        controller = new EstacionamentoController(this, estacionamentoAtual);
        nomeEstacionamento.setHorizontalAlignment(SwingConstants.CENTER);
        nomeEstacionamento.setText( estacionamentoAtual.getNome());
        setTitle(estacionamentoAtual.getNome());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel painelVagas = new JPanel(new GridLayout(estacionamentoAtual.getQuantFileiras(), estacionamentoAtual.getVagasPorFileira()));

        for(int i = 0; i < estacionamentoAtual.getQuantFileiras(); i++) {
            for(int j = 0; j < estacionamentoAtual.getVagasPorFileira(); j++) {
                painelVagas.add(criarVaga(controller.getVagas().get(counter), estacionamentoAtual));
                counter++;
            }
        }
        add(painelVagas);
        add(btnReturn, BorderLayout.AFTER_LAST_LINE);
        add(nomeEstacionamento, BorderLayout.BEFORE_FIRST_LINE);
        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); new PainelDeControle(estacionamentoAtual).setVisible(true);
            }
        });
    }

    private JPanel criarVaga(Vaga vaga, codigo.model.entidades.Estacionamento estacionamentoAtual) {
        JPanel result = new JPanel();
        result.add(btnReturn);
        result.setPreferredSize(new Dimension(60, 40)); // Tamanho da vaga

        if(vaga.disponivel()) {
            result.setBackground(Color.YELLOW); // Caso disponivel, vaga setada para green
        } else {
            result.setBackground(Color.BLUE); // Caso contrario, vaga setada para red
        }

        result.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Borda preta para destacar as vagas

        result.add(new JLabel(vaga.getId()), SwingConstants.CENTER);

        result.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                if(result.getBackground() == Color.YELLOW) {
                    new OcuparVaga(estacionamentoAtual, vaga).setVisible(true);
                } else if (result.getBackground() == Color.BLUE) {
                    new CadastrarSaidaDoVeiculo(estacionamentoAtual, vaga).setVisible(true);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        return result;
    }
}

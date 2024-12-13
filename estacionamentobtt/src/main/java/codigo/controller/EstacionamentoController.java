package codigo.controller;
import java.util.ArrayList;
import java.util.Iterator;

import codigo.model.dao.EstacionamentoDAO;
import codigo.model.entidades.Vaga;
import codigo.view.Estacionamento;
import codigo.view.PainelDeEestacionamento;

import javax.swing.*;

public class EstacionamentoController {
    public Estacionamento view;
    
    private codigo.model.entidades.Estacionamento estacionamento;

    public PainelDeEestacionamento viewDois;

    public EstacionamentoController(PainelDeEestacionamento view) {
        this.viewDois = view;
    }

    public EstacionamentoController(Estacionamento view, codigo.model.entidades.Estacionamento estacionamento) {
        this.view = view; this.estacionamento = estacionamento;
    }

    public ArrayList<Vaga> getVagas() {return estacionamento.getVagas();}


    public codigo.model.entidades.Estacionamento carregarEstacionamento() {
        String nome = viewDois.getComboBoxEstacionamentos().getSelectedItem().toString();

        if(nome != null) {
            codigo.model.entidades.Estacionamento estacionamentoAtual = new EstacionamentoDAO().lerEstacionamento(nome);

            viewDois.exibeMensagem(nome + " selecionado!\n");

            return estacionamentoAtual;
        } else {
            throw new Error("Erro ao selecionar estacionamento!");
        }
    }

    public void carregarComboBox() {
        ArrayList<String> nomesEstacionamentos;
        JComboBox auxComboBox = viewDois.getComboBoxEstacionamentos();

        nomesEstacionamentos = new EstacionamentoDAO().lerNomesEstacionamentos();

        Iterator<String> iteratorNomes = nomesEstacionamentos.iterator();

        while(iteratorNomes.hasNext()) {
            auxComboBox.addItem(iteratorNomes.next());
        }
    }
}

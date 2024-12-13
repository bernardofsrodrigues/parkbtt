package codigo.controller;

import codigo.model.dao.VeiculoDAO;
import codigo.model.entidades.Estacionamento;
import codigo.model.dao.UsoDeVagaDAO;
import codigo.model.dao.VagaDAO;
import codigo.view.OcuparVaga;
import codigo.view.CadastrarVeiculo;
import codigo.view.CadastrarSaidaDoVeiculo;

import javax.swing.*;
import java.time.LocalDateTime;

public class VagaController {
    public CadastrarSaidaDoVeiculo view;
    public Estacionamento estacionamentoAtual;
    public OcuparVaga viewDois;
    public Estacionamento estacionamentoDois;

    public VagaController(OcuparVaga view, Estacionamento estacionamentoAtual) {
        this.viewDois = view;
        this.estacionamentoDois = estacionamentoAtual;
    }

    public VagaController(CadastrarSaidaDoVeiculo view, Estacionamento estacionamentoAtual) {
        this.view = view; this.estacionamentoAtual = estacionamentoAtual;
    }

    public void buscarDadosUsoAtual(String idVaga) {
        String[] dados = new UsoDeVagaDAO().consultarInfoUsoAtual(idVaga, estacionamentoAtual.getNome());

        view.getLabelVeiculo().setText(dados[0]);
        view.getLabelEntrada().setText(dados[1]);
    }

    public void registrarSaida(String idVaga) {
        double aPagar;
        aPagar = estacionamentoAtual.procurarVeiculo(view.getLabelVeiculo().getText()).sair();

        new VagaDAO().atualizarEstadoVaga(idVaga, estacionamentoAtual.getNome());
        new UsoDeVagaDAO().registrarSaida(view.getLabelVeiculo().getText(), view.getLabelEntrada().getText(), LocalDateTime.now().toString());

        view.exibeMensagem("Vaga liberada! Faça a cobrança de R$" + aPagar + " ao cliente");
    }

    public boolean registrar(String placa, String idVaga) {
        if(new VeiculoDAO().veiculoRegistrado(placa)) {
            if (!new UsoDeVagaDAO().estaEstacionado(placa)) {
                new UsoDeVagaDAO().cadastrarUsoDeVaga(estacionamentoDois.estacionar(placa, idVaga), estacionamentoDois.getNome(), placa);
                new VagaDAO().atualizarEstadoVaga(idVaga, estacionamentoDois.getNome());
                viewDois.exibeMensagem("Veículo estacionado com sucesso.");
                return true;
            } else {
                viewDois.exibeMensagem("Erro: Veículo já se encontra estacionado. Tente novamente!");
                viewDois.getTextFieldPlaca().setText("");
                return false;
            }
        } else {
            if(viewDois.exibeDialogo("Placa não encontrada na base de dados. Deseja registrar um novo veículo?") == JOptionPane.YES_OPTION) {
                new CadastrarVeiculo(estacionamentoDois, placa).setVisible(true);
            }
            return false;
        }
    }
}

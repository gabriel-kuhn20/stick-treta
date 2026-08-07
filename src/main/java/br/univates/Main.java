package br.univates;

import br.univates.sticktreta.config.Configuracoes;
import br.univates.sticktreta.engine.MotorJogo;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame janela = new JFrame(Configuracoes.TITULO_JOGO);
        MotorJogo motor = new MotorJogo();

        janela.add(motor.getPainelJogo());

        // Garante que o programa feche no Gerenciador de Tarefas ao clicar no "X"
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        janela.pack();

        janela.setLocationRelativeTo(null);

        janela.setVisible(true);

        motor.iniciarJogo();
    }
}
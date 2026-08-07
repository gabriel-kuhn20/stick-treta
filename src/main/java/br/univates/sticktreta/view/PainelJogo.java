package br.univates.sticktreta.view;

import br.univates.sticktreta.config.Configuracoes;
import br.univates.sticktreta.interfaces.Tela;

import javax.swing.*;
import java.awt.*;

/**
 * Camada de visualização
 */
public class PainelJogo extends JPanel {
    private Tela telaAtual;

    public PainelJogo() {
        setPreferredSize(new Dimension(Configuracoes.LARGURA_TELA, Configuracoes.ALTURA_TELA));
        setBackground(Color.WHITE);
        setFocusable(true);
    }

    public void setTelaAtual(Tela telaAtual) {
        this.telaAtual = telaAtual;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (telaAtual != null) {
            telaAtual.desenhar(g, getWidth(), getHeight());
        }
    }
}
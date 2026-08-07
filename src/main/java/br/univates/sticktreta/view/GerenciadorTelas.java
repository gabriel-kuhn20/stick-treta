package br.univates.sticktreta.view;

import br.univates.sticktreta.model.StickFighter;
import br.univates.sticktreta.util.MusicaFundo;
import br.univates.sticktreta.util.TocadorSom;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GerenciadorTelas {
    /**
     * Responsável por gerenciar as transições de tela e o estado da partida.
     */

    public enum EstadoJogo
    {
        JOGANDO,
        GAME_OVER
    }

    private EstadoJogo estadoAtual;
    private final PainelJogo painelJogo;

    private StickFighter jogador1;
    private StickFighter jogador2;

    private TelaJogo telaJogo;

    public GerenciadorTelas(PainelJogo painelJogo) {
        this.painelJogo = painelJogo;
        iniciarNovaPartida();
    }

    public void iniciarNovaPartida() {
        this.jogador1 = new StickFighter(300,
                650,
                Color.BLUE,
                KeyEvent.VK_SPACE,
                KeyEvent.VK_W,
                KeyEvent.VK_D,
                KeyEvent.VK_A,
                true
        );

        this.jogador2 = new StickFighter(1100,
                650,
                Color.RED,
                KeyEvent.VK_ENTER,
                KeyEvent.VK_UP,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_LEFT,
                false
        );

        this.telaJogo = new TelaJogo(jogador1, jogador2);
        this.painelJogo.setTelaAtual(telaJogo);
        this.estadoAtual = EstadoJogo.JOGANDO;
        MusicaFundo.tocar("/assets/audio/MusicaFundo.wav");
    }

    public void atualizar() {
        if(estadoAtual == EstadoJogo.JOGANDO)
        {
            jogador1.atualizar();
            jogador2.atualizar();

            if (jogador1.getVida() <= 0 || jogador2.getVida() <= 0)
            {
                estadoAtual = EstadoJogo.GAME_OVER;
                TocadorSom.tocar("/assets/audio/Vitoria.wav");
                MusicaFundo.parar();

            }
        }
    }

    public void processarTeclaPressionada(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }

        // Se a partida acabou e o jogador pressionou R, reinicia
        if (estadoAtual == EstadoJogo.GAME_OVER && keyCode == KeyEvent.VK_R)
        {
            iniciarNovaPartida();
        }

        // Se a partida está rolando, repassa os controles para os lutadores
        else if (estadoAtual == EstadoJogo.JOGANDO)
        {
            jogador1.processarTeclaPressionada(keyCode);
            jogador2.processarTeclaPressionada(keyCode);
        }
    }

    public void processarTeclaSolta(int keyCode) {
        if (estadoAtual == EstadoJogo.JOGANDO)
        {
            jogador1.processarTeclaSolta(keyCode);
            jogador2.processarTeclaSolta(keyCode);
        }
    }

    public StickFighter getJogador1() {
        return jogador1;
    }

    public StickFighter getJogador2() {
        return jogador2;
    }

    public EstadoJogo getEstadoAtual() {
        return estadoAtual;
    }
}

package br.univates.sticktreta.view;

import br.univates.sticktreta.interfaces.Tela;
import br.univates.sticktreta.model.StickFighter;
import br.univates.sticktreta.util.CarregadorSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Tela responsável por desenhar a partida em si: fundo, lutadores e HUD.
 */

public class TelaJogo implements Tela {
    private final StickFighter jogador1;
    private final StickFighter jogador2;
    private final HUD hud;
    private final BufferedImage imagemFundo;

    public TelaJogo(StickFighter jogador1, StickFighter jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.imagemFundo = CarregadorSprites.carregarImagem("/assets/sprites/backgrounds/Cenario.png");;
        this.hud = new HUD();

    }

    @Override
    public void desenhar(Graphics g, int largura, int altura) {
        g.drawImage(imagemFundo, 0, 0, largura, altura, null);
        jogador1.desenhar(g);
        jogador2.desenhar(g);

        hud.desenhar(g, jogador1, jogador2);
    }

    @Override
    public void atualizar() {
        // sem lógica ainda
    }
}
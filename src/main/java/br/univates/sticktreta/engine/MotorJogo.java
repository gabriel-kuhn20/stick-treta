package br.univates.sticktreta.engine;

import br.univates.sticktreta.config.Configuracoes;
import br.univates.sticktreta.model.StickFighter;
import br.univates.sticktreta.util.CarregadorSprites;
import br.univates.sticktreta.view.HUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


public class MotorJogo extends JPanel implements Runnable, KeyListener {
    private Thread gameThread; // Linha de execução independente para o jogo não travar a janela
    private boolean rodando;
    private StickFighter jogador1;
    private StickFighter jogador2;

    private HUD hud;
    private BufferedImage imagemFundo;

    public MotorJogo() {
        rodando = false;
        hud = new HUD();
        setPreferredSize(new Dimension(Configuracoes.LARGURA_TELA, Configuracoes.ALTURA_TELA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        imagemFundo = CarregadorSprites.carregarImagem("/assets/sprites/backgrounds/Cenario.png");

        jogador1 = new StickFighter(300, 650,
                Color.BLUE,
                KeyEvent.VK_SPACE,
                KeyEvent.VK_W,
                KeyEvent.VK_D,
                KeyEvent.VK_A,
                true
        );

        jogador2 = new StickFighter(1100, 650,
                Color.RED,
                KeyEvent.VK_ENTER,
                KeyEvent.VK_UP,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_LEFT,
                false
        );
    }

    public void iniciarJogo(){
        rodando = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (rodando) {
            atualizar();
            repaint();

            if (jogador1.getVida() <= 0 || jogador2.getVida() <= 0) {
                rodando = false;
            }

            try
            {
                Thread.sleep(1000/Configuracoes.FPS);
            }
            catch (InterruptedException e)
            {
                System.out.println("Erro no loop do jogo: " + e.getMessage());
            }
        }
    }

    private void atualizar(){
        if(jogador1 != null) jogador1.atualizar();
        if(jogador2 != null) jogador2.atualizar();

        checarColisoes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(imagemFundo != null)
        {
            g.drawImage(imagemFundo, 0, 0, this);
        }
        else
        {
            g.setColor(Color.GRAY);
            g.fillRect(0, 530, Configuracoes.LARGURA_TELA, 70);
        }

        jogador1.desenhar(g);
        jogador2.desenhar(g);

        hud.desenhar(g, jogador1, jogador2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        jogador1.processarTeclaPressionada(keyCode);
        jogador2.processarTeclaPressionada(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        jogador1.processarTeclaSolta(keyCode);
        jogador2.processarTeclaSolta(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // deixar em branco, sem necessidade para o jogo
    }

    private void checarColisoes() {
        Rectangle ataqueJ1 = jogador1.getHitBoxAtaque();
        Rectangle corpoJ2 = jogador2.getHitBoxCorpo();

        // intersects() é o comando que detecta colisoes
        if(ataqueJ1 != null && ataqueJ1.intersects(corpoJ2) && !jogador1.isJaDeuDano()) {
            jogador2.receberDano();

            jogador1.setJaDeuDano(true);

            System.out.println("Jogador 1 acertou! Vida do J2: " + jogador2.getVida());
        }

        Rectangle ataqueJ2 = jogador2.getHitBoxAtaque();
        Rectangle corpoJ1 = jogador1.getHitBoxCorpo();

        if(ataqueJ2 != null && ataqueJ2.intersects(corpoJ1) &&  !jogador2.isJaDeuDano()) {
            jogador1.receberDano();

            jogador2.setJaDeuDano(true);

            System.out.println("Jogador 2 acertou! Vida do J1: " + jogador1.getVida());
        }
    }
}

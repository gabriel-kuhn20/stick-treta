package br.univates.sticktreta.engine;

import br.univates.sticktreta.config.Configuracoes;
import br.univates.sticktreta.model.StickFighter;
import br.univates.sticktreta.view.GerenciadorTelas;
import br.univates.sticktreta.view.PainelJogo;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MotorJogo implements Runnable, KeyListener {
    private Thread gameThread; // Linha de execução independente
    private boolean rodando;

    private final PainelJogo painelJogo;
    private final GerenciadorTelas gerenciadorTelas;

    public MotorJogo() {
        rodando = false;
        painelJogo = new PainelJogo();
        painelJogo.addKeyListener(this);

        // O GerenciadorTelas cria a partida imediatamente ao abrir
        this.gerenciadorTelas = new GerenciadorTelas(painelJogo);
    }

    public PainelJogo getPainelJogo() {
        return painelJogo;
    }

    public void iniciarJogo() {
        rodando = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (rodando) {
            atualizar();
            painelJogo.repaint();

            try
            {
                Thread.sleep(1000 / Configuracoes.FPS);
            }
            catch (InterruptedException e)
            {
                System.out.println("Erro no loop do jogo: " + e.getMessage());
            }
        }
    }

    private void atualizar() {
        // Atualiza a movimentação e estado dos lutadores via GerenciadorTelas
        gerenciadorTelas.atualizar();

        // Checa as colisões de ataque se a luta estiver em andamento
        if (gerenciadorTelas.getEstadoAtual() == GerenciadorTelas.EstadoJogo.JOGANDO)
        {
            checarColisoes();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gerenciadorTelas.processarTeclaPressionada(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gerenciadorTelas.processarTeclaSolta(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Deixar em branco, sem necessidade para o jogo
    }

    private void checarColisoes() {
        StickFighter jogador1 = gerenciadorTelas.getJogador1();
        StickFighter jogador2 = gerenciadorTelas.getJogador2();

        if (jogador1 == null || jogador2 == null) return;

        // Colisão: Ataque do J1 no Corpo do J2
        Rectangle ataqueJ1 = jogador1.getHitBoxAtaque();
        Rectangle corpoJ2 = jogador2.getHitBoxCorpo();

        if (ataqueJ1 != null && ataqueJ1.intersects(corpoJ2) && !jogador1.isJaDeuDano()) {
            jogador2.receberDano();
            jogador1.setJaDeuDano(true);
            System.out.println("Jogador 1 acertou! Vida do J2: " + jogador2.getVida());
        }

        // Colisão: Ataque do J2 no Corpo do J1
        Rectangle ataqueJ2 = jogador2.getHitBoxAtaque();
        Rectangle corpoJ1 = jogador1.getHitBoxCorpo();

        if (ataqueJ2 != null && ataqueJ2.intersects(corpoJ1) && !jogador2.isJaDeuDano()) {
            jogador1.receberDano();
            jogador2.setJaDeuDano(true);
            System.out.println("Jogador 2 acertou! Vida do J1: " + jogador1.getVida());
        }
    }
}
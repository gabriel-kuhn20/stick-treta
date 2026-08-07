package br.univates.sticktreta.view;

import br.univates.sticktreta.model.StickFighter;

import java.awt.*;

public class HUD {
    public void desenhar (Graphics g, StickFighter jogador1, StickFighter jogador2) {

        // Obtém o centro REAL da janela desenhada (evita erros com bordas do Windows)
        int larguraTela = g.getClipBounds().width;
        int alturaTela =  g.getClipBounds().height;

        int centroX = larguraTela / 2;
        int larguraBarra = 500;
        int alturaBarra = 32;
        int posY = 50;
        int espacoCentro = 25;

        int vidaJ1 = jogador1.getVida();
        int vidaJ2 = jogador2.getVida();

        int larguraVidaJ1 = (vidaJ1 * 5); // 100 HP = 500px
        int larguraVidaJ2 = (vidaJ2 * 5);

        Font fonteHUD = new Font("Arial", Font.BOLD, 20);
        g.setFont(fonteHUD);
        FontMetrics metrics = g.getFontMetrics(fonteHUD);


        // BARRA JOGADOR 1 (AZUL - Esquerda)
        int posX1_Fundo = centroX - espacoCentro - larguraBarra; // Início do fundo (ex: 435px)

        // Painel escuro de contraste
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(posX1_Fundo - 10, posY - 32, larguraBarra + 20, alturaBarra + 42);

        // Texto do Jogador 1 (Alinhado à esquerda da barra)
        g.setColor(new Color(100, 200, 255));
        g.drawString("Jogador 1 (Azul): " + vidaJ1, posX1_Fundo, posY - 8);

        // Fundo da barra (Vida perdida - Azul escuro)
        g.setColor(new Color(20, 30, 60));
        g.fillRect(posX1_Fundo, posY, larguraBarra, alturaBarra);

        // Barra de vida ativa (Dano reduz do centro para fora)
        g.setColor(new Color(30, 144, 255));
        g.fillRect(posX1_Fundo, posY, larguraVidaJ1, alturaBarra);

        // Borda
        g.setColor(new Color(100, 200, 255));
        g.drawRect(posX1_Fundo, posY, larguraBarra, alturaBarra);


        // BARRA JOGADOR 2 (VERMELHO - Direita)
        int posX2_Fundo = centroX + espacoCentro; // Início do fundo (ex: 985px)

        // Painel escuro de contraste
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(posX2_Fundo - 10, posY - 32, larguraBarra + 20, alturaBarra + 42);

        // Texto do Jogador 2 (Alinhado à DIREITA da barra para simetria total)
        String textoJ2 = "Jogador 2 (Vermelho): " + vidaJ2;
        int larguraTextoJ2 = metrics.stringWidth(textoJ2);
        int posX2_Texto = (posX2_Fundo + larguraBarra) - larguraTextoJ2;

        g.setColor(new Color(255, 100, 100));
        g.drawString(textoJ2, posX2_Texto, posY - 8);

        // Fundo da barra (Vida perdida - Vermelho escuro)
        g.setColor(new Color(60, 20, 20));
        g.fillRect(posX2_Fundo, posY, larguraBarra, alturaBarra);

        // Barra de vida ativa (Dano reduz do centro para fora)
        g.setColor(new Color(255, 50, 50));
        int posX2_Vida = posX2_Fundo + (larguraBarra - larguraVidaJ2);
        g.fillRect(posX2_Vida, posY, larguraVidaJ2, alturaBarra);

        // Borda
        g.setColor(new Color(255, 100, 100));
        g.drawRect(posX2_Fundo, posY, larguraBarra, alturaBarra);


        // TELA GAME OVER
        if (jogador1.getVida() <= 0 || jogador2.getVida() <= 0) {
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, larguraTela, alturaTela);

            Font fonteGameOver = new Font("Arial", Font.BOLD, 64);
            g.setFont(fonteGameOver);
            FontMetrics metricsGameOver = g.getFontMetrics(fonteGameOver);

            String mensagem = "";

            if (jogador1.getVida() <= 0 && jogador2.getVida() <= 0) {
                mensagem = "EMPATE!";
                g.setColor(Color.YELLOW);
            } else if (jogador1.getVida() <= 0) {
                mensagem = "JOGADOR 2 VENCEU!";
                g.setColor(new Color(255, 80, 80));
            } else {
                mensagem = "JOGADOR 1 VENCEU!";
                g.setColor(new Color(80, 180, 255));
            }

            int x = (larguraTela - metricsGameOver.stringWidth(mensagem)) / 2;
            int y = alturaTela / 2;

            g.drawString(mensagem, x, y);

            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.setColor(Color.WHITE);
            String msgRestart = "Pressione [R] para reiniciar";
            int xRestart = (larguraTela - g.getFontMetrics().stringWidth(msgRestart)) / 2;
            g.drawString(msgRestart, xRestart, y + 70);

        }
        desenharIndicadores(g, jogador1, jogador2);
    }

    public void desenharIndicadores(Graphics g, StickFighter j1, StickFighter j2) {
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // P1 (Azul)
        g.setColor(Color.BLACK);
        g.drawString("P1", j1.getX() + 12, j1.getY() - 8);  // Sombra
        g.setColor(Color.CYAN);
        g.drawString("P1", j1.getX() + 10, j1.getY() - 10); // Texto

        // P2 (Vermelho)
        g.setColor(Color.BLACK);
        g.drawString("P2", j2.getX() + 12, j2.getY() - 8);  // Sombra
        g.setColor(Color.RED);
        g.drawString("P2", j2.getX() + 10, j2.getY() - 10); // Texto
    }
}
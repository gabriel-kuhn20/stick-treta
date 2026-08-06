package br.univates.sticktreta.model;

import br.univates.sticktreta.config.Configuracoes;
import br.univates.sticktreta.interfaces.Controlavel;
import br.univates.sticktreta.interfaces.Renderizavel;
import br.univates.sticktreta.util.CarregadorSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StickFighter extends Lutador implements Controlavel, Renderizavel {
    private int teclaEsq, teclaDir, teclaPulo, teclaAtaque;
    private boolean indoEsquerda, indoDireita, isAtacando, jaDeuDano;
    private Color corLutador;
    private boolean viradoParaDireita; // Define o lado que o boneco está virado
    private int tempoAtaque;
    private BufferedImage [] animParado;
    private BufferedImage [] animAndando;
    private BufferedImage [] animaAtacando;
    private int frameAtual;
    private int contadorTempo;
    private final int VELOCIDADE_ANIMACAO = Configuracoes.VELOCIDADE_ANIMACAO;

    public StickFighter(int x, int y, Color corLutador, int teclaAtaque,
                        int teclaPulo, int teclaDir, int teclaEsq, boolean viradoParaDireita) {

        super(x, y, Configuracoes.LARGURA_CORPO, Configuracoes.ALTURA_CORPO);
        this.viradoParaDireita = viradoParaDireita;
        tempoAtaque = 0;
        this.corLutador = corLutador;
        this.teclaAtaque = teclaAtaque;
        this.teclaPulo = teclaPulo;
        this.teclaDir = teclaDir;
        this.teclaEsq = teclaEsq;
        this.isAtacando = false;
        contadorTempo = 0;
        frameAtual = 0;

        animParado = carregarTiraAnimacao("/assets/sprites/StickmanPack/Idle/thickIdleSheet.png", 6);
        animAndando = carregarTiraAnimacao("/assets/sprites/StickmanPack/Run/thickRunSheet.png", 9);
        animaAtacando = carregarTiraAnimacao("/assets/sprites/StickmanPack/Punch/thickPunchSheet.png", 10);


    }

    private BufferedImage[] carregarTiraAnimacao(String caminho, int totalFrames) {
        BufferedImage sheet = CarregadorSprites.carregarImagem(caminho);
        if (sheet == null) return null;

        BufferedImage[] frames = new BufferedImage[totalFrames];
        int larguraFrame = sheet.getWidth() / totalFrames;
        int alturaFrame = sheet.getHeight();

        for (int i = 0; i < totalFrames; i++)
        {
            frames[i] = sheet.getSubimage(i * larguraFrame, 0, larguraFrame, alturaFrame);
        }

        return frames;
    }


    @Override
    public void atualizar() {
        int mov = Configuracoes.VELOCIDADE_MOVIMENTO_X;

        if(indoEsquerda)
        {
            x -= mov;
        }

        if(indoDireita)
        {
            x += mov;
        }

        aplicarGravidade();

        // Desliga o soco após 10ms
        if (isAtacando)
        {
            tempoAtaque++;
            if(tempoAtaque >= 20) // Aumentei um pouco para dar tempo de ver a animação do soco
            {
                isAtacando = false;
                tempoAtaque = 0;
            }
        }


        // Evita que animação rode rapido demais
        contadorTempo++;

        if(contadorTempo >= VELOCIDADE_ANIMACAO)
        {
            contadorTempo = 0;
            frameAtual++;
        }
    }

    @Override
    public void processarTeclaPressionada(int keyCode) {
        if(keyCode == teclaEsq)
        {
            indoEsquerda = true;
            viradoParaDireita = false;
        }
        else if(keyCode == teclaDir)
        {
            indoDireita = true;
            viradoParaDireita = true;
        }
        else if(keyCode == teclaPulo && noChao)
        {
            velocidadeY = Configuracoes.VELOCIDADE_MOVIMENTO_Y;
            noChao = false;
        }
        else if(keyCode == teclaAtaque && !isAtacando)
        {
            isAtacando = true;
            jaDeuDano = false;

        }
    }

    @Override
    public void processarTeclaSolta(int keyCode) {
        if(keyCode == teclaEsq)
        {
            indoEsquerda = false;
        }
        else if(keyCode == teclaDir)
        {
            indoDireita = false;
        }
    }

    @Override
    public void desenhar(Graphics g) {
        BufferedImage imgAtual = null;

        if(animParado != null && animAndando != null && animaAtacando != null) {

            // Mapeia os 20 ticks do ataque para os 10 frames do soco
            if (isAtacando)
            {
                int frameSoco = (tempoAtaque / 2) % 10;
                imgAtual = animaAtacando[frameSoco];
            }
            else if (indoEsquerda || indoDireita)
            {
                imgAtual = animAndando[frameAtual % 9];
            }
            else
            {
                imgAtual = animParado[frameAtual % 6];
            }
        }

        if(imgAtual != null)
        {
            // A imagem original do sprite é desenhada meio para cima, então ajustamos o Y um pouco (-20)
            int ajusteY = y - 20;
            int ajusteX = x - 20;

            int tamanho = Configuracoes.TAMANHO_LUTADOR;

            if(viradoParaDireita)
            {
                g.drawImage(imgAtual, ajusteX, ajusteY, tamanho, tamanho, null);
            }
            else
            {
                // Inverte a imagem horizontalmente se virar para a esquerda
                g.drawImage(imgAtual, ajusteX + tamanho, ajusteY, -tamanho, tamanho, null);
            }
        }
        else
        {
            // Desenha o retângulo se a imagem der erro
            g.setColor(corLutador);
            g.fillRect(x, y, largura, altura);
        }


//        g.setColor(corLutador);
//        g.fillRect(x, y, largura, altura); // Desenha o corpo (hitbox)
//
//        // Desenha o soco (se estiver atacando)
//        if(isAtacando)
//        {
//            Rectangle ataque = getHitBoxAtaque();
//            if(ataque != null)
//            {
//                g.setColor(Color.YELLOW);
//                g.fillRect(ataque.x, ataque.y, ataque.width, ataque.height);
//            }
//        }
    }

    public Rectangle getHitBoxAtaque() {

        // Trava soco fantasma
        if(!isAtacando)
        {
            return null;
        }

        Rectangle hitbox;

        int larguraAtaque = Configuracoes.LARGURA_ATAQUE;
        int alturaAtaque = Configuracoes.ALTURA_ATAQUE;
        int ataqueY = y + Configuracoes.ATAQUE_Y;

        int ataqueX;
        if(viradoParaDireita)
        {
            ataqueX = x + largura; // Soco sai pela direita do corpo
        }
        else
        {
            ataqueX = x - larguraAtaque; // Soco sai pela esquerda do corpo
        }

        hitbox = new Rectangle(ataqueX, ataqueY, larguraAtaque, alturaAtaque);

        return hitbox;
    }

    public boolean isJaDeuDano() {
        return jaDeuDano;
    }

    public void setJaDeuDano(boolean jaDeuDano) {
        this.jaDeuDano = jaDeuDano;
    }
}

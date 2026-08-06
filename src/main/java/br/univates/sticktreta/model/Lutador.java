package br.univates.sticktreta.model;

import br.univates.sticktreta.config.Configuracoes;

import java.awt.*;

public abstract class Lutador {
    protected int x, y;
    protected int largura, altura; // Dimensões do corpo e hitbox
    protected int vida;
    protected int velocidadeY; // pulo/queda
    protected boolean noChao;
    protected static int posicaoChao;

    public Lutador(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.vida = Configuracoes.VIDA_INICIAL;
        this.noChao = false;
        posicaoChao = Configuracoes.POSICAO_CHAO;

    }

    public abstract void atualizar();

    public void aplicarGravidade() {
        if(!noChao) {
            // Aumenta a velocidade de queda gradualmente
            velocidadeY += 1;

            // Atualiza a posição no eixo Y
            y += velocidadeY;

            if(y >= posicaoChao) {
                y = posicaoChao;
                noChao = true;
                velocidadeY = 0;
            }
        }

    }

    public void receberDano() {
       vida -= Configuracoes.DANO;
       if(vida <= 0) {
           vida = 0;
       }
    }

    public int getVida() {
        return vida;
    }

    public Rectangle getHitBoxCorpo() {
        return new Rectangle(x, y, largura, altura);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

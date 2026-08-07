package br.univates.sticktreta.interfaces;

import java.awt.*;

public interface Tela {

    void desenhar(Graphics g, int largura, int altura); // Recebe o pincel gráfico e o tamanho do painel

    void atualizar(); // Atualiza estado visual da tela
}

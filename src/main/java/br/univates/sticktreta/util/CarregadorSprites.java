package br.univates.sticktreta.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class CarregadorSprites {

    public static BufferedImage carregarImagem(String caminho) {
        try {
            InputStream stream = CarregadorSprites.class.getResourceAsStream(caminho);
            if (stream == null)
            {
                System.out.println("Imagem não encontrada: " + caminho);
                return null;
            }
            return ImageIO.read(stream);
        } catch (Exception e)
        {
            System.out.println("Erro ao ler a imagem (" + caminho + "): " + e.getMessage());
            return null;
        }
    }
}
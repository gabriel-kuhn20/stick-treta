package br.univates.sticktreta.util;

import javax.sound.sampled.*;

public class MusicaFundo {

    private static Clip clip;

    // Inicia a música em loop infinito
    public static void tocar(String caminho) {
        try {

            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(MusicaFundo.class.getResource(caminho)));

            // faz a música repetir infinitamente
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.out.println("Erro ao tocar música: " + e.getMessage());
        }
    }


    public static void parar() {
        if (clip != null) {
            clip.stop();
        }
    }
}
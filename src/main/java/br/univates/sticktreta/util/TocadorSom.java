package br.univates.sticktreta.util;

import javax.sound.sampled.*;

public class TocadorSom {

    private static Clip clipPassos;

    // Toca efeitos rápidos (soco, pulo, etc)
    public static void tocar(String caminho) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(TocadorSom.class.getResource(caminho)));
            clip.start();
        } catch (Exception e) {}
    }

    // Inicia o som de passos em loop
    public static void iniciarPassos(String caminho) {
        try {
            if (clipPassos == null || !clipPassos.isRunning()) {
                clipPassos = AudioSystem.getClip();
                clipPassos.open(AudioSystem.getAudioInputStream(TocadorSom.class.getResource(caminho)));
                clipPassos.loop(Clip.LOOP_CONTINUOUSLY); // Repete enquanto anda
            }
        } catch (Exception e) {}
    }

    // Para o som de passos quando solta a tecla
    public static void pararPassos() {
        if (clipPassos != null && clipPassos.isRunning()) {
            clipPassos.stop();
            clipPassos.close();
        }
    }
}
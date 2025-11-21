package com.example.udpbroadcast;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Utils {
    /**
     * Play a WAV file (blocking until finished).
     * Shows an error dialog on exceptions.
     */
    public static void playWavFile(File wavFile) {
        if (wavFile == null || !wavFile.exists()) {
            JOptionPane.showMessageDialog(null, "File WAV không tồn tại: " + (wavFile == null ? "null" : wavFile.getAbsolutePath()));
            return;
        }
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(wavFile)) {
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioIn);
            clip.start();
            // Wait until clip finished
            while (clip.isRunning()) {
                try { Thread.sleep(100); } catch (InterruptedException ie) { break; }
            }
            clip.stop();
            clip.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi phát WAV: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi không xác định khi phát WAV: " + ex.getMessage());
        }
    }
}

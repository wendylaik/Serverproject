package Interfaz;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MusicPlayer extends JFrame {

    private AdvancedPlayer player;
    private Thread playerThread;

    public MusicPlayer(String musicPath) {
        setTitle("Music Player");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopMusic());
        add(stopButton, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopMusic();
            }
        });

        centerWindow(); // Centrar la ventana
        setVisible(true);

        playMusic(musicPath);
    }

    private void playMusic(String musicPath) {
        try {
            InputStream inputStream = new FileInputStream(musicPath);
            player = new AdvancedPlayer(inputStream);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    System.out.println("Playback finished");
                    dispose();
                }
            });

            playerThread = new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            });
            playerThread.start();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (playerThread != null) {
            playerThread.interrupt();
            playerThread = null;
        }
    }

    private void centerWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }
}

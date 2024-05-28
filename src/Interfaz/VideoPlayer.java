package Interfaz;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoPlayer extends JFrame {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public VideoPlayer(String videoPath) {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        setTitle("Video Player");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Use DISPOSE_ON_CLOSE instead of EXIT_ON_CLOSE
        setLayout(new BorderLayout());
        add(mediaPlayerComponent, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });

        centerWindow(); // Centrar la ventana
        setVisible(true); // Make sure the window is visible before playing the video

        // Play the video after the window is made visible
        SwingUtilities.invokeLater(() -> {
            mediaPlayerComponent.mediaPlayer().media().play(videoPath);
        });
    }

    private void centerWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }
}

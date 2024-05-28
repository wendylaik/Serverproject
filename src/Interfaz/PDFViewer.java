package Interfaz;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PDFViewer {

    private PDDocument document;
    private int currentPage = 0;
    private PDFRenderer pdfRenderer;
    private JPanel panel;

    public PDFViewer(String filePath) {
        try {
            document = PDDocument.load(new File(filePath));
            pdfRenderer = new PDFRenderer(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayPDF() {
        if (document == null) {
            JOptionPane.showMessageDialog(null, "Error al cargar el documento PDF.");
            return;
        }

        JFrame frame = new JFrame("PDF Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    pdfRenderer.renderPageToGraphics(currentPage, (Graphics2D) g);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        frame.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    panel.repaint();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < document.getNumberOfPages() - 1) {
                    currentPage++;
                    panel.repaint();
                }
            }
        });

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        centerWindow(frame); // Centrar la ventana
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}

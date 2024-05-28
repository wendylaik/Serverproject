package server;

import Interfaz.Logiin;
import java.awt.Desktop;    
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import server.Flujocliente;

public final class InterServer extends javax.swing.JFrame {

    private List<String> roles;

    public InterServer() {
        initComponents();
        loadAllContent();
        centerWindow();
        
    }

    private void centerWindow() {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);
}

    
   private void loadAllContent() {
        cargarContenido("Documents", DocList);
        cargarContenido("Videos", VideoList);
        cargarContenido("Music", MusicList);
    }

private void cargarContenido(String fileType, javax.swing.JList<String> list) {
        try (Socket contentSocket = new Socket("25.65.94.55", 12345); // IP y Puerto del servidor
             PrintWriter out = new PrintWriter(contentSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(contentSocket.getInputStream()))) {

            out.println("LOAD," + fileType);
            DefaultListModel<String> model = new DefaultListModel<>();
            String response;
            while ((response = in.readLine()) != null && !response.isEmpty()) {
                if ("DIRECTORY NOT FOUND".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Directorio no encontrado en el servidor");
                    return;
                }
                model.addElement(response);
            }
            list.setModel(model);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el contenido: " + e.getMessage());
        }
    }

private void cargarArchivo(String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (Socket uploadSocket = new Socket("25.65.94.55", 12345); // IP y Puerto del servidor
                 PrintWriter out = new PrintWriter(uploadSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(uploadSocket.getInputStream()));
                 FileInputStream fis = new FileInputStream(selectedFile)) {

                out.println("UPLOAD," + fileType + "," + selectedFile.getName());
                String response = in.readLine();
                if ("200 OK".equals(response)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        uploadSocket.getOutputStream().write(buffer, 0, bytesRead);
                    }
                    uploadSocket.getOutputStream().flush();
                    JOptionPane.showMessageDialog(this, "Archivo cargado exitosamente");

                    // Cerrar el socket después de la operación de carga
                    uploadSocket.close();

                    // Recargar la lista después de una breve pausa
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Thread.sleep(500); // Pausa breve para permitir que el servidor procese la solicitud
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        cargarContenido(fileType, getJList(fileType)); // Usar un nuevo socket para cargar el contenido
                    });

                } else {
                    JOptionPane.showMessageDialog(this, "Error al cargar el archivo");
                }

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo: " + e.getMessage());
            }
        }
    }

    private javax.swing.JList<String> getJList(String fileType) {
        switch (fileType) {
            case "Documents":
                return DocList;
            case "Videos":
                return VideoList;
            case "Music":
                return MusicList;
            default:
                return null;
        }
    }

 private void abrirArchivo(String fileName, String fileType) {
        JOptionPane.showMessageDialog(this, "Abriendo archivo...");
        try (Socket downloadSocket = new Socket("25.65.94.55", 12345); // IP y Puerto del servidor
             PrintWriter out = new PrintWriter(downloadSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(downloadSocket.getInputStream()))) {

            out.println("DOWNLOAD," + fileType + "," + fileName);
            System.out.println("Solicitud de descarga enviada: DOWNLOAD," + fileType + "," + fileName);

            String response = in.readLine();
            System.out.println("Respuesta del servidor: " + response);
            if ("200 OK".equals(response)) {
                File file = new File(fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = downloadSocket.getInputStream().read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        System.out.println("Descargando: " + bytesRead + " bytes");
                    }
                    fos.flush(); // Ensure all data is written to the file
                }
                System.out.println("Archivo descargado: " + file.getAbsolutePath());
                if (file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        System.out.println("Intentando abrir el archivo: " + file.getAbsolutePath());
                        Desktop.getDesktop().open(file);
                    } else {
                        System.out.println("Desktop no soportado. No se puede abrir el archivo automáticamente.");
                    }
                } else {
                    System.out.println("El archivo no existe después de la descarga: " + file.getAbsolutePath());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al descargar el archivo");
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage());
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        VideoList = new javax.swing.JList<>();
        botonVideos = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        MusicList = new javax.swing.JList<>();
        botonMusica = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        DocList = new javax.swing.JList<>();
        botonDocumentos = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Update = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 255));

        jPanel4.setBackground(new java.awt.Color(2, 40, 55));

        jPanel1.setBackground(new java.awt.Color(4, 53, 72));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Videos");

        VideoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VideoListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(VideoList);

        botonVideos.setBackground(new java.awt.Color(2, 40, 55));
        botonVideos.setForeground(new java.awt.Color(255, 255, 255));
        botonVideos.setText("Cargar Videos");
        botonVideos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVideosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(botonVideos)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addGap(45, 45, 45)
                .addComponent(botonVideos)
                .addGap(25, 25, 25))
        );

        jPanel2.setBackground(new java.awt.Color(31, 60, 78));
        jPanel2.setPreferredSize(new java.awt.Dimension(240, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Música");

        MusicList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MusicListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(MusicList);

        botonMusica.setBackground(new java.awt.Color(2, 40, 55));
        botonMusica.setForeground(new java.awt.Color(255, 255, 255));
        botonMusica.setText("Cargar Música");
        botonMusica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMusicaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(botonMusica)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(botonMusica)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(4, 53, 72));

        DocList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DocListMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(DocList);

        botonDocumentos.setBackground(new java.awt.Color(2, 40, 55));
        botonDocumentos.setForeground(new java.awt.Color(255, 255, 255));
        botonDocumentos.setText("Cargar Docs");
        botonDocumentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDocumentosActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(4, 53, 72));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Documentos");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(botonDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(botonDocumentos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Centro de Gestión de Archivos");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Update.setBackground(new java.awt.Color(2, 40, 55));
        Update.setForeground(new java.awt.Color(255, 255, 255));
        Update.setText("Actualizar");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(232, 232, 232))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(Update)
                .addGap(78, 78, 78)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 844, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVideosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVideosActionPerformed
        cargarArchivo("Videos");
    }//GEN-LAST:event_botonVideosActionPerformed

    private void botonMusicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMusicaActionPerformed
        cargarArchivo("Music");
        // TODO add your handling code here:
    }//GEN-LAST:event_botonMusicaActionPerformed

    private void MusicListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MusicListMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            abrirArchivo(MusicList.getSelectedValue(), "Music");
        }
    }//GEN-LAST:event_MusicListMouseClicked

    private void VideoListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VideoListMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            abrirArchivo(VideoList.getSelectedValue(), "Videos");
        }
    }//GEN-LAST:event_VideoListMouseClicked

 private String getSelectedFileName() {
    if (DocList.getSelectedValue() != null) {
        return DocList.getSelectedValue();
    } else if (VideoList.getSelectedValue() != null) {
        return VideoList.getSelectedValue();
    } else if (MusicList.getSelectedValue() != null) {
        return MusicList.getSelectedValue();
    }
    return null;
}

private String getSelectedFileType() {
    if (DocList.getSelectedValue() != null) {
        return "Documents";
    } else if (VideoList.getSelectedValue() != null) {
        return "Videos";
    } else if (MusicList.getSelectedValue() != null) {
        return "Music";
    }
    return null;
}

    
    private void DocListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DocListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_DocListMouseClicked

    private void botonDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDocumentosActionPerformed
 // TODO add your handling code here:
        cargarArchivo("Documents");        // TODO add your handling code here:
    }//GEN-LAST:event_botonDocumentosActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
  // Actualizar el contenido de las listas basadas en los roles del usuario
    loadAllContent();
    JOptionPane.showMessageDialog(this, "Contenido actualizado.");        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> DocList;
    public javax.swing.JList<String> MusicList;
    private javax.swing.JButton Update;
    public javax.swing.JList<String> VideoList;
    private javax.swing.JButton botonDocumentos;
    private javax.swing.JButton botonMusica;
    private javax.swing.JButton botonVideos;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}

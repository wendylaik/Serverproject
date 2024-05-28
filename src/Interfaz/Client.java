package Interfaz;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import server.Flujocliente;

public final class Client extends javax.swing.JFrame {

    private final String username;
    private final Flujocliente clientCommunication;
    private final Socket socket;
    private List<String> roles;

    public Client(String username, Flujocliente clientCommunication, List<String> roles) {
        this.username = username;
        this.clientCommunication = clientCommunication;
        this.roles = roles;
        socket = Logiin.sharedSocket;
        System.out.println("Socket compartido desde el cliente: " + socket);
        if (socket == null) {
            System.err.println("El socket es nulo");
            return;
        }
        initComponents();
        roleAccess(roles);
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
        if (roles.contains("Documents")) {
            cargarContenido("Documents", DocList);
        }
        if (roles.contains("Videos")) {
            cargarContenido("Videos", VideoList);
        }
        if (roles.contains("Music")) {
            cargarContenido("Music", MusicList);
        }
    }

    private void roleAccess(List<String> lista) {
        boolean canAccessDocuments = lista.contains("Documents");
        boolean canAccessVideos = lista.contains("Videos");
        boolean canAccessMusic = lista.contains("Music");

        if (!canAccessDocuments) {
            botonDocumentos.setEnabled(false);
        }
        if (!canAccessVideos) {
            Reprovideo.setEnabled(false);
        }
        if (!canAccessMusic) {
            Repromusica.setEnabled(false);
        }
    }

    private void cargarContenido(String fileType, javax.swing.JList<String> list) {
    try {
        Socket contentSocket = null;
        boolean connected = false;

        // Intentar conectarse a la dirección local primero
        try {
            contentSocket = new Socket("25.65.94.55", Logiin.SERVER_PORT);
            connected = true;
            System.out.println("Conectado localmente");
        } catch (IOException e) {
            System.out.println("No se pudo conectar localmente: " + e.getMessage());
        }

        // Si no se pudo conectar localmente, intentar la dirección de Hamachi
        if (!connected) {
            try {
                contentSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT);
                System.out.println("Conectado externamente a través de Hamachi");
            } catch (IOException e) {
                throw new IOException("No se pudo conectar externamente: " + e.getMessage());
            }
        }

        // Continuar con el flujo normal
        try (PrintWriter out = new PrintWriter(contentSocket.getOutputStream(), true); 
             BufferedReader in = new BufferedReader(new InputStreamReader(contentSocket.getInputStream()))) {

            out.println("LOAD," + fileType);
            DefaultListModel<String> model = new DefaultListModel<>();
            String response;

            System.out.println("Esperando respuesta del servidor para el tipo de archivo: " + fileType);

            while ((response = in.readLine()) != null && !response.isEmpty()) {
                System.out.println("Respuesta del servidor: " + response);
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
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage());
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
        try (Socket downloadSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT); PrintWriter out = new PrintWriter(downloadSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(downloadSocket.getInputStream()))) {

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
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        VideoList = new javax.swing.JList<>();
        Reprovideo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        MusicList = new javax.swing.JList<>();
        Repromusica = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DocList = new javax.swing.JList<>();
        botonDocumentos = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        backTwo = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 255));

        jPanel4.setBackground(new java.awt.Color(2, 40, 55));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("¡Nos encanta que disfrutes de nuestra colección de videos, música y documentos!");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

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

        Reprovideo.setBackground(new java.awt.Color(2, 40, 55));
        Reprovideo.setForeground(new java.awt.Color(255, 255, 255));
        Reprovideo.setText("Reproducir videos");
        Reprovideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReprovideoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(Reprovideo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Reprovideo)
                .addGap(20, 20, 20))
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

        Repromusica.setBackground(new java.awt.Color(2, 40, 55));
        Repromusica.setForeground(new java.awt.Color(255, 255, 255));
        Repromusica.setText("Reproducir Musica");
        Repromusica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RepromusicaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(Repromusica)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Repromusica)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(4, 53, 72));

        jLabel4.setBackground(new java.awt.Color(4, 53, 72));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Documentos");

        DocList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DocListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DocList);

        botonDocumentos.setBackground(new java.awt.Color(2, 40, 55));
        botonDocumentos.setForeground(new java.awt.Color(255, 255, 255));
        botonDocumentos.setText("Abrir Documento PDF");
        botonDocumentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDocumentosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonDocumentos)
                .addGap(99, 99, 99))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonDocumentos)
                .addContainerGap())
        );

        botonGuardar.setBackground(new java.awt.Color(31, 60, 78));
        botonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        botonGuardar.setText("Descargar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        backTwo.setBackground(new java.awt.Color(31, 60, 78));
        backTwo.setForeground(new java.awt.Color(255, 255, 255));
        backTwo.setText("Volver");
        backTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backTwoActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(31, 60, 78));
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Actualizar");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(backTwo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(botonGuardar))
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backTwo)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ReprovideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReprovideoActionPerformed
        String selectedVideo = VideoList.getSelectedValue();
        if (selectedVideo != null) {
            String filePath = "server_content/Videos/" + selectedVideo;
            SwingUtilities.invokeLater(() -> {
                VideoPlayer player = new VideoPlayer(filePath);
                player.setVisible(true); // Ensure visibility
            });
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un video.");
        }
    }//GEN-LAST:event_ReprovideoActionPerformed

    private void RepromusicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RepromusicaActionPerformed
        String selectedMusic = MusicList.getSelectedValue();
        if (selectedMusic != null) {
            String filePath = "server_content/Music/" + selectedMusic;
            SwingUtilities.invokeLater(() -> {
                MusicPlayer player = new MusicPlayer(filePath);
                player.setVisible(true); // Ensure visibility
            });
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una canción.");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_RepromusicaActionPerformed

    private void botonDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDocumentosActionPerformed
        String selectedFile = DocList.getSelectedValue();
        if (selectedFile != null) {
            PDFViewer viewer = new PDFViewer("server_content/Documents/" + selectedFile);
            viewer.displayPDF();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un documento para previsualizar.");
        }
    }//GEN-LAST:event_botonDocumentosActionPerformed

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
        if (evt.getClickCount() == 2) {
            abrirArchivo(DocList.getSelectedValue(), "Documents");
        }
    }//GEN-LAST:event_DocListMouseClicked

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        // TODO add your handling code here:
        String selectedFile = getSelectedFileName();
        String fileType = getSelectedFileType();

        if (selectedFile == null || fileType == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un archivo para guardar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(selectedFile));
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            new Thread(() -> {
                try (Socket downloadSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT); PrintWriter out = new PrintWriter(downloadSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(downloadSocket.getInputStream()))) {

                    out.println("DOWNLOAD," + fileType + "," + selectedFile);
                    System.out.println("Solicitud de descarga enviada: DOWNLOAD," + fileType + "," + selectedFile);

                    String response = in.readLine();
                    System.out.println("Respuesta del servidor: " + response);
                    if ("200 OK".equals(response)) {
                        long fileSize = Long.parseLong(in.readLine());
                        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            long totalBytesRead = 0;
                            InputStream input = downloadSocket.getInputStream();
                            while ((bytesRead = input.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                                totalBytesRead += bytesRead;
                                System.out.println("Descargando: " + bytesRead + " bytes");
                            }
                            fos.flush();

                            // Leer el indicador de fin de transmisión
                            String endOfTransmission = in.readLine();
                            if (totalBytesRead == fileSize && "".equals(endOfTransmission)) {
                                System.out.println("Descarga terminada");
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(Client.this, "Archivo guardado exitosamente: " + saveFile.getAbsolutePath());
                                });
                            } else {
                                System.out.println("Error: Tamaño de archivo descargado no coincide");
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(Client.this, "Error al descargar el archivo: Tamaño de archivo incorrecto o transmisión incompleta");
                                });
                            }
                        }
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(Client.this, "Error al descargar el archivo");
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Client.this, "Error al guardar el archivo: " + e.getMessage());
                    });
                }
            }).start(); // Ejecuta el proceso de descarga en un nuevo hilo para mantener la interfaz de usuario responsiva
        }
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void backTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backTwoActionPerformed
   // Obtener la ventana actual y cerrarla
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }

        // Abrir la ventana de inicio de sesión
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            Logiin loginWindow = new Logiin();
            loginWindow.setVisible(true);
        });
    }//GEN-LAST:event_backTwoActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
  // Actualizar el contenido de las listas basadas en los roles del usuario
    loadAllContent();
    JOptionPane.showMessageDialog(this, "Contenido actualizado.");        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> DocList;
    public javax.swing.JList<String> MusicList;
    private javax.swing.JButton Repromusica;
    private javax.swing.JButton Reprovideo;
    public javax.swing.JList<String> VideoList;
    private javax.swing.JButton backTwo;
    private javax.swing.JButton botonDocumentos;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}

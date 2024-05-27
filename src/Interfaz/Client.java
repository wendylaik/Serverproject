package Interfaz;

import java.awt.Desktop;    
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
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
            botonVideos.setEnabled(false);
        }
        if (!canAccessMusic) {
            botonMusica.setEnabled(false);
        }
    }

   private void cargarContenido(String fileType, javax.swing.JList<String> list) {
    try (Socket contentSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT);
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
        try (Socket uploadSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT);
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
    try (Socket downloadSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT);
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
        jLabel5 = new javax.swing.JLabel();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DocList = new javax.swing.JList<>();
        botonDocumentos = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        backTwo = new javax.swing.JButton();

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

        VideoList.setBackground(new java.awt.Color(57, 57, 57));
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
                .addComponent(botonVideos)
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
                .addComponent(botonVideos)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(31, 60, 78));
        jPanel2.setPreferredSize(new java.awt.Dimension(240, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Música");

        MusicList.setBackground(new java.awt.Color(57, 57, 57));
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
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonMusica)
                .addGap(114, 114, 114))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonMusica)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(4, 53, 72));

        jLabel4.setBackground(new java.awt.Color(4, 53, 72));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Documentos");

        DocList.setBackground(new java.awt.Color(57, 57, 57));
        DocList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DocListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DocList);

        botonDocumentos.setBackground(new java.awt.Color(2, 40, 55));
        botonDocumentos.setForeground(new java.awt.Color(255, 255, 255));
        botonDocumentos.setText("Cargar Docs");
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
                .addComponent(botonDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonDocumentos)
                .addGap(15, 15, 15))
        );

        botonGuardar.setBackground(new java.awt.Color(31, 60, 78));
        botonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        botonGuardar.setText("Guardar");
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(backTwo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(botonGuardar)
                    .addComponent(backTwo))
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void botonDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDocumentosActionPerformed
        // TODO add your handling code here:
        cargarArchivo("Documents");
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
            try (Socket downloadSocket = new Socket(Logiin.SERVER_ADDRESS, Logiin.SERVER_PORT);
                 PrintWriter out = new PrintWriter(downloadSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(downloadSocket.getInputStream()))) {

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

    }//GEN-LAST:event_backTwoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> DocList;
    public javax.swing.JList<String> MusicList;
    public javax.swing.JList<String> VideoList;
    private javax.swing.JButton backTwo;
    private javax.swing.JButton botonDocumentos;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton botonMusica;
    private javax.swing.JButton botonVideos;
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

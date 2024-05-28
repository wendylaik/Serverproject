package Interfaz;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import server.Flujocliente;



/**
 *
 * @author Jordy vindas
 */
public class Register extends javax.swing.JPanel {

    public static Socket sharedSocket;
    public static boolean sharedAuth;
    private static final String SERVER_ADDRESS = "25.65.94.55"; // Cambia esto con la dirección IP de tu servidor
    private static final int SERVER_PORT = 12345;

    public Register() {

        initComponents();
        centerWindow();
    }
    
     private void centerWindow() {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Username = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        confirmPass = new javax.swing.JPasswordField();
        btnRegistro = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        documentRole = new javax.swing.JCheckBox();
        videoRole = new javax.swing.JCheckBox();
        musicRole = new javax.swing.JCheckBox();
        back = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(46, 87, 112));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ingrese su nombre de usuario:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ingrese una contraseña:");

        Password.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Confirmar contraseña");

        confirmPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPassActionPerformed(evt);
            }
        });

        btnRegistro.setBackground(new java.awt.Color(23, 50, 78));
        btnRegistro.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnRegistro.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistro.setText("Registrar");
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(4, 53, 72));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Registro");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(362, 362, 362)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(27, 27, 27))
        );

        jPanel3.setBackground(new java.awt.Color(4, 53, 72));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Seleccione el servicio deseado");

        documentRole.setBackground(new java.awt.Color(23, 50, 78));
        documentRole.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        documentRole.setForeground(new java.awt.Color(255, 255, 255));
        documentRole.setText("Documentos");
        documentRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentRoleActionPerformed(evt);
            }
        });

        videoRole.setBackground(new java.awt.Color(23, 50, 78));
        videoRole.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        videoRole.setForeground(new java.awt.Color(255, 255, 255));
        videoRole.setText("Videos");

        musicRole.setBackground(new java.awt.Color(23, 50, 78));
        musicRole.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        musicRole.setForeground(new java.awt.Color(255, 255, 255));
        musicRole.setText("Musica");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(documentRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 74, Short.MAX_VALUE))
                    .addComponent(videoRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(musicRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(documentRole, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(videoRole, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(musicRole, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        back.setBackground(new java.awt.Color(23, 50, 78));
        back.setForeground(new java.awt.Color(255, 255, 255));
        back.setText("Volver ");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPass, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(Password)
                    .addComponent(Username))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(285, 285, 285)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(confirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(btnRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroActionPerformed
                                                  
    String username = Username.getText();
    char[] passChars = Password.getPassword();
    String password = new String(passChars);
    char[] confirmP = confirmPass.getPassword();
    String confirm = new String(confirmP);

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre de usuario y una contraseña");
    } else if (verificarUsuarioExistente(username)) {
        JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso. Por favor elige otro.");
    } else if (!password.equals(confirm)) {
        JOptionPane.showMessageDialog(this, "La contraseña no coincide");
    } else {
        StringBuilder roles = new StringBuilder();
        if (documentRole.isSelected()) {
            roles.append("Documents,");
        }
        if (videoRole.isSelected()) {
            roles.append("Videos,");
        }
        if (musicRole.isSelected()) {
            roles.append("Music,");
        }

        if (roles.length() > 0) {
            roles.setLength(roles.length() - 1);  // Eliminar la última coma
        } else {
            JOptionPane.showMessageDialog(this, "Por favor seleccione al menos un rol");
            return;
        }

        String role = roles.toString();
        System.out.println("Roles seleccionados: " + role);  // Verificación de roles seleccionados

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Flujocliente clientCommunication = new Flujocliente(socket, in, out);
            clientCommunication.sendMessage("REGISTER," + username + "," + password + "," + role);

            String response = clientCommunication.receiveMessage();
            System.out.println("Response from server: " + response);

            if (response.startsWith("auth exitoso true")) {
                JOptionPane.showMessageDialog(this, "Registro exitoso");
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.dispose();  // Cierra la ventana actual
                Logiin loginPanel = new Logiin();
                loginPanel.setVisible(true);  // Abre la ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Error en el registro");
            }

        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor");
        }
    }

    }//GEN-LAST:event_btnRegistroActionPerformed

    private void documentRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documentRoleActionPerformed

    private void confirmPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmPassActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
         JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();
        }
        Logiin log = new Logiin();
        log.setVisible(true);
        
    }//GEN-LAST:event_backActionPerformed
    private void abrirVentanaCliente(String username, String roles, Flujocliente clientCommunication) {
        List<String> rolesList = Arrays.asList(roles.split(","));
        Client client = new Client(username, clientCommunication, rolesList);
        client.setVisible(true);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }

    private boolean verificarUsuarioExistente(String username) {
        // Ruta del archivo donde se almacenan los usuarios y contraseñas
        String rutaArchivo = "Users.txt";

        // Lee el archivo línea por línea para buscar el nombre de usuario
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Divide la línea en nombre de usuario y contraseña usando una coma como delimitador
                String[] partes = linea.split(",");
                // Compara el nombre de usuario con el proporcionado
                if (partes.length > 0 && partes[0].equals(username)) {
                    return true; // El usuario ya existe
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return false; // El usuario no existe
    }

//    private void guardarUsuario(String username, String password) {
//        String rutaArchivo = "Users.txt";
//        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo, true))) {
//            pw.println(username + "," + password );
//        } catch (IOException e) {
//            System.err.println("Error al escribir en el archivo: " + e.getMessage());
//        }
//    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField Username;
    private javax.swing.JButton back;
    private javax.swing.JButton btnRegistro;
    private javax.swing.JPasswordField confirmPass;
    private javax.swing.JCheckBox documentRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox musicRole;
    private javax.swing.JCheckBox videoRole;
    // End of variables declaration//GEN-END:variables

}

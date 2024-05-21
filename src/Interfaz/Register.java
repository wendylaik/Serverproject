package Interfaz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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
    private static final String SERVER_ADDRESS = "192.168.1.35"; // Cambia esto con la dirección IP de tu servidor
    private static final int SERVER_PORT = 8080;

    public Register() {

        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Username = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        btnRegistro = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        documentRole = new javax.swing.JCheckBox();
        videoRole = new javax.swing.JCheckBox();
        musicRole = new javax.swing.JCheckBox();
        confirmPass = new javax.swing.JPasswordField();

        Password.setToolTipText("");

        btnRegistro.setText("Registrar");
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroActionPerformed(evt);
            }
        });

        jLabel1.setText("Ingrese su nombre de usuario");

        jLabel2.setText("Ingrese una contraseña");

        jLabel3.setText("Confirmar contraseña");

        jLabel4.setText("Seleccione el servicio deseado");

        jLabel5.setText("Registro");

        documentRole.setText("Documentos");
        documentRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentRoleActionPerformed(evt);
            }
        });

        videoRole.setText("Videos");

        musicRole.setText("Musica");

        confirmPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(confirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))))
                .addGap(0, 123, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Password)
                                .addComponent(btnRegistro))
                            .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(videoRole)
                            .addComponent(documentRole)
                            .addComponent(musicRole)
                            .addComponent(jLabel4))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(documentRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(videoRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(musicRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnRegistro)
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroActionPerformed
        String username = Username.getText(); // Obtiene el nombre de usuario del campo de texto    
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
            // Obtener los roles seleccionados
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

            Socket socket = null;
            Flujocliente clientCommunication = null;
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Crear instancia de ClientCommunication para manejar la comunicación con el servidor
                clientCommunication = new Flujocliente(socket, in, out);

                // Envía el nombre de usuario, la contraseña, los roles y el indicador de autenticación compartida al servidor
                clientCommunication.sendMessage("1," + username + "," + password + "-" + Logiin.sharedAuth + "," + role);

                // Recibir respuesta del servidor
                String response = clientCommunication.receiveMessage();

                // Si la autenticación fue exitosa, procede a abrir la ventana del cliente
                if (response.startsWith("auth exitoso")) {
                    boolean isAuthenticated = Boolean.parseBoolean(response.substring(13));
                    if (isAuthenticated) {
                        Logiin.sharedSocket = socket;
                        Logiin.sharedAuth = true;
                 //       guardarUsuario(username, password);  // Guarda credenciales de usuario
                        abrirVentanaCliente(username, role, clientCommunication);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al conectar con el servidor: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error al conectar con el servidor");
                try {
                    if (clientCommunication != null) {
                        clientCommunication.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    System.err.println("Error al cerrar el socket: " + ex.getMessage());
                }
            } finally {
                Username.setText("");
                Password.setText("");
                confirmPass.setText("");

            }
        }

    }//GEN-LAST:event_btnRegistroActionPerformed

    private void documentRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documentRoleActionPerformed

    private void confirmPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmPassActionPerformed
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
    private javax.swing.JButton btnRegistro;
    private javax.swing.JPasswordField confirmPass;
    private javax.swing.JCheckBox documentRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JCheckBox musicRole;
    private javax.swing.JCheckBox videoRole;
    // End of variables declaration//GEN-END:variables

}

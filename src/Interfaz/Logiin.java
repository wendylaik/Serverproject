package Interfaz;

import java.awt.Color;
import java.util.List;
import java.io.*;
import javax.swing.JOptionPane;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import server.Flujocliente;

public class Logiin extends javax.swing.JFrame {

    public static Socket sharedSocket;
    public static boolean sharedAuth;
    private static final String SERVER_ADDRESS = "25.65.94.55"; // Cambia esto con la dirección IP de tu servidor
    private static final int SERVER_PORT = 8080; // Cambia esto con el puerto en el que tu servidor está escuchando

    public Logiin() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        UserName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        LoginButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnRegistrarse = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 255));

        jPanel1.setBackground(new java.awt.Color(4, 53, 72));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Bienvenido");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Por favor registrate o inicia sesión");

        jPanel3.setBackground(new java.awt.Color(46, 87, 112));
        jPanel3.setPreferredSize(new java.awt.Dimension(350, 244));

        UserName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        UserName.setToolTipText("User");
        UserName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        UserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UserNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                UserNameFocusLost(evt);
            }
        });
        UserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserNameActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Contraseña:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Usuario:");

        LoginButton.setText("Iniciar Sesión");
        LoginButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Inicio de sesión");

        btnRegistrarse.setText("Registro");
        btnRegistrarse.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarseActionPerformed(evt);
            }
        });

        txtPassword.setToolTipText("");
        txtPassword.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(22, 22, 22))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRegistrarse, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                    .addComponent(UserName)
                    .addComponent(txtPassword))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LoginButton)
                    .addComponent(btnRegistrarse))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(45, 45, 45)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarseActionPerformed
        this.dispose();
        JFrame frame = new JFrame("Registro");
        Register registerPanel = new Register();
        frame.add(registerPanel);
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_btnRegistrarseActionPerformed

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
       if(txtPassword.getText().equals("Ingrese su contraseña")){
           txtPassword.setText("");
           txtPassword.setForeground(new Color(0,0,0));
       }
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
       if(txtPassword.getText().equals("")){
           txtPassword.setText("Ingrese su contraseña");
           txtPassword.setForeground(new Color(204,204,204));}
    }//GEN-LAST:event_txtPasswordFocusLost

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void UserNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UserNameFocusGained
        if(UserName.getText().equals("Ingrese su usuario")){
           UserName.setText("");
           UserName.setForeground(new Color(0,0,0));
       }
    }//GEN-LAST:event_UserNameFocusGained

    private void UserNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UserNameFocusLost
       if(UserName.getText().equals("")){
           UserName.setText("Ingrese su usuario");
           UserName.setForeground(new Color(204,204,204));
       }
    }//GEN-LAST:event_UserNameFocusLost

    private void UserNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_UserNameActionPerformed
    }// GEN-LAST:event_UserNameActionPerformed

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_PasswordActionPerformed
    }// GEN-LAST:event_PasswordActionPerformed
    
    private List<String> traerRoles(String username) {
    // Ruta del archivo donde se almacenan los usuarios y contraseñas
    String rutaArchivo = "Roles.txt";

    // Lista para almacenar los roles del usuario
    List<String> role = new ArrayList<>();

    // Lee el archivo línea por línea para buscar el nombre de usuario
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            // Divide la línea en nombre de usuario y roles usando una coma como delimitador
            String[] partes = linea.split(",");
            // Compara el nombre de usuario con el proporcionado
            if (partes.length > 0 && partes[0].equals(username)) {
                // Agrega los roles del usuario a la lista
                for (int i = 1; i < partes.length; i++) {
                    role.add(partes[i].trim()); // Utiliza trim() para eliminar espacios en blanco
                }
                // Devuelve la lista de roles
                return role;
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer el archivo: " + e.getMessage());
    }

    // Si el usuario no se encuentra, devuelve una lista vacía
    return role;
}
    
    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {
    String username = UserName.getText();
    char[] passChars = txtPassword.getPassword();
    String password = new String(passChars);

    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        Flujocliente clientCommunication = new Flujocliente(socket, in, out);

        // Enviar mensaje de autenticación al servidor
        clientCommunication.sendMessage("0," + username + "," + password);

        String response = clientCommunication.receiveMessage();

        if (response != null && response.startsWith("auth exitoso true")) {
            // Solicitar roles al servidor
            clientCommunication.sendMessage("getRoles," + username);
            String rolesResponse = clientCommunication.receiveMessage();
            List<String> roles = Arrays.asList(rolesResponse.split(","));
            Logiin.sharedSocket = socket;
            Logiin.sharedAuth = true;
            abrirVentanaCliente(username, roles, clientCommunication);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Usuario o contraseña incorrectos");
        }

    } catch (IOException e) {
        System.err.println("Error al conectar con el servidor: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error al conectar con el servidor");
    }
}

    private void abrirVentanaCliente(String username, List<String> roles, Flujocliente clientCommunication) {
        Client client = new Client(username, clientCommunication, roles);
        client.setVisible(true);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }
    public static void main(String args[]) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Dirección IP del servidor: " + inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println("No se pudo obtener la dirección IP del servidor: " + e.getMessage());
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Logiin().setVisible(true);

            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LoginButton;
    private javax.swing.JTextField UserName;
    private javax.swing.JButton btnRegistrarse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}

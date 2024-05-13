
package Interfaz;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private static final String SERVER_ADDRESS = "192.168.100.4"; // Cambia esto con la dirección IP de tu servidor
    private static final int SERVER_PORT = 8080;

    
    public Register() {

        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Username = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();

        Username.setText("jTextField1");

        Password.setText("jPasswordField1");

        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(Username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String username = Username.getText(); // Obtiene el nombre de usuario del campo de texto    
    char[] passChars = Password.getPassword();
    String password = new String(passChars);

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre de usuario y una contraseña");
    } else if (verificarUsuarioExistente(username)) {
        JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso. Por favor elige otro.");
    } else {
        Socket socket = null;
        Flujocliente clientCommunication = null;
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Crear instancia de ClientCommunication para manejar la comunicación con el servidor
            clientCommunication = new Flujocliente(socket, in, out);

            // Envía el nombre de usuario, la contraseña y el indicador de autenticación compartida al servidor
            clientCommunication.sendMessage("1," + username + "," + password + "-" + Logiin.sharedAuth);

            // Recibir respuesta del servidor
            String response = clientCommunication.receiveMessage();

            // Si la autenticación fue exitosa, procede a abrir la ventana del cliente
            if (response.startsWith("auth exitoso")) {
                boolean isAuthenticated = Boolean.parseBoolean(response.substring(13));
                if (isAuthenticated) {
                    // Establecer la autenticación compartida a verdadera
                    Logiin.sharedSocket = socket;
                    Logiin.sharedAuth = true;
                    abrirVentanaCliente(username, clientCommunication);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor");
            // Cerrar el socket y la comunicación del cliente si ocurrió algún error
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
            // Limpiar los campos de texto después del registro
            Username.setText("");
            Password.setText("");
        }

    }

    }//GEN-LAST:event_jButton1ActionPerformed
 private void abrirVentanaCliente(String username, Flujocliente clientCommunication) {
    // Crea una instancia de la ventana del cliente
    Client client = new Client(username, clientCommunication);
    client.setVisible(true);
    
    // Cierra la ventana de registro actual
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
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField Username;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

      
    }


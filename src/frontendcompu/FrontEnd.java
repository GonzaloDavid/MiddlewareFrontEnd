package frontendcompu;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class FrontEnd extends Thread {

    private DataOutputStream salidaDatos;
    private InputStreamReader inputStream;
    private Socket socket;
    private BufferedReader entrada;
    
    private DataOutputStream salidaDatosB;
    private InputStreamReader inputStreamB;
    private Socket socketB;
    private BufferedReader entradaB;
    
    private String msgCliente = "";
    private String msgBack = "";
    private static int numThreads = 1;
    Connection conexion;

    public FrontEnd(Socket socket) {
        Conexion conec = new Conexion();
        conexion = conec.conectar();
        this.socket = socket;
        this.socketB=socket;
        numThreads++;

    }

    public void grabarArchivo(String datoI, int numArchivo) {
        try {
            String direccion1 = "C:\\Users\\DaViD\\Desktop\\epn\\SEMESTRE POLI\\Sexto Semestre\\computacion distribuida\\SERVER_HIGH_ENABLE\\Server.log" + numArchivo + ".txt";

            File abrir = new File(direccion1);
            FileWriter w = new FileWriter(abrir);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            String aux = "\r\n" + datoI + "\r\n";
            wr.write(aux);

            wr.close();
            bw.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "La direccion del archivo esta incorrecta");
        }

    }

    public void enviarCliente(String Respuesta) throws IOException {
        this.salidaDatos = new DataOutputStream(socket.getOutputStream());
        this.salidaDatos.writeUTF(Respuesta + "\n");
    }
    public void enviarFrontEnd(String Respuesta) throws IOException {
        this.salidaDatosB = new DataOutputStream(socketB.getOutputStream());
        this.salidaDatosB.writeUTF(Respuesta + "\n");
    }

    public static String AlgoritmoCesar(String cadena, int delta) {

        String Minu = "abcdefghijklmnñopqrstuvwxyz";
        String Mayu = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String texto;
        texto = cadena;
        String Cifrado = "";

        for (int n = 0; n < texto.length(); n++) {
            if ((Minu.indexOf(texto.charAt(n)) != -1) || (Mayu.indexOf(texto.charAt(n)) != -1)) {
                if (Minu.indexOf(texto.charAt(n)) != -1) {
                    Cifrado += Minu.charAt(((Minu.indexOf(texto.charAt(n))) + delta) % Minu.length());
                } else {

                    Cifrado += Mayu.charAt((Mayu.indexOf(texto.charAt(n)) + delta) % Mayu.length());
                }
            } else {
                Cifrado += texto.charAt(n);
            }
        }
        return Cifrado;
    }

    public String getMsgCliente() {
        return msgCliente;
    }

    public void SepararCaracteres(String datoConmpleto) throws IOException {
        String total;
        int id = 0;
        int delta = 0;
        String nombre, pais, cargo, facultad, mensaje, contrasena, contrasenaE, hash1;
        StringTokenizer tokens = new StringTokenizer(datoConmpleto, ",");

        id = (int) Math.floor(Math.random() * 10 + 1);
        //id=Integer.parseInt(tokens.nextToken());
       
        nombre = tokens.nextToken();
        pais = tokens.nextToken();
        cargo = tokens.nextToken();
        facultad = tokens.nextToken();
        mensaje = tokens.nextToken();
        contrasena = tokens.nextToken();
        contrasenaE = tokens.nextToken();
        hash1 = tokens.nextToken();

        total = id + nombre + pais + cargo + facultad + mensaje + contrasena + contrasenaE + hash1;
        System.err.println(total);
        if (facultad.equals("Petroleos")) {
            delta = 1;
        }
        if (facultad.equals("Sistemas")) {
            delta = 2;
        }
        if (facultad.equals("Electronica")) {
            delta = 3;
        }
        if (facultad.equals("Mecanica")) {
            delta = 4;
        }
        if (facultad.equals("Quimica")) {
            delta = 5;
        }
        if (facultad.equals("Civil")) {
            delta = 6;
        }

        contrasenaE = AlgoritmoCesar(contrasena, delta);
        enviarCliente(contrasenaE);
        llenarBDD(1, nombre, pais, cargo, facultad, mensaje, contrasena, contrasenaE, hash1);
        llenarFacultad(1, facultad);
        llenarTipo(1, cargo);

    }

    public void llenarBDD(int id, String nombre, String pais, String cargo, String facultad, String mensaje, String contrasena, String contrasenaE, String hash1) {
        try {

            String sql = "execute AgregarPersonas ?,?,?,?,?,?,?,?,?";
            PreparedStatement ps = conexion.prepareCall(sql);
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, pais);
            ps.setString(4, cargo);
            ps.setString(5, facultad);
            ps.setString(6, mensaje);
            ps.setString(7, contrasena);
            ps.setString(8, contrasenaE);
            ps.setString(9, hash1);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            System.out.println("ERROR AL INGRESAR A LA BASE DE DATOS PERSONA "+e);
        }

    }

    public void llenarFacultad(int id, String facultad) {
        try {
            String sql = "execute AgregarFacultad ?,?";
            PreparedStatement ps = conexion.prepareCall(sql);
            ps.setInt(1, id);
            ps.setString(2, facultad);

            ps.execute();
            ps.close();
            conexion.close();
            
        } catch (Exception e) {
            System.out.println("ERROR AL INGRESAR A LA BASE DE DATOS FACULTAD "+e);
        }

    }

    public void llenarTipo(int id, String Cargo) {
        try {
            String sql = "execute AgregarTipo ?,?";
            PreparedStatement ps = conexion.prepareCall(sql);
            ps.setInt(1, id);
            ps.setString(2, Cargo);

            ps.execute();
            ps.close();

            
        } catch (Exception e) {
            System.out.println("ERROR AL INGRESAR A LA BASE DE DATOS tipo "+e);
        }

    }

    public void configuracion()
    {
        AplicacionCliente();
      try {
            inputStream = new InputStreamReader(socket.getInputStream());
            entrada = new BufferedReader(inputStream);
            salidaDatos = new DataOutputStream(socket.getOutputStream());
            while (true) {

                msgCliente = entrada.readLine();
                enviarCliente("BIENVENIDO AL SERVER DE LA EPN" + numThreads);
                SepararCaracteres(msgCliente);
                System.out.println("Recibe Server:" + msgCliente.trim());
                
               
            }
        } catch (Exception e) {
            System.out.println("Se ha desconectado1: " + socket + "      Hora:   " + new Date());
            numThreads--;
        }
    
    }
    public void configuracion1()
    { AplicacionBack();
      try {
            inputStreamB = new InputStreamReader(socketB.getInputStream());
            entradaB = new BufferedReader(inputStreamB);
            salidaDatosB = new DataOutputStream(socketB.getOutputStream());
            while (true) {

                msgBack= entradaB.readLine();
                enviarCliente("BIENVENIDO AL SERVER DE LA EPN" + numThreads);
                SepararCaracteres(msgBack);
                System.out.println("Recibe Server:" + msgBack.trim());
                
               
            }
        } catch (Exception e) {
            System.out.println("Se ha desconectado1: " + socketB + "      Hora:   " + new Date());
            numThreads--;
        }
    
    }
    public void AplicacionCliente()
    {
    
      String login = "";
        ServerSocket ss = null;
        Socket socket = null;
       
              
        System.out.print("Inicializando servidor... ");
        try {
            ss = new ServerSocket(5555);
            System.out.println("\t[OK]");

            while (true) {

                socket = ss.accept();
                FrontEnd servidorsito = new FrontEnd(socket);
                    socket.close();
                   
                    login += "\r\nNueva conexión entrante: " + socket + "     Hora: " + new Date() + "\n\r";
                    servidorsito.grabarArchivo(login, 0);

               }

        } catch (IOException ex) {
            Logger.getLogger(FrontEnd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void AplicacionBack()
    {
    
        String login = "";
        ServerSocket ss1 = null;
        Socket socket1 = null;
       
              
        System.out.print("Inicializando servidor... ");
        try {
            ss1 = new ServerSocket(7777);
            System.out.println("\t[OK]");

            while (true) {

                socket1 = ss1.accept();
                FrontEnd servidorsito = new FrontEnd(socket1);
                    socket1.close();
                    
                    login += "\r\nNueva conexión entrante: " + socket + "     Hora: " + new Date() + "\n\r";
                    servidorsito.grabarArchivo(login, 0);

               }

        } catch (IOException ex) {
            Logger.getLogger(FrontEnd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

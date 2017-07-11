package clientedefinitivo;

import java.io.*;
import java.net.*;

public class ConetorCliente extends Thread {

    private Socket sokett;
    private InputStreamReader entradaSocket;
    private InputStream inputStream;
    private DataInputStream input;
    private DataOutputStream salida;
    private BufferedReader entrada;

    final int puerto = 2525;

    public ConetorCliente(String ip) {
        try {
            String envio = "";
            sokett = new Socket(ip, puerto);

            entradaSocket = new InputStreamReader(sokett.getInputStream());
            entrada = new BufferedReader(entradaSocket);
            inputStream = sokett.getInputStream();
            input = new DataInputStream(inputStream);

            salida = new DataOutputStream(sokett.getOutputStream());
            envio = "gonzalo,Ecuador,Estudiante,Mecanica,holaMijin,contra,contra,123";
            EnviaServidor(envio);

        } catch (Exception e) {
            System.out.println("SERVER CAIDO 1" + e);
        }

    }

    private void EnviaServidor(String cadena) {
        try {

            this.salida = new DataOutputStream(sokett.getOutputStream());
            this.salida.writeUTF(cadena + "\n");

        } catch (IOException e) {
            System.out.println("SERVER CAIDO 2" + e);
        }
    }

    public void run() {

        String cadena1;

        while (true) {
            try {
                cadena1 = entrada.readLine();
                System.out.println(cadena1.trim());//imprime lo que recibe del server

            } catch (IOException e) {
                System.out.println("SERVER CAIDO O DDOS" + e);
            }
        }

    }

}

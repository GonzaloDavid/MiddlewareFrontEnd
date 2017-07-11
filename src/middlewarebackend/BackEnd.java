package middlewarebackend;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author DaViD
 */
public class BackEnd extends Thread {

    private Socket sokett;
    private InputStreamReader entradaSocket;
    private InputStream inputStream;
    private DataInputStream input;
    private DataOutputStream salida;
    private BufferedReader entrada;

    final int puerto = 4545;
    int tic = 0;

    

    public void BackEndt() {
        String envio;
        envio="Sigo vivo";
         Timer timer = new Timer();
        TimerTask task = new TimerTask() {

        @Override
        public void run() {
            if (tic % 2 == 0) {
                
                System.out.println(envio);
                EnviaFronEnd(envio);
            } else {
                System.out.println("");
            }
            tic++;
        }

    };
        timer.schedule(task, 1000, 1000);
    }
    
    
    public BackEnd(String ip) {

        try {
            String envio = "";
            sokett = new Socket(ip, puerto);

            entradaSocket = new InputStreamReader(sokett.getInputStream());
            entrada = new BufferedReader(entradaSocket);
            inputStream = sokett.getInputStream();
            input = new DataInputStream(inputStream);

            salida = new DataOutputStream(sokett.getOutputStream());
            

        } catch (Exception e) {
            System.out.println("SERVER CAIDO 1" + e);
        }

    }


    private void EnviaFronEnd(String cadena) {
        try {

            this.salida = new DataOutputStream(sokett.getOutputStream());
            this.salida.writeUTF(cadena + "\n");

        } catch (IOException e) {
            System.out.println("SERVER CAIDO 2" + e);
        }
    }
   

    @Override
    public void run() {

        String cadena1;
        BackEndt();

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

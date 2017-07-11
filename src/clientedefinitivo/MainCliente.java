package clientedefinitivo;

/**
 *
 * @author DaViD
 */
public class MainCliente {

    public static ConetorCliente cliente;

    public static void main(String[] args) {
        String ip="localhost";
        int numeroClientes=1;
        for (int i = 0; i < numeroClientes; i++) {
            cliente = new ConetorCliente(ip);  
            cliente.start();
        }
    }
   
}

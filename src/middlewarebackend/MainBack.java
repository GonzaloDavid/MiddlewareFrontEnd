
package middlewarebackend;

/**
 *
 * @author DaViD
 */
public class MainBack {

    public static BackEnd back;
    public static void main(String[] args) {
        
         String ip="localhost";
        int numeroClientes=1;
        for (int i = 0; i < numeroClientes; i++) {
            back = new BackEnd(ip);  
            back.start();
        }
        
        
    }

    
}

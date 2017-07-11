package frontendcompu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
    
    public Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            conexion = DriverManager.getConnection("jdbc:sqlserver://DAVID\\SQLEXPRESS\\local:1433;databaseName=AplicacionSever;user=compu;password=12345;");
            
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error " + e);
        }
        return conexion;
    }
    
}

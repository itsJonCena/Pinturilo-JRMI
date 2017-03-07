package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>Clase que sirve para realizar las tareas comunes para interactuar con una Base de Datos en MySQL.
 * Realiza las tareas de insertar, borrar y actualizar registros, así como de llevar a cabo consultas en
 * alguna tabla especificada.</p>
 * <p>Un ejemplo de las sentencias SQL esperadas por los métodos:</p>
 * <table border="1">
 * <tr><td>Acción</td><td>Sentencia SQL</td></tr>
 * <tr><td>Consulta</td><td>SELECT column_name(s) FROM table_name WHERE column_name operator value</td></tr>
 * <tr><td>Insertar</td><td>INSERT INTO table_name VALUES (value1, value2, value3,...)</td></tr>
 * <tr><td>Borrar</td><td>UPDATE table_name SET column1=value, column2=value2,... WHERE some_column=some_value</td></tr>
 * <tr><td>Actualizar</td><td>DELETE FROM table_name WHERE some_column=some_value</td></tr>
 * </table>
 * @author Luis Enrique Cuevas Díaz
 */
public class Conexion {
    //Atributos de conexión
    public static final String usrBD = "root";
    public static final String passBD = "";

    private Connection conexion;
    private Statement st;

    /**
     * <p>Constructor para crear la conexión a la Base de Datos a través de {@code JBDC}.
     * Los datos que requiere son el usuario, la contraseña y la base de datos a utilizar.</p>
     * @param userDB El nombre de usuario con el cual debe hacerse la conexión a la Base de Datos.
     * @param passDB La contraseña para llevar a cabo la conexión a la Base de Datos.
     * @param projectDB El nombre de la Base de Datos a utilizar.
     * @throws ClassNotFoundException Si el conector {@code JDBC} no se encuentra.
     * @throws SQLException Si sucede algún error en la conexión a la Base Datos.
     */
    public Conexion(String userDB, String passDB, String projectDB) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.gjt.mm.mysql.Driver"); //Comprobar el conector
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/"+projectDB, userDB, passDB);
            st=conexion.createStatement();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Conector no encontrado. Mensaje de error: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new SQLException("Error en SQL. Mensaje de error: " + ex.getMessage());
        }
    }

    /**
     * Recibe alguna sentencia para insertar y la ejecuta
     * @param instruccion La sentencia SQL
     * @return Si se insertó o no
     */
    public boolean insertar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                st.execute(instruccion); //Ejecuta los commandos SQL
            }else{ // Error en la conexión
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                return false;
            }
        }catch(SQLException ex){
            System.out.println("Mensaje de error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Recibe alguna sentencia para borrar y la ejecuta
     * @param instruccion La sentencia SQL
     * @return Si se borró o no
     */
    public boolean borrar(String instruccion) {
        return insertar(instruccion);
    }

    /**
     * Recibe alguna sentencia para actualizar y la ejecuta
     * @param instruccion La sentencia SQL
     * @return Si se actualizó o no
     */
    public boolean actualizar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                st.executeUpdate(instruccion); //Ejecuta el comando de actualización SQL
            }else{ // Error en la conexión
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                return false;
            }
        }catch(SQLException ex){
            System.out.println("Error "+ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Recibe alguna sentencia para hacer una selección y la ejecuta
     * @param instruccion La sentencia SQL
     * @return La selección hecha, si no hay registros devuelve null
     */
    public ResultSet buscar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                ResultSet rs = st.executeQuery(instruccion); //Ejecuta el query de SQL
                if(!rs.next()){
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }

                return rs;
            }else{ //Error en la conexión
                System.out.println("La conexión con la Base de Datos está cerrada.");
                return null;
            }
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
    }

    /**
     * Busca la cantidad de registros que hay en la tabla especificada.
     * Devuelve -1 en caso de haber algún error
     * @param tabla La tabla donde contar registros
     * @return La cantidad de registros encontrados o -1 si hay algún error
     */
    public int contarRegistros(String tabla) {
        ResultSet rs = buscar("SELECT COUNT(*) FROM " + tabla);
        if (rs != null) {
            try {
                return rs.getInt(1);
            } catch (SQLException ex) {
                System.out.println("Error " + ex.getMessage());
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Cerrar la conexión con la BD.
     * Cuando ya no se necesite usar la conexión a la BD, es recomendable cerrarla.
     * No se puede volver a utilizar el objeto después de cerrar la conexión
     */
    public void cerrar() {
        try {
            st.close(); // Cerrar el Statement
            conexion.close(); // Cierre de la conexión
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            conexion=null;
            st=null;
        }
    }
}
//LECD 21-julio-11
//LECD 16-abril-12
//Actualizándose

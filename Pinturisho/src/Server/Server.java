package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by lordkarsbein on 3/03/17.
 */
public class Server {
    public static void main (String[] argv) {
        try {

            ServerImp server = new ServerImp();
            ServerInterface stub = (ServerInterface) server;
            Registry registry = LocateRegistry.createRegistry(2001);
            registry.bind("RMIChat", stub);
            System.out.println("Server working...");


        }catch (Exception e) {
            System.out.println("[System] Server failed: " + e);
        }
    }
}

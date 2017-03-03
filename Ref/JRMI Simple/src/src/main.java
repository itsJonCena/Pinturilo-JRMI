package src;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Alex on 02/03/17.
 */
public class main {

    public static void main(String args[]) {

        try {
            //System.setProperty("java.rmi.server.hostname","127.0.0.1");
            HelloImplement obj = new HelloImplement();
            //Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 2001);

            //stub = (Hello) Naming.lookup("//localhost/hello");

            //Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry(2001);
            Registry registry = LocateRegistry.createRegistry(2001);
            registry.rebind("Hello", obj);
            //Naming.bind("rmi://127.0.0.1/hello",stub);

            System.err.println("src.HelloImplement ready");
        } catch (Exception e) {
            System.err.println("src.HelloImplement exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

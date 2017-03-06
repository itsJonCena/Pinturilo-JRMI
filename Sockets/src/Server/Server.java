package Server;

import Interface.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Alex on 04/03/17.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerImplementation servImp = new ServerImplementation();

            ServerInterface stub = servImp;

            Registry registry = LocateRegistry.createRegistry(2001);
            registry.rebind("Pinturillo",stub);

            System.err.println("Server ready ...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

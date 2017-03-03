package Server;
import Interface.Request;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;

/**
 * Created by Alex on 01/03/17.
 */
public class Server{

    public static void main(String[] args) {
        try {



            RequesImplementation requesImplementation = new RequesImplementation();
            Request stub = (Request)requesImplementation;

            Registry registry = LocateRegistry.createRegistry(2001);
            
            try {
                String hostname = InetAddress.getLocalHost().getHostAddress();
                System.out.println("this host IP is " + hostname);
                registry.bind("Pinturillo_JRMI",stub);
            } catch (AlreadyBoundException e) {
                e.printStackTrace();
            } catch ( UnknownHostException e){
                e.printStackTrace();
            }
            //Registry registry = LocateRegistry.createRegistry(1099);


            //registry.rebind("rmi://127.0.0.1/Pinturillo_JRMI", requesImplementation);

            /*//



            Request stub = (Request) UnicastRemoteObject.exportObject(this,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("PinturilloServer",stub);
            */

            System.out.println("Server is ready ...");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

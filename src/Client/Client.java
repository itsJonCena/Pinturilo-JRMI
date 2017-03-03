package Client;
import Server.RequesImplementation;
import Server.Server;
import Interface.Request;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Alex on 01/03/17.
 */
public class Client {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry myreg = LocateRegistry.getRegistry("127.0.0.1",2001);
            
            Request requesImplementation = (Request) myreg.lookup("Pinturillo_JRMI");


            System.out.println(requesImplementation.pWord("hey"));

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        }*/

    }
}

package src;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Alex on 02/03/17.
 */
public class Client {

    private Client() {}

    public static void main(String[] args) {

        //String host = (args.length < 1) ? null : args[0];
        try {

            Registry registry = LocateRegistry.getRegistry("localhost");
            Hello stub = (Hello) registry.lookup("Hello");
            /*
            Hello itsMe = (Hello) Naming.lookup("//localhost/Hello") ;
            Registry registry = LocateRegistry.getRegistry("localhost");

            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            */
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("src.Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
package src;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Alex on 02/03/17.
 */
public class HelloImplement extends UnicastRemoteObject implements Hello {

    public HelloImplement() throws RemoteException {
        super();
    }


    @Override
    public String sayHello() throws RemoteException {
        return "Hello";
    }
}
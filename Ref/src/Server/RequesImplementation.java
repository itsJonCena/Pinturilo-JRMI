package Server;
import Interface.Request;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Alex on 01/03/17.
 */
public class RequesImplementation extends UnicastRemoteObject implements Request {

    public RequesImplementation() throws RemoteException {
        super();
    }

    @Override
    public String pWord(String word) throws RemoteException {
        return "Hi "+word;
    }

}

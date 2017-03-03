package Server;

import Client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lordkarsbein on 3/03/17.
 */
public interface ServerInterface extends Remote {
    void registerChatCln(ClientInterface chatCliente) throws RemoteException;
    void broadcast(String message) throws RemoteException;
}

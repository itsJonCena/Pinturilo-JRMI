package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lordkarsbein on 3/03/17.
 */
public interface ClientInterface extends Remote {
    public void receive(String mssg) throws RemoteException;
    public String getClientName() throws RemoteException;
}

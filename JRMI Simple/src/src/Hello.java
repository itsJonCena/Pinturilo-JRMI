package src;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Alex on 02/03/17.
 */
public interface Hello extends Remote {
    String sayHello() throws RemoteException;
}

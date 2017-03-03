package Interface;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Alex on 01/03/17.
 */
public interface Request extends Remote {

    String pWord(String word) throws RemoteException;

}

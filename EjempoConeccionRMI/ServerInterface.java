import java.rmi.*;

public interface ServerInterface extends Remote{
    public String holaMundo(String text) throws RemoteException;
}

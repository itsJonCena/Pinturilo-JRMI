package ClientBd;

import java.rmi.*;

/**
 * ChatInterface
 */
public interface ChatCInterface extends Remote {
	public void receive(String mssg) throws RemoteException;
	public String getClientName() throws RemoteException;
}
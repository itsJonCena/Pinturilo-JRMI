package ServerBd;

import java.rmi.*;
import ClientBd.*;


/**
 * ChatInterface
 */
public interface ChatSInterface extends Remote {
    void registerChatCln(ChatCInterface chatCliente) throws RemoteException;
    void broadcast(String message) throws RemoteException;
}
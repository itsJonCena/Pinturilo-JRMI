package ServerBd;

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import ClientBd.*;

/**
 * Chat
 */
public class ChatSImpl extends UnicastRemoteObject implements ChatSInterface {

    private ArrayList<ChatCInterface> chatClientes;
    
    public ChatSImpl () throws RemoteException {
        chatClientes = new ArrayList<>();
    }

	public void registerChatCln(ChatCInterface chatCliente){
		this.chatClientes.add(chatCliente);
	}

    public void broadcast(String message) throws RemoteException{
		int i = 0;
		while (i < chatClientes.size()) {
			chatClientes.get(i++).receive(message);
		}
	}
}
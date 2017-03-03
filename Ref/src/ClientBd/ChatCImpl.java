package ClientBd;

import java.rmi.*;
import java.rmi.server.*;
import ServerBd.*;

/**
 * Chat
 */
public class ChatCImpl extends UnicastRemoteObject implements ChatCInterface {

	private ChatSInterface chatServer;
	private String name = null;

    
    public ChatCImpl (String name, ChatSInterface chatServer ) throws RemoteException {
        this.name = name;
		this.chatServer = chatServer;
		chatServer.registerChatCln(this);
    }

    public void receive(String mssg){
		System.out.println(mssg);
	}

	public String getClientName(){
		return this.name;
	}
}
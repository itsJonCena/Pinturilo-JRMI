package Client;

import Server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by lordkarsbein on 3/03/17.
 */
public class ClientImp extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface chatServer;
    private String name = null;


    public ClientImp (String name, ServerInterface chatServer ) throws RemoteException {
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

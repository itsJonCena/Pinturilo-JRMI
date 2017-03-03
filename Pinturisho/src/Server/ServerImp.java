package Server;

import Client.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by lordkarsbein on 3/03/17.
 */
public class ServerImp extends UnicastRemoteObject implements ServerInterface{

    private ArrayList<ClientInterface> chatClientes;

    public ServerImp () throws RemoteException {
        chatClientes = new ArrayList<>();
    }

    public void registerChatCln(ClientInterface chatCliente){
        this.chatClientes.add(chatCliente);
    }

    public void broadcast(String message) throws RemoteException{
        int i = 0;
        while (i < chatClientes.size()) {
            chatClientes.get(i++).receive(message);
        }
    }
}

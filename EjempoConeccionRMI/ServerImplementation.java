import java.rmi.*;
import java.rmi.server.*;

class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

  public ServerImplementation() throws RemoteException{
  }

  public String holaMundo(String text){
    return "Hi " + text;
  }


}

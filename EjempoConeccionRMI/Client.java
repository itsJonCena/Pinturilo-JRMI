import java.net.*;
import java.rmi.*;
import java.net.UnknownHostException;
import java.rmi.registry.*;


class Client {
  public static void main(String[] args) {

    try {
      Registry myreg = LocateRegistry.getRegistry("127.0.0.1",18171); //
      ServerInterface server = (ServerInterface) myreg.lookup("HelloWorld");
      String hostname = InetAddress.getLocalHost().getHostAddress();
      System.out.println(server.holaMundo(hostname));
    }catch (NotBoundException e) {
        e.printStackTrace();
    }catch (RemoteException e) {
        e.printStackTrace();
    } catch (UnknownHostException e) {
        e.printStackTrace();
    }

  }
}

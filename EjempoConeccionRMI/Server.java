import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;

class Server {
  public static void main(String[] args) {
    try {
      ServerImplementation serverImpl = new ServerImplementation();
      ServerInterface stub = (ServerInterface) serverImpl;

      Registry registry = LocateRegistry.createRegistry(18171);

      String hostname = InetAddress.getLocalHost().getHostAddress();
      System.out.println("this host IP is " + hostname);
      registry.bind("HelloWorld",stub);



    }catch (AlreadyBoundException e) {
        e.printStackTrace();
    } catch ( UnknownHostException e){
        e.printStackTrace();
    } catch (RemoteException e) {
        e.printStackTrace();
    }

    System.out.println("Server is ready ...");


  }
}

    package ClientBd;

	import java.rmi.*;
    import java.rmi.server.*;
    import java.util.*;
	import java.rmi.registry.*;
	import ServerBd.*;

     
    public class ChatClient {
    	public static void main (String[] argv) {
    	    try {
    		    	Scanner s=new Scanner(System.in);
    		    	System.out.println("Enter Your name and press Enter:");
    		    	String name=s.nextLine().trim();		    		    	
					Registry myreg = LocateRegistry.getRegistry("127.0.0.1",2001); //
    		    	ChatSInterface server = (ChatSInterface)myreg.lookup("RMIChat");
    		    	ChatCInterface client = new ChatCImpl(name,server);

					String msg = "";

					while (true) {
						msg = s.nextLine().trim();
						server.broadcast(name + " : " +msg);
					}
     
    	    	}catch (Exception e) {
    	    		System.out.println("[System] Server failed: " + e);
    	    	}
    		}
    }
    package ServerBd;
	
	import java.rmi.*;
    import java.rmi.server.*;
    import java.util.*;
	import java.rmi.registry.*;
     
    public class ChatServer {
    public static void main (String[] argv) {
        try {
				
    	    	ChatSImpl server = new ChatSImpl();	
				ChatSInterface stub = (ChatSInterface) server;
	            Registry registry = LocateRegistry.createRegistry(2001);
    	    	registry.bind("RMIChat", stub);
     
     
        	}catch (Exception e) {
        		System.out.println("[System] Server failed: " + e);
        	}
    	}
    }
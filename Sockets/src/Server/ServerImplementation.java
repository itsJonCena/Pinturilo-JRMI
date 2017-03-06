package Server;

import Interface.ServerInterface;
import javafx.scene.paint.Paint;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Alex on 04/03/17.
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private Map<String, Socket> playersList = new LinkedHashMap<>();
    private Map<String, Socket> playersListCanvas = new LinkedHashMap<>();
    private int CHAT_PORT = 2002;
    private int CANVAS_PORT = 2003;
    private int USERS_MAX = 5;
    private DataOutputStream out;
    private ObjectOutputStream objectOutput;


    public ServerImplementation() throws RemoteException {
        super();
    }

    @Override
    public boolean checkNicknameInServer(String name) throws RemoteException {
        if (!playersList.containsKey(name)){

            return true;
        }
        return false;
    }

    @Override
    public void connect(String nickname, String ip) throws RemoteException {

        //todo Retornar un boolean si fue posible conectarse o no
        Socket socketChat = null;
        Socket socketCanvas = null;
        try {
            socketChat = new Socket(ip, CHAT_PORT);
            socketCanvas = new Socket(ip, CANVAS_PORT);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to create Socket");
        }

        try {
            out = new DataOutputStream(socketChat.getOutputStream());
            out.writeUTF("Welcome to chat!!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        playersList.put(nickname,socketChat);
        playersListCanvas.put(nickname,socketCanvas);

        // esto esta de mas
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("Hello test");
    }

    @Override
    public void messege(String message,String name) throws RemoteException {
        //todo Enviar por el socket a todos los usuarios

        for (String key : playersList.keySet()) {
            Socket socketTemp = playersList.get(key);

            try {
                out = new DataOutputStream(socketTemp.getOutputStream());
                out.writeUTF(name+": "+message);

                //out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void sendX_Y(double X, double Y, double lineWidth,String paint) throws RemoteException {
        for (String key : playersListCanvas.keySet()) {
            Socket socketTemp = playersListCanvas.get(key);

            try {
                out = new DataOutputStream(socketTemp.getOutputStream());
                //out.writeUTF("{x:"+X+",y:"+Y+",lineWith:"+lineWidth+",Paint:"+paint+"}");
                out.writeUTF(""+X+" "+Y+" "+lineWidth+" "+paint+"");
                /*
                Map<String,Object> canvasWrite= new LinkedHashMap<>();
                canvasWrite.put("x",X);
                canvasWrite.put("y",Y);
                canvasWrite.put("lineWidth",lineWidth);
                canvasWrite.put("Paint",paint);

                objectOutput.writeObject(canvasWrite);
                */

                //out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

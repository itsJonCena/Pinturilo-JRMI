package Interface;

import javafx.scene.paint.Paint;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Alex on 04/03/17.
 */
public interface ServerInterface extends Remote {

    public void test()throws RemoteException;

    public int messege(boolean onGuess, String message, String name) throws RemoteException;
    public void sendX_Y(String name, double X, double Y, double lineWidth,String paint) throws RemoteException;
    public boolean checkNicknameInServer(String name) throws RemoteException;
    public boolean connect(String nickname, String ip) throws RemoteException;
}

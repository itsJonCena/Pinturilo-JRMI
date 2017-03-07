package Server;

import Conexion.Conexion;
import Interface.ServerInterface;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Alex on 04/03/17.
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private int CHAT_PORT = 2002;
    private int CANVAS_PORT = 2003;
    private int GAME_PORT = 2004;
    private int USERS_MAX = 3;
    private int points=150;
    private int index = 0;
    private int auxIndex = 0;
    private int END_GAME =500;

    private Map<String, Socket> playersList = new LinkedHashMap<>();
    private Map<String, Socket> playersListCanvas = new LinkedHashMap<>();
    private Map<String, Socket> instrucctionsPlayers = new LinkedHashMap<>();
    private Map<String, Integer> playersScore = new LinkedHashMap<>(); // arreglo

    Player[] players = new Player[USERS_MAX];

    private String guessWord ="";

    private DataOutputStream out;
    //private ObjectOutputStream objectOutput;

    Conexion conexion;



    public ServerImplementation() throws RemoteException {
        super();

        try {
            conexion = new Conexion("root","","PinturilloWords");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkNicknameInServer(String name) throws RemoteException {
        if (!playersList.containsKey(name)){

            return true;
        }
        return false;
    }

    @Override
    public boolean connect(String nickname, String ip) throws RemoteException {


        if (playersList.size() < USERS_MAX){

            Socket socketChat = null;
            Socket socketCanvas = null;
            Socket socketInstruccions = null;
            try {
                socketChat = new Socket(ip, CHAT_PORT);
                socketCanvas = new Socket(ip, CANVAS_PORT);
                socketInstruccions = new Socket(ip,GAME_PORT);

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
            instrucctionsPlayers.put(nickname,socketInstruccions);


            players[auxIndex] = new Player(nickname,0);
            auxIndex++;


            if (playersList.size() == USERS_MAX){
                initGame();
            }
            return true;
        }

        if (playersList.size() == USERS_MAX){
            initGame();
        }
        return false;
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("Hello test");
    }

    @Override
    public int messege(boolean onGuess,String message,String name) throws RemoteException {

        if (onGuess){

            if (message.compareTo(guessWord) == 0){
                switchPlayer();
                //todo en el arreglo, buscar al jugador en turno de adivinar y sumarle los puntos.
                for (Player p: players) {
                    if (p.getName().compareTo(name) == 0){
                        p.setScore(points);
                    }
                }
                switchPlayer();
                return points;
            }
        }

        for (String key : playersList.keySet()) {
            Socket socketTemp = playersList.get(key);

            try {
                out = new DataOutputStream(socketTemp.getOutputStream());
                out.writeUTF(name+": "+message);

            } catch (IOException e) {
                System.out.println("Error line: 141 o 142 "+e);
                //e.printStackTrace();
            }
        }

        return 0;

    }

    @Override
    public void sendX_Y(String name, double X, double Y, double lineWidth,String paint) throws RemoteException {
        for (String key : playersListCanvas.keySet()) {
            if (key.compareTo(name) != 0){// todo quitar si no funciona
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

    private void switchPlayer(){
        newWord();
        index++;

        if (index < playersList.size()){
            String name = playerOnDrawingMode(index);
            playersOnGuessingMode(name);
        }else {
            index=0;
        }

    }

    private void initGame(){
        newWord();
        /**
         * Initialize game
         */
        for (String key : instrucctionsPlayers.keySet()) {
            Socket socketTemp = instrucctionsPlayers.get(key);

            try {
                out = new DataOutputStream(socketTemp.getOutputStream());
                out.writeUTF("1");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String name = playerOnDrawingMode(index);
        playersOnGuessingMode(name);

    }

    private String playerOnDrawingMode(int in){

        /**
         * player to draw
         */

        String name = players[in].getName();

        Socket tmp = instrucctionsPlayers.get(name);
        try {
            out = new DataOutputStream(tmp.getOutputStream());
            out.writeUTF("2,"+guessWord);
        } catch (IOException e) {
            System.out.println("Error: "+e);
            //e.printStackTrace();
        }
        return name;

    }

    private void playersOnGuessingMode(String name){
        /**
         * guessMode
         */
        for (String key : instrucctionsPlayers.keySet()) {
            if (key.compareTo(name) != 0){

                Socket socketTemp = instrucctionsPlayers.get(key);

                try {
                    out = new DataOutputStream(socketTemp.getOutputStream());
                    String r ="";
                    for (int loop =0;loop < guessWord.length();loop++) {
                        r= r+"_ ";
                    }

                    out.writeUTF("3,true,"+r);

                } catch (IOException e) {
                    System.out.println("Error: "+e);
                    //e.printStackTrace();
                }
            }

        }
    }

    private void endGame(){

    }
    private void newWord(){
        ResultSet rs = conexion.buscar("Select word from words");
        try {

            int rdn = getRandomNumber(1,10);
            int temp = 1;

            while (temp <= rdn){
                rs.next();
                if(rs != null){
                    temp++;
                }else {
                    rs.previous();
                    break;
                }
            }

            guessWord = rs.getString("word");
            System.out.println("rs: "+ guessWord);


        } catch (SQLException e) {
            System.out.println("Error: "+e);
            //e.printStackTrace();
        }
    }

    private int getRandomNumber(int minimo,int maximo){

        int num=(int)Math.floor(Math.random()*(minimo-(maximo+1))+(maximo));
        return num;
    }

}
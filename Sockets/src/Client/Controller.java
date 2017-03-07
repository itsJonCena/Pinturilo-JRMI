package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interface.ServerInterface;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Alex
 */
public class Controller implements Initializable {

    @FXML Pane canvasPane;
    @FXML Canvas canvas;
    @FXML ColorPicker colorPicker;
    @FXML Slider sliderPensilSize;
    @FXML Label lbtimer;
    @FXML Label lbWord;

    @FXML Pane chatPane;
    @FXML TextField tfChat;
    @FXML TextArea taChat;

    @FXML Pane loginPane;
    @FXML TextField tfnickName;

    @FXML ListView<String> userList;



    private GraphicsContext gc;

    private Registry myreg;
    private ServerInterface server;

    private Socket socketListener;
    private DataInputStream flujodeEntrada;

    ServerSocket socketCanvas;
    private DataInputStream inputStream;
    private ObjectInputStream objectInput;

    Socket gameInstruccionsListener;
    private DataInputStream instruccionsInput;

    private Boolean chatRunning = true;
    private Boolean print = true;
    private boolean draw = false;
    private boolean onGame = true; //thread instruccions
    private boolean chatOnGess;

    private Timer timer = new Timer();


    TextInputDialog dialog = new TextInputDialog();
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert warning = new Alert(Alert.AlertType.WARNING);


    String nickName;
    String serverAddress="";



    @FXML private void connect(){
        nickName = validateWord(tfnickName.getText());
        String ip;

        if (nickName.compareTo("") != 0){

            InetAddress addr = null;
            try {
                addr = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                System.err.println("failed to getLocalHost: line 78");
                e.printStackTrace();
            }

            ip = addr.getHostAddress();
            try {
                if (server.checkNicknameInServer(nickName)){

                    if(server.connect(nickName,ip)){
                        chatPane.setVisible(true);
                        loginPane.setVisible(false);
                    }


                }else {
                    warning.setContentText("Nickname already in use");
                    warning.showAndWait();
                }

            } catch (RemoteException e) {
                System.err.println("failed to connect: line 86 o 92");
                e.printStackTrace();
            }
        }else {
            System.err.println("Faile to connect: return false in line 74");
        }

    }

    @FXML private void sendMessage(){
        String word = validateWord(tfChat.getText());
        tfChat.clear();

        if (word.compareTo("") != 0){
            try {
                server.messege(chatOnGess,word,nickName);

            } catch (RemoteException e) {
                System.err.println("Failed to send message: line 94");
                e.printStackTrace();
            }
        }else {
            error.setTitle("Message Error!! ");
            error.setContentText("Invalid message!!");
            error.show();
        }


    }


    Service<Void> chatThread = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    flujodeEntrada = new DataInputStream(socketListener.getInputStream());
                    while (chatRunning){

                        String mss = flujodeEntrada.readUTF();
                        taChat.appendText(mss+"\n");
                    }
                    return null;
                }
            };
        }
    };

    Service<Void> canvasThread = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    /**
                     *
                     */


                    System.out.println("SocketCanvas: En espera del servidor");
                    Socket socketObjectInput = socketCanvas.accept();
                    System.out.println("SocketCanvas: Ready");
                    GraphicsContext serverGC = canvas.getGraphicsContext2D();
                    //*
                    inputStream = new DataInputStream(socketObjectInput.getInputStream());
                    while (print){
                        String canvasString = inputStream.readUTF();
                        String[] datas = canvasString.split(" ");
                        if (datas.length > 0 && datas != null){
                            //System.out.println("datas[0]"+datas[0]);
                            //System.out.println("datas[1]"+datas[1]);
                            //System.out.println("datas[2]"+datas[2]);
                            //System.out.println("datas[3]"+datas[3]);

                            gc.lineTo(Double.parseDouble(datas[0]),Double.parseDouble(datas[1]));
                            gc.setStroke(Paint.valueOf(datas[3]));
                            gc.setLineWidth(Double.parseDouble(datas[2]));
                            gc.stroke();
                            datas = null;
                        }


                    }
                    return null;
                }
            };
        }
    };

    Service<Void> socketsWait = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Socket
                    /**
                     * Se habilita al usuario como servidor para recibir mensajes de otros usuarios
                     * con el ServerSocket
                     */

                    try {
                        ServerSocket serverSocket = new ServerSocket(2002);
                        System.out.println("Socket en espera del servidor");
                        socketListener = serverSocket.accept();
                        System.out.println("Socket ready");
                        chatThread.restart();


                    } catch (IOException e) {

                        //e.printStackTrace();
                    }

                    return null;
                }
            };
        }
    };

    Service<Void> instruccionsThread = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    /**
                     * Socket para recibir las instrucciones del servidor a lo largo del juego
                     */
                    ServerSocket gameInstruccions = new ServerSocket(2004);
                    System.out.println("Socket de instrucciones: esperando servidor ...");
                    gameInstruccionsListener = gameInstruccions.accept();
                    System.out.println("Socket de instrucciones: Listo ");

                    instruccionsInput = new DataInputStream(gameInstruccionsListener.getInputStream());

                    while (onGame){

                        String inst = instruccionsInput.readUTF();
                        String[] split = inst.split(",");



                        switch (Integer.parseInt(split[0])){
                            case 0:
                                gameOver();
                                break;
                            case 1: // Start game
                                System.out.println(" Start Game");
                                canvasPane.setVisible(true);
                                break;
                            case 2: // draw
                                drawMode(split[1]);
                                break;
                            case 3:// gess word
                                chatOnGess = Boolean.parseBoolean(split[1]);
                                guessMode(split[2]);
                                break;

                        }

                    }

                    return null;
                }
            };
        }
    };

    private void guessMode(String s){
        timer.schedule(timerTask,10,1000);
        System.out.println(nickName+": on Guess mode");
        draw = false;


        lbWord.setText(s);

    }

    private void drawMode(String s){
        timer.schedule(timerTask,10,1000);
        System.out.println(nickName+": on Drawing mode");
        draw = true;
        Platform.runLater(()->lbWord.setText(s));

    }

    private void gameOver(){
        System.out.println("Game over!!");
    }

    private void updateUsersList(){

    }

    @FXML private void onMousePressed(MouseEvent e){
        double X = e.getX(),Y = e.getY();
        if (draw){
            gc.beginPath();
            gc.lineTo(e.getX(),e.getY());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(sliderPensilSize.getValue());
            gc.stroke();

        try {
            server.sendX_Y(nickName,X,Y,sliderPensilSize.getValue(),""+colorPicker.getValue());
        } catch (RemoteException e1) {
            //e1.printStackTrace();
        }
        }

    }

    @FXML private void onMousseDragged(MouseEvent e){
        double X = e.getX(),Y = e.getY();
        if (draw){
            gc.lineTo(e.getX(),e.getY());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(sliderPensilSize.getValue());
            gc.stroke();

        try {
            server.sendX_Y(nickName,X,Y,sliderPensilSize.getValue(),""+colorPicker.getValue().toString());
        } catch (RemoteException e1) {
            //e1.printStackTrace();
        }

        }

    }


    private boolean getServerAddress (){

        dialog.setTitle("Enter the address of the Server please :D");
        dialog.setContentText("Server Address: ");

        Optional<String> result =   dialog.showAndWait();
        if (result.isPresent()){

            serverAddress = result.get();

            //RMI
            try {
                // "192.168.1.73"

                myreg = LocateRegistry.getRegistry(serverAddress,2001);
                //server = new ServerImplementation(); local connection
                server = (ServerInterface) myreg.lookup("Pinturillo");
                //myreg.rebind("Pinturillo",server);

                System.out.println("Server connetion enable ...");

                return true;

            } catch (RemoteException e) {
                System.err.println("Error to connect with server :c");

                //e.printStackTrace();
            } catch (NotBoundException e) {
                //e.printStackTrace();
            }


        }
        return false;

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);


        colorPicker.setValue(Color.BLACK);

        sliderPensilSize.setMin(1);
        sliderPensilSize.setMax(80);
        sliderPensilSize.setShowTickLabels(true);
        sliderPensilSize.setShowTickMarks(true);

        confirmation.setTitle("Failed to connect to the server.");
        confirmation.setContentText("Try again ?");


        boolean accept= false;
        while (accept != true){

            accept = getServerAddress();
            if (accept != true){
                Optional<ButtonType> buttonTypeOptional = confirmation.showAndWait();

                if(buttonTypeOptional.get() == ButtonType.OK){
                    accept = false;
                }else {
                    accept = true;

                    System.exit(1);
                }
            }
        }

        // Initialize the Server Socket
        try {
            socketCanvas = new ServerSocket(2003);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        socketsWait.restart();
        instruccionsThread.restart();
        canvasThread.restart();

    }

    private String  validateWord(String word){
        // Expresion regular para validar solo texto A-Z/a-z
        String regex = "[a-zA-Z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(word);
        matcher.matches();

        if (matcher.find()){
            return word;
        }

        return "";
    }

    private String validateAddress(String address){
        // todo validar la ip con una expresion regular

        System.out.println("Address: "+ address);

        return "";
    }

    private TimerTask timerTask = new TimerTask() {
        int t = 90;

        public void run() {
            if (t > 0){
                Platform.runLater(() -> lbtimer.setText(""+t));
                t--;
            }else{
                this.cancel();
                Platform.runLater( () ->{
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
                    //gc = null;
                    draw = false;
                });
            }

        }
    };


}

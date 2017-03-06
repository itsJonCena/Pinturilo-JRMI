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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interface.ServerInterface;
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

    @FXML Pane chatPane;
    @FXML TextField tfChat;
    @FXML TextArea taChat;

    @FXML Pane loginPane;
    @FXML TextField tfnickName;



    private GraphicsContext gc;

    private Registry myreg;
    private ServerInterface server;

    private Socket socketListener;
    private DataInputStream flujodeEntrada;
    private DataInputStream inputStream;
    private ObjectInputStream objectInput;

    private Boolean chatRunning = true;
    private Boolean print = true;
    private boolean draw = false;

    TextInputDialog dialog = new TextInputDialog();
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert warning = new Alert(Alert.AlertType.WARNING);


    String nickName;
    String serverAddress="";


    ServerSocket socketCanvas;

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

                    canvasPane.setVisible(true);
                    chatPane.setVisible(true);
                    loginPane.setVisible(false);

                    server.connect(nickName,ip);


                    canvasThread.restart(); //Todo mover a donde sera turno de otro jugador


                }else {
                    warning.setContentText("Nickname already in use");
                    warning.showAndWait();
                    //todo Alert Nombre en uso
                }

            } catch (RemoteException e) {
                System.err.println("failed to connect: line 86 o 92");
                e.printStackTrace();
            }
        }else {
            System.err.println("Faile to connect: return false in line 74");
            //Todo Alert con mensaje "entrada no valida"
        }

    }

    @FXML private void sendMessage(){
        String word = validateWord(tfChat.getText());
        tfChat.clear();

        if (word.compareTo("") != 0){
            try {
                server.messege(word,nickName);

            } catch (RemoteException e) {
                System.err.println("Failed to send message: line 94");
                e.printStackTrace();
            }
        }else {
            //todo Alert con error "mensaje no valido"
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
                            System.out.println("datas[0]"+datas[0]);
                            System.out.println("datas[1]"+datas[1]);
                            System.out.println("datas[2]"+datas[2]);
                            System.out.println("datas[3]"+datas[3]);

                            gc.lineTo(Double.parseDouble(datas[0]),Double.parseDouble(datas[1]));
                            gc.setStroke(Paint.valueOf(datas[3]));
                            gc.setLineWidth(Double.parseDouble(datas[2])); //todo convertir a double datas[2]
                            gc.stroke();
                            datas = null;
                        }


                        //System.out.println("Canvas: "+canvasString);
                    }
                    //*/
                    /*objectInput = new ObjectInputStream(socketObjectInput.getInputStream());

                    while (print){
                        //String canvasString = (String) objectInput.readUTF();

                        Map<String,Object> datas = (Map<String, Object>) objectInput.readObject();

                        System.out.println(datas);
                        //System.out.println("Canvas: "+canvasString);
                    }
                    */
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
                        e.printStackTrace();
                    }

                    return null;
                }
            };
        }
    };


    @FXML private void onMousePressed(MouseEvent e){
        double X = e.getX(),Y = e.getY();
        if (draw){
            gc.beginPath();
            gc.lineTo(e.getX(),e.getY());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(sliderPensilSize.getValue());
            gc.stroke();

        try {
            server.sendX_Y(X,Y,sliderPensilSize.getValue(),""+colorPicker.getValue());
        } catch (RemoteException e1) {
            e1.printStackTrace();
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
            server.sendX_Y(X,Y,sliderPensilSize.getValue(),""+colorPicker.getValue().toString());
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        }

    }


    private boolean getServerAddress (){

        dialog.setTitle("Enter the address of the Server please :D");
        dialog.setContentText("Server Address: ");

        Optional<String> result =   dialog.showAndWait();
        if (result.isPresent()){

            //todo validar ip con el metodo validateAddress()
            serverAddress = result.get();

            //RMI
            try {
                // "192.168.1.73"

                myreg = LocateRegistry.getRegistry(serverAddress,2001);
                //server = new ServerImplementation(); local connection
                server = (ServerInterface) myreg.lookup("Pinturillo");
                //myreg.rebind("Pinturillo",server);

                //server.test(); todo talvez lo utilice despues
                System.out.println("Server connetion enable ...");

                return true;

            } catch (RemoteException e) {
                System.err.println("Error to connect with server :c");

                //Todo Alert
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
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
            e.printStackTrace();
        }

        socketsWait.restart();
        //canvasThread.restart(); //Todo mover a donde sera turno de otro jugador


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


}

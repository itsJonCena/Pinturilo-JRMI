package Client;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import ServerBd.ChatSInterface;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.rmi.registry.*;



/**
 * @author Alex
 */
public class Controller implements Initializable {


    @FXML Pane paneRegister;
    @FXML TextField tfNick;
    @FXML Pane paneGame;
    @FXML Label lbWord;
    @FXML Label lbTime;
    @FXML TextArea taChat;
    @FXML TextField tfMssg;
    @FXML Canvas canvasPaint;
    @FXML ListView lvPlayers;
    @FXML ColorPicker colorPicker;
    @FXML Slider slider;


    GraphicsContext gc;

    Boolean Drag = false;
    Timer timer = new Timer();
    String nick;
    ChatSInterface server = null;
    ChatCInterface client = null;
    //ServerBd server;

    @FXML private void getNickname(){

        nick = validateWord(tfNick.getText());
        if (nick.compareTo("") != 0){
            try {
                nick= nick.trim();
                Registry myreg = LocateRegistry.getRegistry("192.168.100.33",2001); // todo Cambiar IP
                server = (ChatSInterface)myreg.lookup("RMIChat");
                client = new ChatCImpl(nick,server);
            }catch(Exception e) {
                System.out.println("[System] ServerBd failed: " + e);
            }



            Drag = true;
            paneRegister.setVisible(false);
            paneGame.setVisible(true);
            tfNick.setText("");



            //Quitar esta parte cuando funciones el servidor
            timer.schedule(timerTask,10,1000);
        }else{
            // todo Alert box
        }

    }

    @FXML private void getMssge(){
        String word = lbWord.getText();
        word = validateWord(word);
        tfMssg.setText("");

        if (word.length() > 0){
            try {
                server.broadcast(nick+" : "+ word);
            }catch(Exception e){
                System.out.println("[System] ServerBd failed: " + e);
            }


        }
    }


    @FXML private void updtePlayersList(){

    }


    /**
     *
     *  Pintar en el canvas en turno
     */
    @FXML private void onMousePressed(MouseEvent e){
        if (Drag){
            gc.beginPath();
            gc.lineTo(e.getX(),e.getY());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(slider.getValue());
            gc.stroke();

            /**
             * Envio de coodenadas al servidor
             */

            System.out.println("x: "+e.getX()+"y: " + e.getY());
        }

    }

    @FXML private void onMousseDragged(MouseEvent e){
        if (Drag){
            gc.lineTo(e.getX(),e.getY());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(slider.getValue());
            gc.stroke();

            /**
             * Envio de coodenadas al servidor
             */

            System.out.println("x: "+e.getX()+"y: " + e.getY());
        }

    }


    /**
     *
     */

     private void onPlayerDraw(Object json){
         int x,y,lineWidth;
         // Color

         gc.lineTo(20,50);
         gc.setStroke(colorPicker.getValue());
         gc.setLineWidth(30);
         gc.stroke();
     }


     @Override
     public void initialize(URL url, ResourceBundle rb) {
         paneGame.setVisible(false);

        try {
            gc = canvasPaint.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0,canvasPaint.getWidth(),canvasPaint.getHeight());

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);


            colorPicker.setValue(Color.BLACK);

            slider.setMin(1);
            slider.setMax(80);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     *
     * @param word Palabra a validar
     * @return la misma palabra si esta escrita correctamente, "" si no lo esta
     */
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
    private void setGc(){
        gc = canvasPaint.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

    }

    private TimerTask timerTask = new TimerTask() {
        int t = 5;
        public void run() {
            if (t > 0){
                Platform.runLater(() -> lbTime.setText(""+t));
                t--;
            }else{
                this.cancel();
                Platform.runLater( () ->{
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0,0,canvasPaint.getWidth(),canvasPaint.getHeight());
                    gc = null;
                    Drag = false;
                });
            }

        }
    };

}

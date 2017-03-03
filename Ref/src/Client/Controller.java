package Client;

import java.net.URL;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.Server;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * @author Alex
 */
public class Controller implements Initializable {

    @FXML Label lbWord;
    @FXML TextArea taChat;
    @FXML TextField tfMssg;
    @FXML Canvas canvasPaint;
    @FXML ListView lvPlayers;
    @FXML ColorPicker colorPicker;
    @FXML Slider slider;

    GraphicsContext gc;

    Boolean Drag = true;

    //Server server;

    @FXML private void getMssge(){
        String word = lbWord.getText();
        word = validateWord(word);
        tfMssg.setText("");

        if (word.length() > 0){
            // Envio al servidor

            /*try {
                System.out.println(server.pWord(word));

            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
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

        try {
            gc = canvasPaint.getGraphicsContext2D();
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

        // server creation

        /*
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            server = new Server();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        */
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

}

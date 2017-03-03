package Client;/**
 * Created by Alex on 01/03/17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PinturilloClient extends Application {

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];

        System.out.println("Server host: "+ host);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Pinturilo_view_1.fxml"));
        primaryStage.setTitle("Pintuillo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

package Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = new Stage(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        primaryStage.setTitle("Pinturillo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //primaryStage.initStyle(StageStyle.UTILITY);

        /*
        stage.setTitle("Pinturillo");
        stage.setScene(new Scene(root));
        stage.show();
        */


        primaryStage.setOnCloseRequest(we -> {
            System.out.println("Exit");
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

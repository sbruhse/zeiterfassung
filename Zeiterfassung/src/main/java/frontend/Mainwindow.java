package frontend;

import backend.Identitaet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by sellmer on 08.06.17.
 */
public class Mainwindow extends Application{

    Button button;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println(Identitaet.getObjectsFromJson("[{name:\"test1\"},{name:\"test2\"}]"));

        primaryStage.setTitle("Zeiterfassung");
        button = new Button();
        button.setText("Click me");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

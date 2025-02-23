package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        java.net.URL fxmlLocation = getClass().getResource("Ui.fxml");
        if (fxmlLocation == null) {
            System.err.println("FXML file not found!");
        } else {
            System.out.println("FXML file loaded: " + fxmlLocation);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Ui.fxml"));

        if (fxmlLocation == null) {
            System.err.println("FXML file not found!");
        } else {
            System.out.println("FXML file loaded: " + fxmlLocation);
        }

        Parent root = loader.load();

        primaryStage.setTitle("File Upload Example");
        primaryStage.setScene(new Scene(root, 900, 600)); // Sesuaikan ukuran window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


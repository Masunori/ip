package mirai.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Main class encapsulates a JavaFX application, in this case the Mirai chatbot application.
 */
public class Main extends Application {
    private Mirai mirai = new Mirai("./data/mirai.txt");

    /**
     * Starts the application.
     * @param stage The stage to mount
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Mirai");
            stage.setResizable(false);
            fxmlLoader.<MainWindow>getController().setMirai(mirai); // inject the Mirai instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

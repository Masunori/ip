package mirai.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * The DialogBox class encapsulates a box of dialog containing the user's image and text message.
 */
public class DialogBox extends HBox {
    @FXML
    protected Label dialog;
    @FXML
    protected ImageView displayPicture;

    /** An enumeration-type that signals to the DialogBox the source of the message */
    protected enum Subject {
        USER, MIRAI
    }

    protected DialogBox(String text, Image img, Subject subject) {
        try {
            String resource = subject == Subject.USER
                    ? "/view/UserDialogBox.fxml"
                    : subject == Subject.MIRAI
                    ? "/view/MiraiDialogBox.fxml"
                    : "/view/DialogBox.fxml";

            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(resource));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        dialog.getStyleClass().add("user-dialog-box-text");

        displayPicture.setImage(img);

        // make the profile image circular
        displayPicture.setClip(new Circle(30, 30, 30));
    }

    /**
     * Creates a dialog box that represents the user's message.
     *
     * @param s The user's message/command
     * @param i The user's profile picture
     * @return a user's dialog component
     */
    public static UserDialogBox getUserDialog(String s, Image i) {
        return new UserDialogBox(s, i);
    }

    /**
     * Creates a dialog box that represents Mirai's message.
     *
     * @param s Mirai's message
     * @param i Mirai's profile picture
     * @return Mirai's dialog component
     */
    public static MiraiDialogBox getMiraiDialog(String s, Image i) {
        return new MiraiDialogBox(s, i);
    }
}


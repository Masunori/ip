package mirai.ui;

import javafx.scene.image.Image;

/**
 * The UserDialogBox encapsulates a dialog box that comes from the user.
 */
public class UserDialogBox extends DialogBox {
    protected UserDialogBox(String text, Image i) {
        super(text, i, Subject.USER);
    }
}

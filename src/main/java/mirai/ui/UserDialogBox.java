package mirai.ui;

import javafx.scene.image.Image;

public class UserDialogBox extends DialogBox{
    protected UserDialogBox(String text, Image i) {
        super(text, i, Subject.USER);
    }
}

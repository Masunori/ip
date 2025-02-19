package mirai.ui;

import javafx.scene.image.Image;

public class MiraiDialogBox extends DialogBox {
    protected MiraiDialogBox(String text, Image i) {
        super(text, i, Subject.MIRAI);
    }
}

package mirai.ui;

import javafx.scene.image.Image;

/**
 * The MiraiDiagloBox encapsulates a dialog box that comes from Mirai.
 */
public class MiraiDialogBox extends DialogBox {
    protected MiraiDialogBox(String text, Image i) {
        super(text, i, Subject.MIRAI);
    }
}

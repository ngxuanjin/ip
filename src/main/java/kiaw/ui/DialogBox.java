package kiaw.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box containing a message and the speaker's image.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box containing the specified text and image.
     *
     * @param text message to display
     * @param image speaker's image
     */
    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(DialogBox.class.getResource(
                            "/view/DialogBox.fxml"));

            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to load DialogBox.fxml.", e);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Flips the dialog box so the image appears on the left.
     */
    private void flip() {
        ObservableList<Node> children =
                FXCollections.observableArrayList(
                        this.getChildren());

        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a dialog box for the user's message.
     *
     * @param text user's message
     * @param image user's image
     * @return user dialog box
     */
    public static DialogBox getUserDialog(
            String text, Image image) {

        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for Kiaw's response.
     *
     * @param text Kiaw's response
     * @param image Kiaw's image
     * @return Kiaw dialog box
     */
    public static DialogBox getKiawDialog(
            String text, Image image) {

        DialogBox dialogBox =
                new DialogBox(text, image);

        dialogBox.flip();
        return dialogBox;
    }
}

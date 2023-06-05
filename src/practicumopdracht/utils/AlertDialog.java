package practicumopdracht.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import practicumopdracht.MainApplication;
import java.util.Objects;

/**
 * JavaFX Custom alert dialog
 * I made this custom alert dialog class because I didn't want to repeat my alert code all over the place with the
 * added chance to make mistakes. Also, to keep my code short and clean.
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class AlertDialog {
    private String type, title, headerText, contentText;
    private Alert alert = new Alert(Alert.AlertType.NONE);
    private DialogPane dialog;
    private static final Image DIALOG_ICON = MainApplication.getAppIcon();
    private static final String CSS = MainApplication.getAppCSS();

    /**
     * Default AlertDialog constructor when nothing is provided
     */
    public AlertDialog() {
        this.headerText = null;
    }

    /**
     * AlertDialog constructor only providing type
     * @param type setAlertType for new Alert
     */
    public AlertDialog(String type) {
        this.type = type;
        this.headerText = null;
        initialize();
    }

    /**
     * AlertDialog constructor without providing type and headerText
     *
     * @param title       setTitle for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String title, String contentText) {
        // Providing default type - If you want to create an empty alert use the class without params/args
        this.type = "INFORMATION";
        this.title = title;
        this.contentText = contentText;
        this.headerText = null;
        initialize();
    }

    /**
     * AlertDialog constructor without providing headerText
     *
     * @param type        setAlertType for new Alert
     * @param title       setTitle for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String type, String title, String contentText) {
        this.type = type;
        this.title = title;
        this.contentText = contentText;
        this.headerText = null;
        initialize();
    }

    /**
     * AlertDialog constructor
     *
     * @param type        setAlertType for new Alert
     * @param title       setTitle for new Alert
     * @param headerText  setHeaderText for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String type, String title, String headerText, String contentText) {
        this.type = type;
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        initialize();
    }

    private void initialize() {
        switch (type) {
            case "CONFIRMATION":
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setGraphic(new ImageView(new Image(
                        new ResourceLoader().getFileFromResourceAsStream("images/emoji/thinking-face.gif"),
                        100, 100, true, true)));
                break;
            case "WARNING":
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setGraphic(new ImageView(new Image(
                        new ResourceLoader().getFileFromResourceAsStream("images/emoji/weary.gif"),
                        100, 100, true, true)));
                break;
            case "ERROR":
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setGraphic(new ImageView(new Image(
                        new ResourceLoader().getFileFromResourceAsStream("images/emoji/dizzy-face.gif"),
                        100, 100, true, true)));
                break;
            case "NONE":
                break;
            default:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setGraphic(new ImageView(new Image(
                        new ResourceLoader().getFileFromResourceAsStream("images/emoji/slightly-happy.gif"),
                        100, 100, true, true)));
        }
    }

    public void setGraphic(final ImageView IMAGE_VIEW) {
        alert.setGraphic(IMAGE_VIEW);
    }

    private void initializeText() {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
    }

    public void show() {
        try {
            initializeText();
        } catch (Exception e) {
            System.err.println("Alert dialog text couldn't be initialized. Please check if you have at least provided " +
                    "a title and contentText");
        }

        try {
            /* Add button to AlertType NONE otherwise alert window can't be closed
             * See link for explanation: https://stackoverflow.com/questions/32402131/alert-type-none-closing-javafx
             */
            if (Objects.equals(type, "NONE")) {
                alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            }

            // Set the CSS file and add a CSS class to the dialog
            dialog = alert.getDialogPane();
            dialog.getStylesheets().add(CSS);

            // To set the icon of the dialog window
            Stage stage = (Stage) dialog.getScene().getWindow();
            stage.getIcons().add(DIALOG_ICON);

            // Show the dialog
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Alert dialog couldn't be created. Something went wrong");
        }
    }

    // Help my fellow programmers so that they can use both :)
    public void showAndWait() {
        show();
    }

    public ButtonType getResult() {
        return alert.getResult();
    }

    @Override
    public String toString() {
        if (type == null || title == null || contentText == null) {
            return "You need to at least provide type, title and contentText";
        }
        if (headerText == null) {
            return String.format("""
                    Alert type: %s
                    Title: %s
                    Content text: %s
                    """, type, title, contentText);
        }
        return String.format("""
                Alert type: %s
                Title: %s
                Header text: %s
                Content text: %s
                """, type, title, headerText, contentText);
    }

    // Getter and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
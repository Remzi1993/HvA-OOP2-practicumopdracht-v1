package practicumopdracht.utils;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Minimal reproducible example (MRE) - Example of a simple JavaFX preloader class.
 * @author Remzi Cavdar - ict@remzi.info - <a href="https://github.com/Remzi1993">@Remzi1993</a>
 */
public class Preloader extends javafx.application.Preloader {
    private ProgressBar progressBar;
    private Stage stage;
    private static final boolean DEBUG = false;

    private Scene getScene() {
        progressBar = new ProgressBar();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(progressBar);
        return new Scene(borderPane, 300, 150);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.getIcons().add(new Image(new ResourceLoader().getInputStream("images/icon.png")));
        stage.setTitle("Laden van applicatie");
        stage.setResizable(false);
        stage.setScene(getScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        progressBar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (DEBUG) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stage.hide();
        }
    }
}
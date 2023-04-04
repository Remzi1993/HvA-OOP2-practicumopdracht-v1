package practicumopdracht;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import practicumopdracht.controllers.Controller;
import practicumopdracht.controllers.PersonController;
import practicumopdracht.data.*;
import practicumopdracht.utils.ResourceLoader;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static practicumopdracht.Main.*;

/**
 * Main class for the JavaFX application.
 * This class is responsible for starting the JavaFX application and starting the default view.
 * Switching between views is done by the controllers. Whereby the controllers use the switchController method in this
 * class.
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class MainApplication extends Application {
    private static final String TITLE = String.format("Practicumopdracht OOP2 - %s - %d", getStudentName(),
            getStudentNumber());
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // Visual bounds - usable area of the screen (no task bars etc.)
    private static final Rectangle2D VISUAL_BOUNDS = Screen.getPrimary().getVisualBounds();
    // Resources - see README.md if this fails
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();
    private static final Image APP_ICON = new Image(RESOURCE_LOADER.getInputStream("images/icon.png"));
    private static final String APP_CSS = RESOURCE_LOADER.getResourceDir("/style.css");
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static Stage stage;
    private static Scene scene;
    // PersonDAO - MasterDAO
    private static PersonDAO personDAO;
    // TicketDAO - DetailDAO
    private static TicketDAO ticketDAO;
    // For DAOs - the directory the application data is stored in
    private static final String APP_DATA_DIRECTORY = "App_data";
    // Global debug setting for getting (extra) error and/or success messages in the console. Turn off for production.
    public static final boolean DEBUG = false;
    public static final String APP_VERSION = "1.0.8";

    @Override
    public void start(Stage stage) {
        if (!launchedFromMain) {
            System.err.println("You must start this application from the Main class, not the MainApplication class!");
            System.exit(1337);
            return;
        }

        if (DEBUG) {
            System.out.printf("Resources directory: %s%nDefault charset: %s%nDefault locale: %s%n",
                    RESOURCE_LOADER.getResourceDir("/"), Charset.defaultCharset().displayName(),
                    Locale.getDefault().getDisplayName());
        }

        MainApplication.stage = stage;
        // Stage settings
        stage.getIcons().add(APP_ICON);
        stage.setTitle(TITLE);

        // Min width and height
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);

        personDAO = new BinaryPersonDAO();
        ticketDAO = new ObjectTicketDAO();
        try {
            personDAO.load();
            ticketDAO.load();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load data!");
            Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Something went wrong while loading data!");
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

        // Start/default controller with default associated view
        switchController(new PersonController(null));
    }

    /**
     * switchController for switching between views.
     * @param controller needs a controller as a parameter/argument and can't be null
     */
    public static void switchController(Controller controller) {
        // Check if there is a controller and if not throw an exception
        if (controller == null) {
            throw new IllegalArgumentException("switchController() expects a Controller as an argument and can't be null!");
        }

        // Check if there is a scene and if not create a new scene with the initial width and height
        if (scene == null) {
            // Initial width and height when starting the application for the first time
            scene = new Scene(controller.getView().getRoot(), WIDTH, HEIGHT);
        } else {
            /*
             * The current width and height of the scene is used when switching views, therefore the window of
             * the app will not resize and stay the same size when switching views.
             */
            scene = new Scene(controller.getView().getRoot(), scene.getWidth(), scene.getHeight());
        }

        // Apply stylesheet to the scene
        scene.getStylesheets().add(APP_CSS);

        // Apply scene to stage
        stage.setScene(scene);

        // Show the stage (App window)
        stage.show();
    }

    // Getters
    public static String getAppTitle() {
        return TITLE;
    }

    public static Image getAppIcon() {
        return APP_ICON;
    }

    public static String getAppCSS() {
        return APP_CSS;
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }

    public static PersonDAO getPersonDAO() {
        return personDAO;
    }

    public static TicketDAO getTicketDAO() {
        return ticketDAO;
    }

    public static double getMaxWidthScreen() {
        return VISUAL_BOUNDS.getWidth();
    }

    public static String getAppDataDirectory() {
        return APP_DATA_DIRECTORY;
    }
}
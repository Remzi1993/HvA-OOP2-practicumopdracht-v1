package practicumopdracht;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import practicumopdracht.controllers.Controller;
import practicumopdracht.controllers.PersonController;
import practicumopdracht.data.PersonDAO;
import practicumopdracht.data.TextPersonDAO;
import practicumopdracht.data.TextTicketDAO;
import practicumopdracht.data.TicketDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static practicumopdracht.Main.*;

public class MainApplication extends Application {
    private static final String TITLE = String.format("Practicumopdracht OOP2 - %s - %d", getStudentName(),
            getStudentNumber());
    private static final int WIDTH = 720;
    private static final int HEIGHT = 560;
    // Windows 11 window overflow bug
    private static final double WIDTH_OVERFLOW = 15.3043823242188;
    private static final double HEIGHT_OVERFLOW = 37.5652465820312;
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
    // Global debug setting for getting (extra) error and/or success messages in the console
    public static final boolean DEBUG = true;

    @Override
    public void start(Stage stage) {
        if (!launchedFromMain) {
            System.err.println("You must start this application from the Main class, not the MainApplication class!");
            System.exit(1337);
            return;
        }

        if (DEBUG) {
            System.out.println("Resources directory: " + RESOURCE_LOADER.getResourceDir("/"));
            System.out.println("Default charset: " + Charset.defaultCharset().displayName());
        }

        MainApplication.stage = stage;
        // Stage settings
        stage.getIcons().add(APP_ICON);
        stage.setTitle(TITLE);

        // Min width and height
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);

        personDAO = new TextPersonDAO();
        ticketDAO = new TextTicketDAO();
        try {
            personDAO.load();
            ticketDAO.load();
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load data!");
            stage.close();
            Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Something went wrong while loading data!");
        }

        // Start/default controller with default associated view
        switchController(new PersonController());
    }

    /**
     * switchController for switching scenes
     *
     * @param controller needs a controller as a parameter/argument and can't be null
     */
    public static void switchController(@NotNull Controller controller) {
        // Debugging window sizes
        // System.out.println("Width: " + stage.getWidth() + " Height: " + stage.getHeight());

        /* (Temporary) Windows 11 window overflow bugfix
         * There is a window overflow around 15 is added to the width and around 37 to the height.
         * This might be a Windows 11 window border problem. Windows 11 has a lot of issues with their rounded
         * corners. Everyone knows the infamous "Rounded corners issue", basically Windows 11 rounded corners are
         * not calculated (where to drag the mouse pointer) correctly because when resizing the window you need to drag
         * into the "invisible space" where the square corner used to be.
         *
         * This quick and dirty fix is discovered by Remzi Cavdar after intensive debugging and luck (around 5 hours).
         * Use a double for the exact extra width and height:
         * Width: 15.3043823242188
         * Height: 37.5652465820312
         *
         * Disclaimer. I use Windows 11 for programming and testing. I used Linux before that, but I couldn't use
         * certain specific programs, so I switched to Windows 11. That means that this dirty fix could cause (potential)
         * issues on Windows 10 and older, macOS and/or Linux. Maybe someone else can extend this dirty fix with a
         * better check(s) and clarify what's exactly going on.
         *
         * Debugging
         * You can see the extra added with and height with this:
         * System.out.println("Width: " + stage.getWidth() + " Height: " + stage.getHeight());
         * You can see the difference in width and height if you give your scene a width and height and switch scenes
         * With the debugging you will see the extra added width and height.
         */
        if (scene == null) {
            // Scene with initial width and height
            scene = new Scene(controller.getView().getRoot(), WIDTH, HEIGHT);
        } else if (Objects.equals(System.getProperty("os.name"), "Windows 11")) {
            /* Windows 11 gets special treatment from me
             This will be used to remove the extra width and height
             */
            double width = stage.getWidth() - WIDTH_OVERFLOW;
            double height = stage.getHeight() - HEIGHT_OVERFLOW;
            scene = new Scene(controller.getView().getRoot(), width, height);
        } else {
            // Everybody else plays nicely with the window width and height (hopefully)
            scene = new Scene(controller.getView().getRoot(), stage.getWidth(), stage.getHeight());
        }

        // Apply stylesheet
        scene.getStylesheets().add(APP_CSS);

        // Get scene from controller who shows the specific view
        stage.setScene(scene);

        // Show stage with scene and when changing (navigating in app) scene show the other scene.
        stage.show();
    }

    // Getters
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
}
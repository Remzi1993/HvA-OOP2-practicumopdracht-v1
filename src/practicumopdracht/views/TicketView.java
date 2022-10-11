package practicumopdracht.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import practicumopdracht.MainApplication;
import practicumopdracht.models.Person;
import practicumopdracht.models.Ticket;

/**
 * TicketView - DetailView
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class TicketView extends View {
    private Label labelBelongsTo, labelStartDate, labelEndDate, labelCost, labelCheckedIn;
    private TextField txtFieldCost;
    private DatePicker datePickerStartDate, datePickerEndDate;
    private CheckBox checkBoxCheckedIn;
    private ComboBox<Person> comboBoxBelongsTo;
    private Button saveButton, newButton, deleteButton, switchButton;
    private ListView<Ticket> listview;
    private MenuItem menuItemSave, menuItemLoad, menuItemClose;

    @Override
    protected Parent initializeView() {
        // Root
        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.getStyleClass().add("root-container");
        // Create the menu bar.
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menu-bar");
        // Create the File menu.
        Menu fileMenu = new Menu("Bestand");
        menuItemSave = new MenuItem("Opslaan");
        menuItemLoad = new MenuItem("Laden");
        menuItemClose = new MenuItem("Afsluiten");
        fileMenu.getItems().addAll(menuItemSave, menuItemLoad, new SeparatorMenuItem(), menuItemClose);
        menuBar.getMenus().add(fileMenu);

        // VBox parent container
        VBox vboxContainer = new VBox();
        vboxContainer.getStyleClass().add("vbox-container");
        // Padding
        vboxContainer.setPadding(new Insets(10));
        vboxContainer.setSpacing(10);

        // GridPane container
        GridPane gridpane = new GridPane();
        gridpane.getStyleClass().add("bg-1");
        // GridPane Column settings
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        gridpane.getColumnConstraints().addAll(col1,col2);

        // GridPane horizontal and vertical Spacing - margins between columns
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        // Labels and inputs and controls
        // Belongs to
        labelBelongsTo = new Label("Persoon:");

        comboBoxBelongsTo = new ComboBox<>();
        gridpane.add(labelBelongsTo, 0, 0);
        gridpane.add(comboBoxBelongsTo, 1, 0);

        // Start date
        labelStartDate = new Label("Datum vanaf:");
        datePickerStartDate = new DatePicker();
        gridpane.add(labelStartDate, 0, 1);
        gridpane.add(datePickerStartDate, 1, 1);

        // End date
        labelEndDate = new Label("Datum tot:");
        datePickerEndDate = new DatePicker();
        gridpane.add(labelEndDate, 0, 2);
        gridpane.add(datePickerEndDate, 1, 2);

        // Cost
        labelCost = new Label("Kosten:");
        txtFieldCost = new TextField();
        gridpane.add(labelCost, 0, 4);
        gridpane.add(txtFieldCost, 1, 4);

        // Checkbox checkedIn
        labelCheckedIn = new Label("Checked in:");
        checkBoxCheckedIn = new CheckBox("(verplicht)");
        gridpane.add(labelCheckedIn, 0, 5);
        gridpane.add(checkBoxCheckedIn, 1, 5);

        // Save button
        HBox hboxSaveButton = new HBox();
        saveButton = new Button("Opslaan");
        hboxSaveButton.getChildren().add(saveButton);
        hboxSaveButton.setAlignment(Pos.CENTER);
        hboxSaveButton.getStyleClass().add("bg-2");
        saveButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        HBox.setHgrow(saveButton, Priority.ALWAYS);

        // ListView
        HBox hboxListview = new HBox();
        listview = new ListView<>();
        hboxListview.getChildren().add(listview);
        hboxListview.setAlignment(Pos.CENTER);
        hboxListview.getStyleClass().add("bg-3");
        HBox.setHgrow(listview, Priority.ALWAYS);

        // Bottom area for buttons
        HBox hboxBottomButtons = new HBox();
        newButton = new Button("Nieuw");
        deleteButton = new Button("Verwijderen");
        switchButton = new Button("Terug naar overzicht personen");
        hboxBottomButtons.getChildren().addAll(newButton, deleteButton, switchButton);
        hboxBottomButtons.getStyleClass().add("bg-4");
        hboxBottomButtons.setSpacing(10);
        newButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        deleteButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        switchButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        HBox.setHgrow(newButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);
        HBox.setHgrow(switchButton, Priority.ALWAYS);

        // VBox container
        vboxContainer.getChildren().addAll(gridpane, hboxSaveButton, hboxListview, hboxBottomButtons);
        // Root
        rootBorderPane.setTop(menuBar);
        rootBorderPane.setCenter(vboxContainer);
        return rootBorderPane;
    }

    // All getters
    public MenuItem getMenuItemSave() {
        return menuItemSave;
    }

    public MenuItem getMenuItemLoad() {
        return menuItemLoad;
    }

    public MenuItem getMenuItemClose() {
        return menuItemClose;
    }

    public TextField getTxtFieldCost() {
        return txtFieldCost;
    }

    public DatePicker getDatePickerStartDate() {
        return datePickerStartDate;
    }

    public DatePicker getDatePickerEndDate() {
        return datePickerEndDate;
    }

    public CheckBox getCheckBoxCheckedIn() {
        return checkBoxCheckedIn;
    }

    public ComboBox<Person> getComboBoxBelongsTo() {
        return comboBoxBelongsTo;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getNewButton() {
        return newButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public ListView<Ticket> getListView() {
        return listview;
    }
}
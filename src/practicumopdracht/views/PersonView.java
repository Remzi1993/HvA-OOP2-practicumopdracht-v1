package practicumopdracht.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import practicumopdracht.MainApplication;
import practicumopdracht.TextFieldLimiter;
import practicumopdracht.models.Person;

/**
 * PersonView - MasterView
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class PersonView extends View {
    private Label labelName, labelSex, labelBirthdate, labelBirthplace, labelNationality, labelBSN, labelDocumentNumber;
    private TextField txtFieldName, txtFieldBirthplace, txtFieldNationality, txtFieldBSN, txtFieldDocumentNumber;
    private DatePicker datePickerBirthdate;
    private ComboBox<String> comboBoxSex;
    private Button saveButton, newButton, deleteButton, switchButton;
    private ListView<Person> listView;
    private static final int MAX_LENGTH_BSN = 9;
    private MenuItem menuItemSave, menuItemLoad, menuItemClose;

    public PersonView() {
        new TextFieldLimiter(txtFieldBSN, MAX_LENGTH_BSN);
    }

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
        // Name
        labelName = new Label("Voor- en achternaam:");
        txtFieldName = new TextField();
        gridpane.add(labelName, 0, 0);
        gridpane.add(txtFieldName, 1, 0);

        // Sex
        labelSex = new Label("Geslacht:");
        comboBoxSex = new ComboBox<>();
        comboBoxSex.getItems().add("Man");
        comboBoxSex.getItems().add("Vrouw");
        gridpane.add(labelSex, 0, 1);
        gridpane.add(comboBoxSex, 1, 1);

        // Birthdate
        labelBirthdate = new Label("Geboortedatum:");
        datePickerBirthdate = new DatePicker();
        gridpane.add(labelBirthdate, 0, 2);
        gridpane.add(datePickerBirthdate, 1, 2);

        // Birthplace
        labelBirthplace = new Label("Geboorteplaats:");
        txtFieldBirthplace = new TextField();
        gridpane.add(labelBirthplace, 0, 3);
        gridpane.add(txtFieldBirthplace, 1, 3);

        // Nationality
        labelNationality = new Label("Nationaliteit:");
        txtFieldNationality = new TextField();
        gridpane.add(labelNationality, 0, 4);
        gridpane.add(txtFieldNationality, 1, 4);

        // BSN
        labelBSN = new Label("BSN:");
        txtFieldBSN = new TextField();
        gridpane.add(labelBSN, 0, 5);
        gridpane.add(txtFieldBSN, 1, 5);

        // Passport or ID document number
        labelDocumentNumber = new Label("Documentnummer:");
        txtFieldDocumentNumber = new TextField();
        gridpane.add(labelDocumentNumber, 0, 6);
        gridpane.add(txtFieldDocumentNumber, 1, 6);

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
        listView = new ListView<>();
        hboxListview.getChildren().add(listView);
        hboxListview.setAlignment(Pos.CENTER);
        hboxListview.getStyleClass().add("bg-3");
        HBox.setHgrow(listView, Priority.ALWAYS);

        // Bottom area for buttons
        HBox hboxBottomButtons = new HBox();
        newButton = new Button("Nieuw");
        deleteButton = new Button("Verwijderen");
        switchButton = new Button("Tickets");
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

    // All getters and setters
    public MenuItem getMenuItemSave() {
        return menuItemSave;
    }

    public MenuItem getMenuItemLoad() {
        return menuItemLoad;
    }

    public MenuItem getMenuItemClose() {
        return menuItemClose;
    }

    public Label getLabelName() {
        return labelName;
    }

    public void setLabelName(Label labelName) {
        this.labelName = labelName;
    }

    public Label getLabelSex() {
        return labelSex;
    }

    public void setLabelSex(Label labelSex) {
        this.labelSex = labelSex;
    }

    public Label getLabelBirthdate() {
        return labelBirthdate;
    }

    public void setLabelBirthdate(Label labelBirthdate) {
        this.labelBirthdate = labelBirthdate;
    }

    public Label getLabelBirthplace() {
        return labelBirthplace;
    }

    public void setLabelBirthplace(Label labelBirthplace) {
        this.labelBirthplace = labelBirthplace;
    }

    public Label getLabelNationality() {
        return labelNationality;
    }

    public void setLabelNationality(Label labelNationality) {
        this.labelNationality = labelNationality;
    }

    public Label getLabelBSN() {
        return labelBSN;
    }

    public void setLabelBSN(Label labelBSN) {
        this.labelBSN = labelBSN;
    }

    public Label getLabelDocumentNumber() {
        return labelDocumentNumber;
    }

    public void setLabelDocumentNumber(Label labelDocumentNumber) {
        this.labelDocumentNumber = labelDocumentNumber;
    }

    public TextField getTxtFieldName() {
        return txtFieldName;
    }

    public void setTxtFieldName(TextField txtFieldName) {
        this.txtFieldName = txtFieldName;
    }

    public TextField getTxtFieldBirthplace() {
        return txtFieldBirthplace;
    }

    public void setTxtFieldBirthplace(TextField txtFieldBirthplace) {
        this.txtFieldBirthplace = txtFieldBirthplace;
    }

    public TextField getTxtFieldNationality() {
        return txtFieldNationality;
    }

    public void setTxtFieldNationality(TextField txtFieldNationality) {
        this.txtFieldNationality = txtFieldNationality;
    }

    public TextField getTxtFieldBSN() {
        return txtFieldBSN;
    }

    public void setTxtFieldBSN(TextField txtFieldBSN) {
        this.txtFieldBSN = txtFieldBSN;
    }

    public TextField getTxtFieldDocumentNumber() {
        return txtFieldDocumentNumber;
    }

    public void setTxtFieldDocumentNumber(TextField txtFieldDocumentNumber) {
        this.txtFieldDocumentNumber = txtFieldDocumentNumber;
    }

    public DatePicker getDatePickerBirthdate() {
        return datePickerBirthdate;
    }

    public void setDatePickerBirthdate(DatePicker datePickerBirthdate) {
        this.datePickerBirthdate = datePickerBirthdate;
    }

    public ComboBox<String> getComboBoxSex() {
        return comboBoxSex;
    }

    public void setComboBoxSex(ComboBox<String> comboBoxSex) {
        this.comboBoxSex = comboBoxSex;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getNewButton() {
        return newButton;
    }

    public void setNewButton(Button newButton) {
        this.newButton = newButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(Button switchButton) {
        this.switchButton = switchButton;
    }

    public ListView<Person> getListView() {
        return listView;
    }

    public void setListView(ListView<Person> listView) {
        this.listView = listView;
    }
}

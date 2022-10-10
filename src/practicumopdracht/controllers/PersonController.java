package practicumopdracht.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import practicumopdracht.*;
import practicumopdracht.data.PersonDAO;
import practicumopdracht.data.TicketDAO;
import practicumopdracht.models.Person;
import practicumopdracht.views.PersonView;
import practicumopdracht.views.View;
import java.io.FileNotFoundException;
import static practicumopdracht.MainApplication.*;

/**
 * PersonController - MasterController
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class PersonController extends Controller {
    private PersonView view;
    private Person person;
    private PersonDAO personDAO;
    private TicketDAO ticketDAO;
    private AlertDialog alert;
    // All the inputs from the view
    private TextField fullName, birthplace, nationality, BSN, documentNumber;
    private ComboBox<String> sex;
    private DatePicker birthdate;
    private ListView<Person> listView;
    private Object[] data;
    private static InputHandler inputHandler;
    private ObservableList<Person> observableListPersons;
    private DatePickerConverter datePickerConverter;

    public PersonController() {
        personDAO = getPersonDAO();
        ticketDAO = getTicketDAO();
        view = new PersonView();
        inputHandler = new InputHandler();

        view.getListView().getSelectionModel().selectedItemProperty().addListener((
                observableValue, oldPerson, newPerson) -> {
            if (newPerson != null) {
                getInputDataFromView();
                fullName.setText(String.valueOf(newPerson.getName()));
                sex.setValue(newPerson.getSex());
                birthdate.setValue(newPerson.getBirthdate());
                birthplace.setText(String.valueOf(newPerson.getBirthplace()));
                nationality.setText(String.valueOf(newPerson.getNationality()));
                BSN.setText(String.valueOf(newPerson.getBSN()));
                documentNumber.setText(String.valueOf(newPerson.getDocumentNumber()));
            }
        });

        // Menubar items
        view.getMenuItemSave().setOnAction(this::handleMenuSaveButton);
        view.getMenuItemLoad().setOnAction(this::handleMenuLoadButton);
        view.getMenuItemClose().setOnAction(actionEvent -> Platform.exit());

        // Buttons
        view.getSaveButton().setOnAction(this::handleSaveButton);
        view.getNewButton().setOnAction(this::handleNewButton);
        view.getDeleteButton().setOnAction(this::handleDeleteButton);
        view.getSwitchButton().setOnAction(this::handleSwitchButton);

        // ObservableList which controls the ListView
        observableListPersons = FXCollections.observableArrayList(personDAO.getAll());
        view.getListView().setItems(observableListPersons);

        // Datepicker
        birthdate = view.getDatePickerBirthdate();
        // Create the DateConverter
        datePickerConverter = new DatePickerConverter(getDateFormat());
        // Add the Converter to the DatePicker
        birthdate.setConverter(datePickerConverter);
        // Set the Date in the Prompt
        birthdate.setPromptText(getDateFormat().toUpperCase());

        birthdate.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    // Set typed text to DatePicker value
                    birthdate.setValue(birthdate.getConverter().fromString(birthdate.getEditor().getText()));
                } catch (Exception e) {
                    // For wrong input return old value
                    birthdate.getEditor().setText(birthdate.getConverter().toString(birthdate.getValue()));
                }
            }
        });
    }

    @Override
    public View getView() {
        return view;
    }

    private void getInputDataFromView() {
        fullName = view.getTxtFieldName();
        sex = view.getComboBoxSex();
        birthdate = view.getDatePickerBirthdate();
        birthplace = view.getTxtFieldBirthplace();
        nationality = view.getTxtFieldNationality();
        BSN = view.getTxtFieldBSN();
        documentNumber = view.getTxtFieldDocumentNumber();
        listView = view.getListView();
        data = new Object[]{fullName, sex, birthdate, birthplace, nationality, BSN, documentNumber, listView};
    }

    private void handleMenuSaveButton(ActionEvent event) {
        personDAO.save();
        ticketDAO.save();
    }

    private void handleMenuLoadButton(ActionEvent event) {
        try {
            // Load data from data sources
            personDAO.load();
            ticketDAO.load();

            // Update the observable list with the new data
            observableListPersons.setAll(personDAO.getAll());
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load data!");
            Platform.exit();
            System.exit(0);
        }
    }

    private void handleSaveButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: save");
        }
        getInputDataFromView();

        // String builder
        StringBuilder sb = new StringBuilder("De volgende invoerveld(en) heeft u niet of onjuist ingevuld:");

        // Reset and clear warnings
        inputHandler.setTotalErrorValues(0);
        inputHandler.clearWarnings(data);

        IsNumeric isNumeric = new IsNumeric();

        if (DEBUG) {
            System.out.printf("""
                                                
                            Input values:
                            Full name: %s
                            Sex: %s
                            Birthdate: %s
                            Birthplace: %s
                            Nationality: %s
                            BSN: %s
                            Document number: %s
                                                
                            """, fullName.getText().strip(), sex.getSelectionModel().getSelectedItem(), birthdate.getValue(),
                    birthplace.getText().strip(), nationality.getText().strip(), BSN.getText().strip(),
                    documentNumber.getText().strip());
        }

        if (fullName.getText().isBlank() || sex.getSelectionModel().isEmpty() || birthplace.getText().isBlank() ||
                nationality.getText().isBlank() || BSN.getText().isBlank() || !isNumeric.isInt(BSN.getText()) ||
                documentNumber.getText().isBlank() || birthdate.getValue() == null) {

            /* Show warnings if inputs are empty and/or have incorrect values - empty and/or incorrect inputs get a
            red border */
            inputHandler.checkValues(data);
            inputHandler.checkIsInt(BSN);

            if (fullName.getText().isBlank()) {
                sb.append("\nU heeft geen naam ingevuld.");
            }
            if (sex.getSelectionModel().isEmpty()) {
                sb.append("\nU heeft geen geslacht geselecteerd.");
            }
            if (birthdate.getValue() == null) {
                sb.append("\nU heeft geen of een onjuist geboortedatum ingevuld.");
            }
            if (birthplace.getText().isBlank()) {
                sb.append("\nU heeft geen geboorteplaats ingevuld.");
            }
            if (nationality.getText().isBlank()) {
                sb.append("\nU heeft geen nationaliteit ingevuld.");
            }
            if (!isNumeric.isInt(BSN.getText())) {
                if (BSN.getText().isBlank()) {
                    sb.append("\nU heeft geen BSN ingevuld.");
                } else {
                    sb.append("\nU heeft een onjuist nummer ingevoerd bij het BSN invoerveld.");
                    sb.append("\nU kunt alleen gehele nummers invoeren in het BSN invoerveld.");
                }
            }
            if (documentNumber.getText().isBlank()) {
                sb.append("\nU heeft geen documentnummer ingevuld.");
            }

            alert = new AlertDialog("WARNING");
            if (inputHandler.getTotalErrorValues() > 1) {
                alert.setTitle("Bepaalde invoervelden zijn niet of onjuist ingevuld");
            } else {
                alert.setTitle("Er is een invoerveld niet of onjuist ingevuld");
            }
            sb.append("\n\nProbeer het opnieuw.");
            alert.setContentText(String.valueOf(sb));
            alert.show();
            return;
        }

        // Get selected person from listView and compare it
        person = view.getListView().getSelectionModel().getSelectedItem();

        alert = new AlertDialog("INFORMATION");
        if (person == null) {
            // Create new person - Name, Sex, Birthdate, Birthplace, Nationality, BSN, Document number
            person = new Person(fullName.getText(), sex.getSelectionModel().getSelectedItem(),
                    birthdate.getValue(), birthplace.getText(), nationality.getText(),
                    Integer.parseInt(BSN.getText()), documentNumber.getText());
            // Update ListView
            observableListPersons.add(person);
            // Alert title and text
            alert.setTitle("Persoon aangemaakt");
            alert.setContentText("De persoon is aangemaakt.");
        } else {
            // Update person
            person.setName(fullName.getText());
            person.setSex(sex.getSelectionModel().getSelectedItem());
            person.setBirthdate(birthdate.getValue());
            person.setBirthplace(birthplace.getText());
            person.setNationality(nationality.getText());
            person.setBSN(Integer.parseInt(BSN.getText()));
            person.setDocumentNumber(documentNumber.getText());

            // Alert title and text
            alert.setTitle("Persoon bijgewerkt");
            alert.setContentText("De persoon is bijgewerkt.");
        }

        // Update DAO
        personDAO.addOrUpdate(person);
        // Show confirmation
        alert.show();
        // Clear everything after successful save
        inputHandler.clearValues(data, true);
        if (DEBUG) {
            System.out.println("End action: save");
        }
    }

    private void handleNewButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: new/clear");
        }

        alert = new AlertDialog("CONFIRMATION", "Persoon aanmaken",
                "Wilt u een nieuw persoon aanmaken?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            getInputDataFromView();
            if (data == null) {
                System.err.println("Data is null");
                return;
            }
            // Clear everything
            inputHandler.clearValues(data, true);
            // Clear warnings
            inputHandler.clearWarnings(data);
        }

        if (DEBUG) {
            System.out.println("End action: new/clear");
        }
    }

    private void handleDeleteButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: delete");
        }
        person = view.getListView().getSelectionModel().getSelectedItem();

        // If nothing is selected in the listview
        if (person == null) {
            alert = new AlertDialog("Selecteer persoon",
                    "Selecteer een persoon die u wilt verwijderen.");
            alert.show();
            return;
        }

        alert = new AlertDialog("CONFIRMATION", "Persoon verwijderen",
                "Weet u zeker dat u deze persoon wilt verwijderen?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            // Update DAO
            personDAO.remove(person);
            ticketDAO.removeAllFor(person);
            // Update ListView
            observableListPersons.remove(person);
        }

        if (DEBUG) {
            System.out.println("End action: delete");
        }
    }

    private void handleSwitchButton(ActionEvent event) {
        person = view.getListView().getSelectionModel().getSelectedItem();
        if (person == null) {
            alert = new AlertDialog("WARNING", "Persoon selecteren",
                    "U dient een persoon te selecteren voordat u verder kan gaan naar het tickets-overzicht.");
            alert.show();
            return;
        }
        switchController(new TicketController(person));
    }
}
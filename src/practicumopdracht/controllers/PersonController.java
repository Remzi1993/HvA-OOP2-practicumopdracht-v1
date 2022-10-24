package practicumopdracht.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import practicumopdracht.comparators.NameComparator;
import practicumopdracht.data.PersonDAO;
import practicumopdracht.data.TicketDAO;
import practicumopdracht.models.Person;
import practicumopdracht.utils.*;
import practicumopdracht.views.AboutView;
import practicumopdracht.views.PersonView;
import practicumopdracht.views.View;
import java.io.FileNotFoundException;
import static practicumopdracht.MainApplication.*;

/**
 * PersonController - MasterController
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class PersonController extends Controller {
    private PersonView view;
    private Person person;
    private PersonDAO personDAO;
    private TicketDAO ticketDAO;
    private AlertDialog alert;
    // All the inputs from the view
    private TextField fullName, birthplace, nationality, SSN, documentNumber;
    private ComboBox<String> sex;
    private DatePicker birthdate;
    private ListView<Person> listView;
    private Object[] data;
    private ObservableList<Person> observableListPersons;
    private boolean personNameAscending;
    private static InputHandler inputHandler;
    private Person selectedPerson;
    private Stage secondStage;

    public PersonController(final Person SELECTED_PERSON) {
        selectedPerson = SELECTED_PERSON;
        personDAO = getPersonDAO();
        ticketDAO = getTicketDAO();
        view = new PersonView();
        inputHandler = new InputHandler();

        // By default, these buttons are disabled until a person is selected in the listViw
        view.getNewButton().setDisable(true);
        view.getDeleteButton().setDisable(true);
        view.getSwitchButton().setDisable(true);

        view.getListView().getSelectionModel().selectedItemProperty().addListener((
                observableValue, oldPerson, newPerson) -> {
            if (newPerson != null) {
                // This is needed to remember the selected person in the listview, so we can give this to the TicketController
                selectedPerson = newPerson;

                getInputDataFromView();
                fullName.setText(String.valueOf(newPerson.getName()));
                sex.setValue(newPerson.getSex());
                birthdate.setValue(newPerson.getBirthdate());
                birthplace.setText(String.valueOf(newPerson.getBirthplace()));
                nationality.setText(String.valueOf(newPerson.getNationality()));
                SSN.setText(String.valueOf(newPerson.getSSN()));
                documentNumber.setText(String.valueOf(newPerson.getDocumentNumber()));

                // Enable these buttons if a person is selected in the listView
                view.getNewButton().setDisable(false);
                view.getDeleteButton().setDisable(false);
                view.getSwitchButton().setDisable(false);

                // Reset and clear warnings when a person is selected from the listView
                inputHandler.setTotalErrorValues(0);
                inputHandler.clearWarnings(data);
            } else {
                selectedPerson = null;
                // Disable these buttons if no person is selected in the listView
                view.getNewButton().setDisable(true);
                view.getDeleteButton().setDisable(true);
                view.getSwitchButton().setDisable(true);
            }
        });

        // Menubar items
        view.getMenuItemSave().setOnAction(this::handleMenuSaveButton);
        view.getMenuItemLoad().setOnAction(this::handleMenuLoadButton);
        view.getMenuItemClose().setOnAction(this::handleMenuCloseButton);
        view.getMenuItemSortAZ().setOnAction(this::handleMenuSortAZButton);
        view.getMenuItemSortZA().setOnAction(this::handleMenuSortZAButton);
        view.getMenuItemAbout().setOnAction(this::handleMenuAboutButton);

        // Buttons
        view.getSaveButton().setOnAction(this::handleSaveButton);
        view.getNewButton().setOnAction(this::handleNewButton);
        view.getDeleteButton().setOnAction(this::handleDeleteButton);
        view.getSwitchButton().setOnAction(this::handleSwitchButton);

        // ObservableList which will automatically update the ListView if changed
        observableListPersons = FXCollections.observableArrayList(personDAO.getAll());
        // Default sorting from A to Z
        observableListPersons.sort(new NameComparator(true));
        personNameAscending = true;
        // Set the observableList to the ListView
        view.getListView().setItems(observableListPersons);

        // Remember the selected person if users go back from the tickets overview
        if(selectedPerson != null) {
            view.getListView().getSelectionModel().select(selectedPerson);
        }

        // Datepicker
        birthdate = view.getDatePickerBirthdate();
        // Create the dateConverter
        DatePickerConverter datePickerConverter = new DatePickerConverter(getDateFormat());
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
        SSN = view.getTxtFieldSSN();
        documentNumber = view.getTxtFieldDocumentNumber();
        listView = view.getListView();
        data = new Object[]{fullName, sex, birthdate, birthplace, nationality, SSN, documentNumber, listView};
    }

    private void handleMenuSaveButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Data opslaan",
                "Wilt u de data opslaan?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            if(personDAO.save() && ticketDAO.save()) {
                menuAlert(true, "Data succesvol opgeslagen",
                        "De data is succesvol opgeslagen.");
            } else {
                menuAlert(false, "Error bij opslaan data!",
                        "Data is niet opgeslagen.\nEr is een fout opgetreden tijdens het opslaan van de data.");
            }
        }
    }

    private void handleMenuLoadButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Data laden",
                "Wilt u de data laden?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            try {
                // Load data from data sources and confirm if successful
                if(personDAO.load() && ticketDAO.load()) {
                    menuAlert(true, "Data succesvol opgehaald",
                            "De data is succesvol opgehaald.");
                } else {
                    menuAlert(false, "Error bij laden data!",
                            "Er is een fout opgetreden tijdens het laden van de data.");
                }

                // Update the observable list with the new data
                observableListPersons.setAll(personDAO.getAll());
            } catch (FileNotFoundException e) {
                System.err.println("Couldn't load data!");
                Platform.exit();
                System.exit(0);
            }
        }
    }

    private void menuAlert(boolean result, String title, String contextText) {
        if(result) {
            alert = new AlertDialog("INFORMATION", title,
                    contextText);
            alert.show();
        } else {
            alert = new AlertDialog("ERROR", title,
                    contextText);
            alert.show();
        }
    }

    private void handleMenuCloseButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Afsluiten",
                "Wilt u de app afsluiten?");
        alert.setGraphic(new ImageView(new Image(
                new ResourceLoader().getInputStream("images/emoji/screaming.gif"),
                100, 100, true, true)));
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    private void handleMenuSortAZButton(ActionEvent event) {
        observableListPersons.sort(new NameComparator(true));
        personNameAscending = true;
    }

    private void handleMenuSortZAButton(ActionEvent event) {
        observableListPersons.sort(new NameComparator(false));
        personNameAscending = false;
    }

    private void handleMenuAboutButton(ActionEvent event) {
        if (secondStage != null) {
            secondStage.close();
        }
        secondStage = new Stage();
        secondStage.getIcons().add(getAppIcon());
        secondStage.setTitle("Informatie & contactgegevens - versie: " + APP_VERSION);
        secondStage.setResizable(false);
        secondStage.initModality(Modality.APPLICATION_MODAL);
        secondStage.setAlwaysOnTop(true);
        secondStage.setScene(new AboutView().getScene());
        secondStage.show();
    }

    private void handleSaveButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: save");
        }
        getInputDataFromView();

        // String builder
        StringBuilder sb = new StringBuilder("Invoerveld(en) niet of onjuist ingevuld:");

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
                            SSN: %s
                            Document number: %s
                                                
                            """, fullName.getText().strip(), sex.getSelectionModel().getSelectedItem(),
                    birthdate.getValue(), birthplace.getText().strip(), nationality.getText().strip(),
                    SSN.getText().strip(), documentNumber.getText().strip());
        }

        if (fullName.getText().isBlank() || sex.getSelectionModel().isEmpty() || birthplace.getText().isBlank() ||
                nationality.getText().isBlank() || SSN.getText().isBlank() || !isNumeric.isInt(SSN.getText()) ||
                documentNumber.getText().isBlank() || birthdate.getValue() == null) {

            /* Show warnings if inputs are empty and/or have incorrect values - empty and/or incorrect inputs get a
            red border */
            inputHandler.checkValues(data);
            inputHandler.checkIsInt(SSN);

            if (fullName.getText().isBlank()) {
                sb.append("\nGeen naam ingevuld.");
            }
            if (sex.getSelectionModel().isEmpty()) {
                sb.append("\nGeen geslacht geselecteerd.");
            }
            if (birthdate.getValue() == null) {
                sb.append("\nGeen/onjuist geboortedatum ingevuld.");
            }
            if (birthplace.getText().isBlank()) {
                sb.append("\nGeen geboorteplaats ingevuld.");
            }
            if (nationality.getText().isBlank()) {
                sb.append("\nGeen nationaliteit ingevuld.");
            }
            if (!isNumeric.isInt(SSN.getText())) {
                if (SSN.getText().isBlank()) {
                    sb.append("\nGeen BSN ingevuld.");
                } else {
                    sb.append("\nOnjuist nummer ingevoerd bij het BSN.");
                    sb.append("\nAlleen gehele getallen invoeren bij het BSN.");
                }
            }
            if (documentNumber.getText().isBlank()) {
                sb.append("\nGeen documentnummer ingevuld.");
            }

            alert = new AlertDialog("WARNING");
            if (inputHandler.getTotalErrorValues() > 1) {
                alert.setTitle("Invoervelden zijn niet/onjuist ingevuld");
            } else {
                alert.setTitle("Een invoerveld niet/onjuist ingevuld");
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
            // Create new person - Name, Sex, Birthdate, Birthplace, Nationality, SSN, Document number
            person = new Person(fullName.getText(), sex.getSelectionModel().getSelectedItem(),
                    birthdate.getValue(), birthplace.getText(), nationality.getText(),
                    Integer.parseInt(SSN.getText()), documentNumber.getText());
            // Update ListView
            observableListPersons.add(person);
            // Alert title and text
            alert.setTitle("Persoon toegevoegd");
            alert.setContentText("Persoon is toegevoegd.");
        } else {
            // Update person
            person.setName(fullName.getText());
            person.setSex(sex.getSelectionModel().getSelectedItem());
            person.setBirthdate(birthdate.getValue());
            person.setBirthplace(birthplace.getText());
            person.setNationality(nationality.getText());
            person.setSSN(Integer.parseInt(SSN.getText()));
            person.setDocumentNumber(documentNumber.getText());

            // Alert title and text
            alert.setTitle("Persoon bijgewerkt");
            alert.setContentText("Persoon is bijgewerkt.");
        }

        // Update DAO
        personDAO.addOrUpdate(person);
        // Sort list
        observableListPersons.sort(new NameComparator(personNameAscending));
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
                "Wilt u een nieuwe persoon aanmaken?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            getInputDataFromView();
            if (data == null) {
                if (DEBUG) {
                    System.err.println("Data is null");
                }
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
                "Wilt u deze persoon verwijderen?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            // Update DAO
            personDAO.remove(person);
            ticketDAO.removeAllFor(person);
            // Update ListView
            observableListPersons.remove(person);
            // Clear listView selection and inputs
            view.getListView().getSelectionModel().clearSelection();
            inputHandler.clearValues(data, true);
        }

        if (DEBUG) {
            System.out.println("End action: delete");
        }
    }

    private void handleSwitchButton(ActionEvent event) {
        switchController(new TicketController(selectedPerson, personNameAscending));
    }
}
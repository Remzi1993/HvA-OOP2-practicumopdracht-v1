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
import practicumopdracht.models.Ticket;
import practicumopdracht.views.TicketView;
import practicumopdracht.views.View;
import java.io.FileNotFoundException;
import static practicumopdracht.MainApplication.*;

/**
 * TicketController - DetailController
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class TicketController extends Controller {
    private TicketView view;
    private Ticket ticket;
    private TicketDAO ticketDAO;
    private PersonDAO personDAO;
    private AlertDialog alert;
    // All the inputs from the view
    private ComboBox<Person> belongsTo;
    private DatePicker startDate, endDate;
    private TextField cost;
    private CheckBox checkedIn;
    private ListView<Ticket> listView;
    private Object[] data;
    private ObservableList<Person> observableListPersons;
    private ObservableList<Ticket> observableListTickets;
    private static InputHandler inputHandler;

    public TicketController(Person person) {
        ticketDAO = getTicketDAO();
        personDAO = getPersonDAO();
        view = new TicketView();
        inputHandler = new InputHandler();

        // Menubar items
        view.getMenuItemSave().setOnAction(this::handleMenuSaveButton);
        view.getMenuItemLoad().setOnAction(this::handleMenuLoadButton);
        view.getMenuItemClose().setOnAction(this::handleMenuCloseButton);

        // Buttons
        view.getSaveButton().setOnAction(this::handleSaveButton);
        view.getNewButton().setOnAction(this::handleNewButton);
        view.getDeleteButton().setOnAction(this::handleDeleteButton);
        view.getSwitchButton().setOnAction(this::handleSwitchButton);

        // All the persons in the DAO
        observableListPersons = FXCollections.observableArrayList(personDAO.getAll());
        // Set the items in the belongs to combobox
        view.getComboBoxBelongsTo().setItems(observableListPersons);
        // Selected person from the previous screen
        view.getComboBoxBelongsTo().getSelectionModel().select(person);
        // Set the items in the listview
        observableListTickets = FXCollections.observableArrayList(ticketDAO.getAllFor(
                view.getComboBoxBelongsTo().getSelectionModel().getSelectedItem()
        ));
        view.getListView().setItems(observableListTickets);

        view.getListView().getSelectionModel().selectedItemProperty().addListener((
                observableValue, oldTicket, newTicket) -> {
            if (newTicket != null) {
                getInputDataFromView();
                belongsTo.getSelectionModel().select(newTicket.getBelongsTo());
                startDate.setValue(newTicket.getStartDate());
                endDate.setValue(newTicket.getEndDate());
                cost.setText(String.valueOf(newTicket.getCost()));
                checkedIn.selectedProperty().set(newTicket.isCheckedIn());
            }
        });

        view.getComboBoxBelongsTo().getSelectionModel().selectedItemProperty().addListener((
                observableValue, oldBelongsTo, newBelongsTo) -> {
            if (newBelongsTo != null) {
                observableListTickets.setAll(ticketDAO.getAllFor(newBelongsTo));
            }
        });

        // Datepickers
        startDate = view.getDatePickerStartDate();
        endDate = view.getDatePickerEndDate();
        // Create the DateConverter
        DatePickerConverter datePickerConverter = new DatePickerConverter(getDateFormat());
        // Add the Converter to the DatePicker
        startDate.setConverter(datePickerConverter);
        endDate.setConverter(datePickerConverter);
        // Set the Date in the Prompt
        startDate.setPromptText(getDateFormat().toUpperCase());
        endDate.setPromptText(getDateFormat().toUpperCase());

        startDate.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    // Set typed text to DatePicker value
                    startDate.setValue(startDate.getConverter().fromString(startDate.getEditor().getText()));
                } catch (Exception e) {
                    // For wrong input return old value
                    startDate.getEditor().setText(startDate.getConverter().toString(startDate.getValue()));
                }
            }
        });

        endDate.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    // Set typed text to DatePicker value
                    endDate.setValue(endDate.getConverter().fromString(endDate.getEditor().getText()));
                } catch (Exception e) {
                    // For wrong input return old value
                    endDate.getEditor().setText(endDate.getConverter().toString(endDate.getValue()));
                }
            }
        });
    }

    @Override
    public View getView() {
        return view;
    }

    private void getInputDataFromView() {
        belongsTo = view.getComboBoxBelongsTo();
        startDate = view.getDatePickerStartDate();
        endDate = view.getDatePickerEndDate();
        cost = view.getTxtFieldCost();
        checkedIn = view.getCheckBoxCheckedIn();
        listView = view.getListView();
        data = new Object[]{belongsTo, startDate, endDate, cost, checkedIn, listView};
    }

    private void handleMenuSaveButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Data opslaan in het bestand",
                "Weet u zeker dat u de data wilt opslaan in het bestand?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            if(personDAO.save() && ticketDAO.save()) {
                menuAlert(true, "Data is succesvol opgeslagen",
                        "De data is succesvol opgeslagen in het bestand.");
            } else {
                menuAlert(false, "Error bij opslaan data!",
                        "Er is een fout opgetreden tijdens het opslaan van de data naar het bestand.");
            }
        }
    }

    private void handleMenuLoadButton(ActionEvent event) {
        try {
            view.getComboBoxBelongsTo().getSelectionModel().clearSelection();
            view.getListView().getSelectionModel().clearSelection();

            // Load data from data sources and confirm if successful
            if(personDAO.load() && ticketDAO.load()) {
                menuAlert(true, "Data is succesvol ingeladen",
                        """
                                De data is succesvol geladen van het bestand.
                                Selecteer een persoon om de tickets te zien.""");
            } else {
                menuAlert(false, "Error bij laden data!",
                        "Er is een fout opgetreden tijdens het laden van de data van het bestand.");
            }

            // Update the observable list with the new data
            observableListPersons.setAll(personDAO.getAll());
            observableListTickets.setAll(ticketDAO.getAllFor(
                    view.getComboBoxBelongsTo().getSelectionModel().getSelectedItem()
            ));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load data!");
            Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Something went wrong!");
            Platform.exit();
            System.exit(0);
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
                "Weet u zeker dat u de app wilt afsluiten?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
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

        if (belongsTo.getSelectionModel().isEmpty() || startDate.getValue() == null || endDate.getValue() == null ||
                cost.getText().isBlank() || !checkedIn.isSelected() || !isNumeric.isDouble(cost.getText())) {

            /* Show warnings if inputs are empty and/or have incorrect values - empty and/or incorrect inputs get a
            red border */
            inputHandler.checkValues(data);
            inputHandler.checkIsDouble(cost);

            if (belongsTo.getSelectionModel().isEmpty()) {
                sb.append("\nU heeft geen persoon geselecteerd.");
            }
            if (startDate.getValue() == null) {
                sb.append("\nU heeft geen datum vanaf ingevuld.");
            }
            if (endDate.getValue() == null) {
                sb.append("\nU heeft geen datum tot ingevuld.");
            }
            if (cost.getText().isBlank()) {
                sb.append("\nU heeft geen kosten ingevuld.");
            }
            if (!isNumeric.isDouble(cost.getText())) {
                sb.append("\nU heeft een onjuist nummer ingevoerd bij het kosten invoerveld.");
                sb.append("\nU kunt alleen nummers invoeren in het kosten invoerveld.");
            }
            if (!checkedIn.isSelected()) {
                sb.append("\nU heeft de checkbox niet aangevinkt.");
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

        // Get selected ticket from listView and compare it
        ticket = view.getListView().getSelectionModel().getSelectedItem();

        alert = new AlertDialog("INFORMATION");
        if (ticket == null) {
            // Create new ticket
            ticket = new Ticket(belongsTo.getSelectionModel().getSelectedItem(), startDate.getValue(),
                    endDate.getValue(), Double.parseDouble(cost.getText()), checkedIn.isSelected());

            // Update ListView
            observableListTickets.add(ticket);
            // Alert title and text
            alert.setTitle("Ticket aangemaakt");
            alert.setContentText("Ticket is aangemaakt.");
        } else {
            // Update ticket
            ticket.setBelongsTo(belongsTo.getSelectionModel().getSelectedItem());
            ticket.setStartDate(startDate.getValue());
            ticket.setEndDate(endDate.getValue());
            ticket.setCost(Double.parseDouble(cost.getText()));
            ticket.setCheckedIn(checkedIn.isSelected());

            // Alert title and text
            alert.setTitle("Ticket bijgewerkt");
            alert.setContentText("Ticket is bijgewerkt.");
        }

        // Update DAO
        ticketDAO.addOrUpdate(ticket);
        // Show confirmation
        alert.show();
        // Clear everything after successful save
        inputHandler.clearValues(data, false);
        if (DEBUG) {
            System.out.println("End action: save");
        }
    }

    private void handleNewButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: new/clear");
        }

        alert = new AlertDialog("CONFIRMATION", "Ticket aanmaken",
                "Wilt u een nieuwe ticket aanmaken?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            getInputDataFromView();
            if (data == null) {
                System.err.println("Data is null");
                return;
            }
            // Clear everything
            inputHandler.clearValues(data, false);
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
        ticket = view.getListView().getSelectionModel().getSelectedItem();

        // If nothing is selected in the listview
        if (ticket == null) {
            alert = new AlertDialog("Selecteer ticket",
                    "Selecteer een ticket die u wilt verwijderen.");
            alert.show();
            return;
        }

        alert = new AlertDialog("CONFIRMATION", "Ticket verwijderen",
                "Weet u zeker dat u deze ticket wilt verwijderen?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            // Update DAO
            ticketDAO.remove(ticket);
            // Update ListView
            observableListTickets.remove(ticket);
        }

        if (DEBUG) {
            System.out.println("End action: delete");
        }
    }

    private void handleSwitchButton(ActionEvent event) {
        switchController(new PersonController());
    }
}
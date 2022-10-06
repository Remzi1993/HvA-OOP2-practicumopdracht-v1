package practicumopdracht.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import practicumopdracht.AlertDialog;
import practicumopdracht.InputHandler;
import practicumopdracht.IsNumeric;
import practicumopdracht.MainApplication;
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
 *
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
    private static InputHandler inputHandler;
    private ObservableList<Person> observableListPersons;
    private ObservableList<Ticket> observableListTickets;

    public TicketController(Person person) {
        ticketDAO = getTicketDAO();
        personDAO = getPersonDAO();
        view = new TicketView();
        inputHandler = new InputHandler();

        view.getListView().getSelectionModel().selectedItemProperty().addListener((
                observableValue, oldTicket, newTicket) -> {
            if (newTicket != null) {
                getInputDataFromView();
                belongsTo.setValue(newTicket.getBelongsTo());
                startDate.setValue(newTicket.getStartDate());
                endDate.setValue(newTicket.getEndDate());
                cost.setText(String.valueOf(newTicket.getCost()));
                checkedIn.selectedProperty().set(newTicket.isCheckedIn());
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
        // Belongs to combobox bind to listView
        view.getComboBoxBelongsTo().setOnAction(this::handleUpdateListView);

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
        checkedIn = view.getCheckBoxcheckedIn();
        listView = view.getListView();
        data = new Object[]{belongsTo, startDate, endDate, cost, checkedIn, listView};
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
            observableListTickets.setAll(ticketDAO.getAll());
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load data!");
            Platform.exit();
            System.exit(0);
        }
    }

    private void handleUpdateListView(ActionEvent event) {
        observableListTickets.setAll(ticketDAO.getAllFor(
                view.getComboBoxBelongsTo().getSelectionModel().getSelectedItem()
        ));
        inputHandler.clearValues(data, false);
    }

    private void handleSaveButton(ActionEvent event) {
        if (MainApplication.DEBUG) {
            System.out.println("Start action: save");
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
            System.out.println("Update ticket");
            // Update ticket
            ticket.setBelongsTo(belongsTo.getSelectionModel().getSelectedItem());
            ticket.setStartDate(startDate.getValue());
            ticket.setEndDate(endDate.getValue());
            ticket.setCost(Double.parseDouble(cost.getText()));
            ticket.setcheckedIn(checkedIn.isSelected());

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
        if (MainApplication.DEBUG) {
            System.out.println("End action: save");
        }
    }

    private void handleNewButton(ActionEvent event) {
        if (MainApplication.DEBUG) {
            System.out.println("Start action: new/clear");
        }

        alert = new AlertDialog("CONFIRMATION", "Ticket aanmaken",
                "Wilt u een nieuwe ticket aanmaken?");
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            // Clear everything
            inputHandler.clearValues(data, false);
            // Clear warnings
            inputHandler.clearWarnings(data);
        }

        if (MainApplication.DEBUG) {
            System.out.println("End action: new/clear");
        }
    }

    private void handleDeleteButton(ActionEvent event) {
        if (MainApplication.DEBUG) {
            System.out.println("Start action: delete");
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

        if (MainApplication.DEBUG) {
            System.out.println("End action: delete");
        }
    }

    private void handleSwitchButton(ActionEvent event) {
        switchController(new PersonController());
    }
}
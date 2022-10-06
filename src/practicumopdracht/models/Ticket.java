package practicumopdracht.models;

import practicumopdracht.data.PersonDAO;
import java.time.LocalDate;
import java.util.Locale;

import static practicumopdracht.MainApplication.getDateFormat;
import static practicumopdracht.MainApplication.getPersonDAO;

/**
 * Ticket model - DetailModel
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class Ticket {
    private Person belongsTo;
    private PersonDAO personDAO = getPersonDAO();
    private LocalDate startDate;
    private LocalDate endDate;
    private double cost;
    private boolean checkedIn;

    public Ticket(Person belongsTo, LocalDate startDate, LocalDate endDate, double cost, boolean checkedIn) {
        this.belongsTo = belongsTo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.checkedIn = checkedIn;
    }

    @Override
    public String toString() {
        return String.format("Ticket: [Datum vanaf: %s - Datum tot: %s - Kosten: %.2f - Checked in: %b]",
                getDateFormat().format(startDate), getDateFormat().format(endDate), cost, checkedIn);
    }

    public String toStringTextFile() {
        // belongsTo, startDate, endDate, cost, checkedIn
        // Locale is set to US because we use comma as a separator
        return String.format(Locale.US, "%d,%s,%s,%.2f,%b", personDAO.getIdFor(belongsTo),
                getDateFormat().format(startDate), getDateFormat().format(endDate), cost, checkedIn);
    }

    // Getters and setters
    public Person getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Person belongsTo) {
        this.belongsTo = belongsTo;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setcheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
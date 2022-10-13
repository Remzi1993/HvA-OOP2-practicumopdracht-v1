package practicumopdracht.models;

import java.io.Serializable;
import java.time.LocalDate;
import static practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * Ticket model - DetailModel
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class Ticket implements Serializable {
    private Person belongsTo;
    private LocalDate startDate, endDate;
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
                getDateTimeFormatter().format(startDate), getDateTimeFormatter().format(endDate), cost,
                checkedIn);
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

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
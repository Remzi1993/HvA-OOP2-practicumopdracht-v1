package practicumopdracht.data;

import practicumopdracht.models.Ticket;
import java.time.LocalDate;
import static practicumopdracht.MainApplication.getDateTimeFormatter;
import static practicumopdracht.MainApplication.getPersonDAO;

/**
 * DummyTicketDAO - DummyDetailDAO
 * This is a dummy DAO class that is used to test the application.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class DummyTicketDAO extends TicketDAO {
    @Override
    public boolean load() {
        tickets.add(new Ticket(getPersonDAO().getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(1),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(2),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(3),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(4),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(getPersonDAO().getById(5),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        return true;
    }

    @Override
    public boolean save() {
        return false;
    }
}
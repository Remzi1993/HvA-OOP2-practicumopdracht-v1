package practicumopdracht.data;

import practicumopdracht.models.Ticket;
import java.time.LocalDate;
import static practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * DummyTicketDAO - DummyDetailDAO
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class DummyTicketDAO extends TicketDAO {
    @Override
    public boolean load() {
        tickets.add(new Ticket(personDAO.getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(0),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(1),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(2),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(3),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(4),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        tickets.add(new Ticket(personDAO.getById(5),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()),
                LocalDate.parse("23-07-2022", getDateTimeFormatter()), 446, true));
        return true;
    }

    @Override
    public boolean save() {
        return false;
    }
}
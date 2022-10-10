package practicumopdracht;

import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static practicumopdracht.MainApplication.DEBUG;

public class DatePickerConverter extends StringConverter<LocalDate> {
    // The Date Time Converter
    private DateTimeFormatter dateTimeFormatter;

    public DatePickerConverter(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * @param string the {@code String} to convert String to a LocalDate
     * @return LocalDate
     */
    @Override
    public LocalDate fromString(String string) {
        if (string.isBlank()) {
            if (DEBUG) {
                System.err.println("Empty input in DatePicker field");
            }
            return null;
        }

        try {
            return LocalDate.parse(string, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            if (DEBUG) {
                System.err.println("Error parsing LocalDate: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            if (DEBUG) {
                System.err.println("Something serious went wrong!");
            }
            return null;
        }
    }

    /**
     * @param localDate the object of type {@code T} to convert LocalDate to a String
     * @return String
     */
    @Override
    public String toString(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return dateTimeFormatter.format(localDate);
    }
}
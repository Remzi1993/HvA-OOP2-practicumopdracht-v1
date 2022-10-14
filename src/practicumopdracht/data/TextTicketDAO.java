package practicumopdracht.data;

import practicumopdracht.models.Ticket;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import static practicumopdracht.MainApplication.*;

/**
 * TextTicketDAO - TextDetailDAO
 * This is a DAO class which handles loading and saving data to a text file for the Ticket model.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class TextTicketDAO extends TicketDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Tickets.txt";
    private static final File DIRECTORY = new File(DIRECTORY_NAME);
    private static final File FILE = new File(DIRECTORY_NAME + File.separator + FILE_NAME);
    private static final String UTF8_BOM = "\uFEFF";
    private static final String SEPARATOR = ";";

    @Override
    public boolean load() {
        if (DEBUG) {
            System.out.printf("%n******** Debug info%n* App data directory: %s%n* Full path to file: %s%n********%n%n",
                    DIRECTORY.getAbsolutePath(), FILE.getAbsolutePath());
        }

        /*
         * 1. Check if the directory exists
         * 2. If not, create the directory
         * 3. Check if the file exists
         * 4. If not, create the file
         */
        try {
            if (DIRECTORY.mkdir()) {
                if (DEBUG) {
                    System.out.println("Directory created: " + DIRECTORY_NAME);
                }
            } else {
                if (DEBUG) {
                    System.out.println("Directory already exists: " + DIRECTORY_NAME);
                }
            }

            if (FILE.createNewFile()) {
                if (DEBUG) {
                    System.out.println("File created: " + FILE_NAME);
                }
            } else {
                if (DEBUG) {
                    System.out.println("File already exists: " + FILE_NAME);
                }
            }
        } catch (SecurityException e) {
            System.err.println("SecurityException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("An error occurred.");
        } catch (Exception e) {
            System.err.println("Something went wrong. An unexpected error occurred.");
            e.printStackTrace();
        }

        if (DEBUG) {
            System.out.println("Loading data: " + FILE_NAME);
        }

        try (
                FileReader fileReader = new FileReader(FILE, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            // Clear the list
            tickets.clear();

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.startsWith(UTF8_BOM)) {
                    line = line.substring(1);
                }
                String[] values = line.split(SEPARATOR);

                if (DEBUG) {
                    System.out.println(Arrays.toString(values));
                }



                try {
                    // belongsTo, destination, startDate, endDate, cost, checkedIn, description
                    tickets.add(new Ticket(
                            getPersonDAO().getById(Integer.parseInt(values[0])),
                            values[1],
                            LocalDate.parse(values[2], getDateTimeFormatter()),
                            LocalDate.parse(values[3], getDateTimeFormatter()),
                            Double.parseDouble(values[4]),
                            Boolean.parseBoolean(values[5]),
                            values[6]
                    ));
                } catch (DateTimeException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Something went wrong: " + e.getMessage());
                    e.printStackTrace();
                }

                // read next line
                line = bufferedReader.readLine();
            }

            // Successful load
            if (DEBUG) {
                System.out.println("Loading complete: " + FILE_NAME);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Unsuccessful load
        return false;
    }

    @Override
    public boolean save() {
        if (DEBUG) {
            System.out.println("\nSaving data: " + FILE_NAME);
        }

        try (
                FileWriter fileWriter = new FileWriter(FILE, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (Ticket ticket : tickets) {
                // belongsTo, destination, startDate, endDate, cost, checkedIn, description
                bufferedWriter.append(String.format(
                        Locale.US, "%d%s%s%s%s%s%s%s%.2f%s%b%s%s",
                        getPersonDAO().getIdFor(ticket.getBelongsTo()),
                        SEPARATOR,
                        ticket.getDestination(),
                        SEPARATOR,
                        getDateTimeFormatter().format(ticket.getStartDate()),
                        SEPARATOR,
                        getDateTimeFormatter().format(ticket.getEndDate()),
                        SEPARATOR,
                        ticket.getCost(),
                        SEPARATOR,
                        ticket.isCheckedIn(),
                        SEPARATOR,
                        ticket.getDescription()
                ));
                bufferedWriter.newLine();
            }

            // Successful save
            if (DEBUG) {
                System.out.println("Saving data complete: " + FILE_NAME);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Something went wrong while saving the file!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Unsuccessful save
        return false;
    }
}
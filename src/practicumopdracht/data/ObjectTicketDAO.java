package practicumopdracht.data;

import practicumopdracht.models.Ticket;
import java.io.*;
import static practicumopdracht.MainApplication.*;

public class ObjectTicketDAO extends TicketDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Tickets.obj";
    private static final File DIRECTORY = new File(DIRECTORY_NAME);
    private static final File FILE = new File(DIRECTORY_NAME + File.separator + FILE_NAME);

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
                FileInputStream fileInputStream = new FileInputStream(FILE);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ) {
            if(objectInputStream.available() == 0) {
                if (DEBUG) {
                    System.out.println("File is empty");
                }
                return true;
            }

            // Clear the list
            tickets.clear();

            int arraySize = objectInputStream.readInt();
            if (arraySize == 0) {
                return false;
            }

            for (int i = 0; i < arraySize; i++) {
                // belongsTo, destination, startDate, endDate, cost, checkedIn, description
                int belongsTo = objectInputStream.readInt();
                Ticket ticket = (Ticket) objectInputStream.readObject();
                ticket.setBelongsTo(getPersonDAO().getById(belongsTo));
                tickets.add(ticket);
            }

            // Successful load
            if (DEBUG) {
                System.out.println("Loading complete: " + FILE_NAME);
            }
            return true;
        } catch (EOFException e) {
            /* When the file is empty, an EOFException is thrown. This happens when an empty file is created during load().
             * This is not an error, so we can safely ignore it.
             *
             * Signals that an end of file or end of stream has been reached unexpectedly during input.
             * This exception is mainly used by data input streams to signal end of stream.
             * This exception is harmless and can be ignored.
             */
            if (DEBUG) {
                System.out.println("File is empty");
            }
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
                FileOutputStream fileOutputStream = new FileOutputStream(FILE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            objectOutputStream.writeInt(tickets.size());

            for (Ticket ticket : tickets) {
                // destination, startDate, endDate, cost, checkedIn, description
                Ticket ticketObj = new Ticket(
                        ticket.getDestination(),
                        ticket.getStartDate(),
                        ticket.getEndDate(),
                        ticket.getCost(),
                        ticket.isCheckedIn(),
                        ticket.getDescription()
                );
                // belongsTo is saved as a person id based on the array index of the persons list in the personDAO
                objectOutputStream.writeInt(getPersonDAO().getIdFor(ticket.getBelongsTo()));
                // Write the Ticket object to a file (serialization)
                objectOutputStream.writeObject(ticketObj);
            }

            // Successful save
            if (DEBUG) {
                System.out.println("Saving data complete: " + FILE_NAME);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Something went wrong while saving the file!" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Unsuccessful save
        return false;
    }
}
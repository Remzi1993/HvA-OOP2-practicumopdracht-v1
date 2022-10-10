package practicumopdracht.data;

import practicumopdracht.models.Person;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import static practicumopdracht.MainApplication.DEBUG;
import static practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * TextPersonDAO - TextMasterDAO
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class TextPersonDAO extends PersonDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Persons.txt";
    private static final String UTF8_BOM = "\uFEFF";

    @Override
    public boolean load() {
        File directory = new File(DIRECTORY_NAME);
        File file = new File(DIRECTORY_NAME + "/" + FILE_NAME);

        if (DEBUG) {
            System.out.println("\nApp data directory: " + directory.getAbsolutePath());
        }

        /*
         * 1. Check if the directory exists
         * 2. If not, create the directory
         * 3. Check if the file exists
         * 4. If not, create the file
         */
        try {
            if (directory.mkdir()) {
                if (DEBUG) {
                    System.out.println("Directory created: " + DIRECTORY_NAME);
                }
            } else {
                if (DEBUG) {
                    System.out.println("Directory already exists: " + DIRECTORY_NAME);
                }
            }

            if (file.createNewFile()) {
                if (DEBUG) {
                    System.out.println("File created: " + FILE_NAME);
                }
            } else {
                if (DEBUG) {
                    System.out.println("File already exists: " + FILE_NAME);
                }
            }
        } catch (SecurityException e) {
            System.out.println("SecurityException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An error occurred.");
        } catch (Exception e) {
            System.out.println("Something went wrong. An unexpected error occurred.");
            e.printStackTrace();
        }

        if (DEBUG) {
            System.out.println("Loading data: " + FILE_NAME);
        }

        try (
                FileReader fileReader = new FileReader(DIRECTORY_NAME + "/" + FILE_NAME,
                        StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            // Clear the list
            persons.clear();

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.startsWith(UTF8_BOM)) {
                    line = line.substring(1);
                }
                String[] values = line.split(",");

                if (DEBUG) {
                    System.out.println(Arrays.toString(values));
                }

                // Name, Sex, Birthdate, Birthplace, Nationality, BSN, Document number
                try {
                    persons.add(new Person(
                            values[0],
                            values[1],
                            LocalDate.parse(values[2], getDateTimeFormatter()),
                            values[3],
                            values[4],
                            Integer.parseInt(values[5]),
                            values[6]
                    ));
                } catch (DateTimeException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number: " + e.getMessage());
                }

                // Read next line
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
                FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (Person person : persons) {
                // Name, Sex, Birthdate, Birthplace, Nationality, BSN, Document number
                bufferedWriter.append(person.toStringTextFile());
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
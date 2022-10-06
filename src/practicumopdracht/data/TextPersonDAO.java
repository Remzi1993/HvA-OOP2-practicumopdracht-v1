package practicumopdracht.data;

import practicumopdracht.models.Person;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import static practicumopdracht.MainApplication.DEBUG;
import static practicumopdracht.MainApplication.getDateFormat;

/**
 * TextPersonDAO - TextMasterDAO
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class TextPersonDAO extends PersonDAO {
    private static final String FILENAME = "Persons.txt";
    private static final String UTF8_BOM = "\uFEFF";

    @Override
    public boolean load() {
        File file = new File(FILENAME);

        if (DEBUG) {
            System.out.println("Data directory: " + file.getAbsolutePath().replace(FILENAME, ""));
        }

        // Safety check - check if the file already exists and if not create an empty file
        try {
            if (file.createNewFile()) {
                if (DEBUG) {
                    System.out.println("File created: " + FILENAME);
                }
            } else {
                if (DEBUG) {
                    System.out.println("File already exists: " + FILENAME);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        if (DEBUG) {
            System.out.println("Loading data: " + FILENAME);
        }

        try (
                FileReader fileReader = new FileReader(FILENAME, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            // Clear the list in RAM
            persons.clear();

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.startsWith(UTF8_BOM)) {
                    line = line.substring(1);
                }
                String[] values = line.split(",");
                System.out.println(Arrays.toString(values));

                // Name, Sex, Birthdate, Birthplace, Nationality, BSN, Document number
                try {
                    persons.add(new Person(
                            values[0],
                            values[1],
                            LocalDate.parse(values[2], getDateFormat()),
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
                System.out.println("Loading complete: " + FILENAME);
                System.out.println(Arrays.toString(persons.toArray()));
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILENAME);
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
            System.out.println("Saving data: " + FILENAME);
        }

        try (
                FileWriter fileWriter = new FileWriter(FILENAME, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (Person person : persons) {
                // Name, Sex, Birthdate, Birthplace, Nationality, BSN, Document number
                bufferedWriter.append(person.toStringTextFile());
                bufferedWriter.newLine();
            }

            // Successful save
            if (DEBUG) {
                System.out.println("Saving complete: " + FILENAME);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILENAME);
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
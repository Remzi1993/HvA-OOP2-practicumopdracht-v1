package practicumopdracht.data;

import practicumopdracht.models.Person;
import java.io.*;
import java.time.LocalDate;
import static practicumopdracht.MainApplication.DEBUG;
import static practicumopdracht.MainApplication.getDateTimeFormatter;

public class BinaryPersonDAO extends PersonDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Persons.dat";
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
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        ) {
            if(dataInputStream.available() == 0) {
                if (DEBUG) {
                    System.out.println("File is empty");
                }
                return true;
            }

            // Clear the list
            persons.clear();

            int arraySize = dataInputStream.readInt();
            for (int i = 0; i < arraySize; i++) {
                persons.add(new Person(
                        dataInputStream.readUTF(),
                        dataInputStream.readUTF(),
                        LocalDate.parse(dataInputStream.readUTF(), getDateTimeFormatter()),
                        dataInputStream.readUTF(),
                        dataInputStream.readUTF(),
                        dataInputStream.readInt(),
                        dataInputStream.readUTF()
                ));
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
                FileOutputStream fileOutputStream = new FileOutputStream(FILE);
                DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        ) {
            dataOutputStream.writeInt(persons.size());

            for (Person person : persons) {
                // Name, Sex, Birthdate, Birthplace, Nationality, SSN, Document number
                dataOutputStream.writeUTF(person.getName());
                dataOutputStream.writeUTF(person.getSex());
                dataOutputStream.writeUTF(person.getBirthdate().format(getDateTimeFormatter()));
                dataOutputStream.writeUTF(person.getBirthplace());
                dataOutputStream.writeUTF(person.getNationality());
                dataOutputStream.writeInt(person.getSSN());
                dataOutputStream.writeUTF(person.getDocumentNumber());
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

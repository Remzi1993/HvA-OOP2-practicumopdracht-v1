package practicumopdracht.utils;

import java.io.InputStream;

/**
 * Class for loading resources.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class ResourceLoader {
    /**
     * Get a file from the resources' folder. This method works everywhere, IDEA, unit test and JAR file.
     * @param fileName the name of the file
     * @return the file as an InputStream
     */
    public InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
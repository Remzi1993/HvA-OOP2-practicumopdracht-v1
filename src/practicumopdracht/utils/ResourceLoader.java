package practicumopdracht.utils;

import java.io.*;
import java.util.Objects;

/**
 * Class for loading resources.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class ResourceLoader {
    private final Class<?> CURRENT_CLASS = new Object() { }.getClass().getEnclosingClass();
    private InputStream inputStream;

    public InputStream getInputStream(final String FILE_NAME) {
        inputStream = CURRENT_CLASS.getClassLoader().getResourceAsStream(FILE_NAME);

        // The stream holding the file content
        if (inputStream == null) {
            Exception ex = new FileNotFoundException("File not found: " + FILE_NAME);

            // Throw an IllegalArgumentException that wraps FileNotFoundException
            throw new IllegalArgumentException(ex);
        } else {
            return inputStream;
        }
    }

    public String getResourceDir(final String FILE_NAME) {
        if(FILE_NAME == null) {
            System.err.println("File name is null");
            throw new IllegalArgumentException("File name cannot be null");
        }

        // The stream holding the file content
        if (FILE_NAME.isBlank()) {
            Exception ex = new FileNotFoundException("Resource not found! - " + FILE_NAME);

            // Throw an IllegalArgumentException that wraps FileNotFoundException
            throw new IllegalArgumentException(ex);
        } else {
            return Objects.requireNonNull(CURRENT_CLASS.getResource(FILE_NAME)).toExternalForm();
        }
    }
}
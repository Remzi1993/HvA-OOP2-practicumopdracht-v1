package practicumopdracht;

import java.io.*;
import java.util.Objects;

public class ResourceLoader {
    private final Class<?> CURRENT_CLASS = new Object() { }.getClass().getEnclosingClass();
    private InputStream inputStream;

    public InputStream getInputStream(final String FILENAME) {
        inputStream = CURRENT_CLASS.getClassLoader().getResourceAsStream(FILENAME);

        // The stream holding the file content
        if (inputStream == null) {
            Exception ex = new FileNotFoundException("File not found: " + FILENAME);

            // Throw an IllegalArgumentException that wraps FileNotFoundException
            throw new IllegalArgumentException(ex);
        } else {
            return inputStream;
        }
    }

    public String getResourceDir(final String FILENAME) {
        // The stream holding the file content
        if (FILENAME.isBlank()) {
            Exception ex = new FileNotFoundException("Resource not found! - " + FILENAME);

            // Throw an IllegalArgumentException that wraps FileNotFoundException
            throw new IllegalArgumentException(ex);
        } else {
            return Objects.requireNonNull(CURRENT_CLASS.getResource(FILENAME)).toExternalForm();
        }
    }
}

package practicumopdracht;

public class IsNumeric {
    private static final boolean DEBUG = false; // Debug setting for getting error messages in the console

    /**
     * @param string will be checked if it's an int.
     * @return true or false. Return true if a String is an int
     */
    public boolean isInt(String string) {
        if (string.isBlank()) {
            if (DEBUG) {
                System.err.println("String is empty.");
            }
            return false;
        }

        try {
            int i = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            if (DEBUG) {
                System.err.println("String is not an integer!");
            }
            return false;
        }

        return true;
    }

    /**
     * @param string will be checked if it's a double.
     * @return true or false. Return true if a String is a double
     */
    public boolean isDouble(String string) {
        if (string.isBlank()) {
            if (DEBUG) {
                System.err.println("String is empty.");
            }
            return false;
        }

        try {
            double d = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            if (DEBUG) {
                System.err.println("String is not a double!");
            }
            return false;
        }

        return true;
    }
}
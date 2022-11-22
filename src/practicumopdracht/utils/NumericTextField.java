package practicumopdracht.utils;

import javafx.scene.control.TextField;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

/**
 * NumericTextField extends TextField
 * This class is a custom TextField which only allows numeric input.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class NumericTextField extends TextField {
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
    private static final char DECIMAL_SEPARATOR = ',';
    private static final char GROUPING_SEPARATOR = '.';
    private static final Pattern GROUPING_SEPARATOR_PATTERN = Pattern.compile("\\.");
    // For positive decimals: ^\\d*([,.]\\d{0,2})?$
    // For positive and negative decimals: ^-?\\d*([,.]\\d{0,2})?$
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d*([,.]\\d{0,2})?$");
    // For positive integers: ^$|^[1-9]\\d*$
    // For positive and negative integers ^$|^[-1-9]\\d*$
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^$|^[1-9]\\d*$");

    /**
     * Constructor for NumericTextField for both integer and decimal input.
     * @param IS_DECIMAL True if decimal input is allowed, false if only integer input is allowed.
     * @param MAX_LENGTH The maximum length of the number.
     */
    public NumericTextField(final boolean IS_DECIMAL, final int MAX_LENGTH) {
        super();
        DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator(DECIMAL_SEPARATOR);
        DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator(GROUPING_SEPARATOR);

        if (IS_DECIMAL) {
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!DECIMAL_PATTERN.matcher(newValue).matches()) {
                    this.setText(oldValue);
                }

                if (newValue.length() > MAX_LENGTH) {
                    String string = newValue.substring(0, MAX_LENGTH);
                    this.setText(string);
                }
            });
        } else {
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!INTEGER_PATTERN.matcher(newValue).matches()) {
                    this.setText(oldValue);
                }

                if (newValue.length() > MAX_LENGTH) {
                    String string = newValue.substring(0, MAX_LENGTH);
                    this.setText(string);
                }
            });
        }
    }

    @Override
    public void insertText(int index, String text) {
        super.insertText(index, replaceAll(text));
    }

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, replaceAll(text));
    }

    private String replaceAll(String text) {
        return GROUPING_SEPARATOR_PATTERN
                .matcher(text)
                .replaceAll(String.valueOf(DECIMAL_SEPARATOR));
    }
}
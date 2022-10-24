package practicumopdracht.utils;

import javafx.scene.control.TextField;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * NumericTextField extends TextField
 * This class is a custom TextField which only allows numeric input.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class NumericTextField extends TextField {
    private static final char DECIMAL_SEPARATOR = ',';
    private static final char GROUPING_SEPARATOR = '.';
    private static final int DEFAULT_MAX_LENGTH = 10;
    private static DecimalFormatSymbols decimalFormatSymbols;

    /**
     * Constructor for NumericTextField with a default pattern of #,##0.00 for currency.
     * The default constructor is intended for currency input and is decimal limited to 2 digits.
     */
    public NumericTextField() {
        super();
        final String PATTERN = "#,##0.00";
        decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        decimalFormatSymbols.setGroupingSeparator(GROUPING_SEPARATOR);
        DecimalFormat decimalFormat = new DecimalFormat(PATTERN, decimalFormatSymbols);
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\,\\.]\\d{0,2})?")) {
                this.setText(oldValue);
            }

            if (newValue.length() > DEFAULT_MAX_LENGTH) {
                String string = newValue.substring(0, DEFAULT_MAX_LENGTH);
                this.setText(string);
            }
        });
    }

    /**
     * This constructor only accepts positive natural numbers.
     * @param MAX_LENGTH The maximum length of the number.
     */
    public NumericTextField(final int MAX_LENGTH) {
        super();
        decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        decimalFormatSymbols.setGroupingSeparator(GROUPING_SEPARATOR);
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[1-9]\\d*$") || newValue.equals("")) {
                this.setText(newValue);
            } else {
                this.setText(oldValue);
            }

            if (newValue.length() > MAX_LENGTH) {
                String string = newValue.substring(0, MAX_LENGTH);
                this.setText(string);
            }
        });
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
        return text.replaceAll("\\.", Character.toString(decimalFormatSymbols.getDecimalSeparator()));
    }
}
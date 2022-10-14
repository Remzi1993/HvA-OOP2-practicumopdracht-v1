package practicumopdracht.utils;

import javafx.scene.control.TextField;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * NumericTextField extends TextField
 * This class is a custom TextField which only allows numeric input.
 * @author Remzi Cavdar -
 */
public class NumericTextField extends TextField {
    private static final char DECIMAL_SEPARATOR = ',';
    private static final char GROUPING_SEPARATOR = '.';
    private static final String PATTERN = "#,##0.00";
    private static DecimalFormatSymbols decimalFormatSymbols;

    public NumericTextField() {
        super();
        decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        decimalFormatSymbols.setGroupingSeparator(GROUPING_SEPARATOR);
        DecimalFormat decimalFormat = new DecimalFormat(PATTERN, decimalFormatSymbols);
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\,\\.]\\d{0,2})?")) {
                this.setText(oldValue);
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
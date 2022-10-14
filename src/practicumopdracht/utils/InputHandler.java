package practicumopdracht.utils;

import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

/**
 * Class InputHandler
 * Handles/checks all kinds of inputs (JavaFX UI Controls) and clears all inputs. There are also methods to check inputs
 * for double and int
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class InputHandler {
    private static final Border BORDER = Border.stroke(Color.RED);
    private static final IsNumeric IS_NUMERIC = new IsNumeric();
    private int totalErrorValues;

    public void checkValues(Object[] inputs) {
        // Make the empty inputs red
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if (((TextField) input).getText().isBlank()) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof ComboBox) {
                ((ComboBox<?>) input).setBorder(null);
                if (((ComboBox<?>) input).getSelectionModel().isEmpty()) {
                    ((ComboBox<?>) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof DatePicker) {
                if (((DatePicker) input).getValue() == null && ((DatePicker) input).isEditable()) {
                    ((DatePicker) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    public void checkValuesIsInt(Object[] inputs) {
        // Check if input is an int and if not give the inputs red border
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if(!IS_NUMERIC.isInt(((TextField) input).getText())) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    public void checkIsInt(Object input) {
        if (input instanceof TextField) {
            if(!IS_NUMERIC.isInt(((TextField) input).getText())) {
                ((TextField) input).setBorder(BORDER);
                ++totalErrorValues;
            }
        }
    }

    public void checkValuesIsDouble(Object[] inputs) {
        // Check if input is a double and if not give the inputs red border
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if(!IS_NUMERIC.isDouble(((TextField) input).getText())) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    public void checkIsDouble(Object input) {
        if (input instanceof TextField) {
            if(!IS_NUMERIC.isDouble(((TextField) input).getText())) {
                ((TextField) input).setBorder(BORDER);
                ++totalErrorValues;
            }
        }
    }

    public void clearValues(Object[] inputs, boolean clearComboBox) {
        if (inputs == null || inputs.length == 0) {
            return;
        }
        for (Object input : inputs) {
            if (input instanceof TextField) {
                ((TextField) input).clear();
            }
            if (input instanceof TextArea) {
                ((TextArea) input).clear();
            }
            if (input instanceof DatePicker) {
                ((DatePicker) input).getEditor().clear();
                ((DatePicker) input).setValue(null);
            }
            if (input instanceof CheckBox) {
                ((CheckBox) input).setSelected(false);
            }
            if (input instanceof ListView<?>) {
                ((ListView<?>) input).getSelectionModel().clearSelection();
            }
            if (input instanceof ComboBox && clearComboBox) {
                ((ComboBox<?>) input).getSelectionModel().clearSelection();
            }
        }
    }

    public void clearWarnings(Object[] inputs) {
        for (Object input : inputs) {
            if (input instanceof TextField) {
                ((TextField) input).setBorder(null);
            }
            if (input instanceof ComboBox) {
                ((ComboBox<?>) input).setBorder(null);
            }
            if (input instanceof DatePicker) {
                ((DatePicker) input).setBorder(null);
            }
        }
    }

    // All getters and setters
    public int getTotalErrorValues() {
        return totalErrorValues;
    }

    public void setTotalErrorValues(int total) {
        this.totalErrorValues = total;
    }
}
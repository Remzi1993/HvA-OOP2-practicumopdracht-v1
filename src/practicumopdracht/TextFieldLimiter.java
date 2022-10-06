package practicumopdracht;

import javafx.scene.control.TextField;

public class TextFieldLimiter {
    public TextFieldLimiter(final TextField textField, final int maxLength) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (textField.getText().length() > maxLength) {
                String string = textField.getText().substring(0, maxLength);
                textField.setText(string);
            }
        });
    }
}
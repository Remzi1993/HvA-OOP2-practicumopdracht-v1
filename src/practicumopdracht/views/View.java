package practicumopdracht.views;

import javafx.scene.Parent;

public abstract class View {
    protected Parent root;

    public View() {
        initializeView();
    }

    protected abstract void initializeView();

    public Parent getRoot() {
        return root;
    }
}
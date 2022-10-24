package practicumopdracht.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import practicumopdracht.utils.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static practicumopdracht.MainApplication.getAppCSS;

public class AboutView {
    public Scene getScene() {
        // VBox parent container
        VBox vboxContainer = new VBox();
        vboxContainer.getStyleClass().add("vbox-container");
        // Padding
        vboxContainer.setPadding(new Insets(10));
        vboxContainer.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        Image image = new Image(new ResourceLoader().getInputStream("images/emoji/zany-face.gif"),
                100, 100, true, true);
        ImageView imageView = new ImageView(image);
        Image image2 = new Image(new ResourceLoader().getInputStream("images/emoji/star-struck.gif"),
                100, 100, true, true);
        ImageView imageView2 = new ImageView(image2);
        Image image3 = new Image(new ResourceLoader().getInputStream("images/emoji/partying-face.gif"),
                100, 100, true, true);
        ImageView imageView3 = new ImageView(image3);
        Image image4 = new Image(new ResourceLoader().getInputStream("images/emoji/rocket.gif"),
                100, 100, true, true);
        ImageView imageView4 = new ImageView(image4);

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(imageView, imageView2, imageView3, imageView4);
        hbox.getStyleClass().add("about-view-hbox");

        // GridPane container
        GridPane gridpane = new GridPane();
        gridpane.getStyleClass().add("gridpane");
        // GridPane Column settings
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(45);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(55);
        gridpane.getColumnConstraints().addAll(col1, col2);

        // GridPane horizontal and vertical Spacing - margins between columns
        gridpane.setVgap(5);
        gridpane.setHgap(5);
        gridpane.setAlignment(Pos.CENTER);

        Label label1 = new Label("Gemaakt door:");
        Label label2 = new Label("Remzi Cavdar");
        gridpane.add(label1, 0, 0);
        gridpane.add(label2, 1, 0);
        Label label3 = new Label("Klas:");
        Label label4 = new Label("ISR101");
        gridpane.add(label3, 0, 1);
        gridpane.add(label4, 1, 1);
        Label label5 = new Label("Studentnummer:");
        Label label6 = new Label("500714645");
        gridpane.add(label5, 0, 2);
        gridpane.add(label6, 1, 2);
        Label label7 = new Label("E-mail:");
        Hyperlink link = new Hyperlink("remzi.cavdar@hva.nl");
        gridpane.add(label7, 0, 3);
        gridpane.add(link, 1, 3);
        Label label8 = new Label("School:");
        Label label9 = new Label("Hogeschool van Amsterdam (HvA)");
        gridpane.add(label8, 0, 4);
        gridpane.add(label9, 1, 4);
        Label label10 = new Label("Opleiding:");
        Label label11 = new Label("HBO-ICT Software Engineering");
        gridpane.add(label10, 0, 5);
        gridpane.add(label11, 1, 5);
        Label label12 = new Label("Studieloopbaanbegeleider:");
        Label label13 = new Label("Ingrid Roks");
        gridpane.add(label12, 0, 6);
        gridpane.add(label13, 1, 6);

        link.setOnAction(e -> {
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI("mailto:remzi.cavdar@hva.nl"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().add(gridpane);

        // Root container
        vboxContainer.getChildren().addAll(hbox, hbox2);
        // Scene
        Scene scene = new Scene(vboxContainer, 500, 350);
        // Apply stylesheet
        scene.getStylesheets().add(getAppCSS());
        return scene;
    }
}
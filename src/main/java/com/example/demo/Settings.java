package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Settings extends VBox {


    // not used Anymore


    private boolean darkMode = false;
    private String name;
    private Role role;


    private enum Role{
        Student_High,
        Student_Uni,
        Teacher,
        Trainee,
        Individual,
        Other,
    }



    public Settings(Stage window, Scene home) {
        Button button = new Button("Dark Mode");
        button.setOnAction(event -> {
            setDarkMode();
        });

        Button go_back = new Button("Go back");
        go_back.getStyleClass().add("button");
        go_back.setOnAction(e -> window.setScene(home));
        GridPane.setConstraints(go_back, 0, 9);

        this.getChildren().addAll(button, go_back);
    }


    // Set DarkMode for the settings screen
    private void setDarkMode(){
        // If it is not in dark mode switch to dark mode.
        if (!this.darkMode) {
            // Create Dark background
            BackgroundFill darkFill = new BackgroundFill(Color.BLACK,
                    new CornerRadii(0), new Insets(0));
            Background dark = new Background(darkFill);
            // Set background
            this.setBackground(dark);
            this.darkMode = true;
        }
        // If in dark mode switch to light mode
        else {
            // Create Light background
            BackgroundFill lightFill = new BackgroundFill(Color.WHITE,
                    new CornerRadii(0), new Insets(0));
            Background light = new Background(lightFill);
            // Set background
            this.setBackground(light);
            this.darkMode = false;
        }
    }
    public void darkMode (Pane root){
        // If Dark mode is enabled change given root to dark mode
        if (this.darkMode) {
            // Create Dark background
            BackgroundFill darkFill = new BackgroundFill(Color.BLACK,
                    new CornerRadii(0), new Insets(0));
            Background dark = new Background(darkFill);
            // Set background
            root.setBackground(dark);
            this.darkMode = true;
        }
        // If Dark mode is enabled change given root to dark mode
        else {
            // Create Light background
            BackgroundFill lightFill = new BackgroundFill(Color.WHITE,
                    new CornerRadii(0), new Insets(0));
            Background light = new Background(lightFill);
            // Set background
            root.setBackground(light);
            this.darkMode = false;
        }
    }

    public boolean getDarkMode() {
        return darkMode;
    }
}

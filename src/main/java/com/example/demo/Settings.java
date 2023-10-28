package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/** Kayden */
public class Settings extends Pane {

    public boolean darkMode = false;



    public Settings() {
        Button button = new Button("Dark Mode");
        button.setOnAction(event -> {
            setDarkMode();
        });
        this.getChildren().add(button);
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

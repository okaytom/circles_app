package com.example.demo;

/**
 * TOMMY OJO
 */


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CircleController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onCircleButtonClick() {
        welcomeText.setText("You get a circle!");
    }
}
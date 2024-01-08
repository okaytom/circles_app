package com.example.demo.UserInput;

/**
 * TOMMY OJO
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/***
 * Creates a new Alert Window that must be addressed before any other window
 ***/
public class AlertBox {

    /**
     * Makes a new window and displays some text
     * @param title Window title
     * @param message Window message
     */
    public static void  display(String title, String message){
        Stage alert_window = new Stage();
        // have to address this window before any other open window
        alert_window.initModality(Modality.APPLICATION_MODAL);
        alert_window.setTitle(title);
        alert_window.setMinWidth(350);

        Label alert = new Label();
        alert.setText(message);

        Button close = new Button("Ok");
        close.setOnAction(e -> alert_window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(alert, close);
        layout.setAlignment(Pos.CENTER);

        Scene alert_scene = new Scene(layout);
        alert_window.setScene(alert_scene);
        alert_window.showAndWait(); // needs to be closed
    }
}

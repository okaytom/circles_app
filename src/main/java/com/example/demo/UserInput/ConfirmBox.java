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
public class ConfirmBox {
    static boolean confirm;

    /**
     *  Makes a new window that displays a question
     * @param title window title
     * @param message Question you want confirmed
     * @return true if user clicked yes, false otherwise
     */
    public static boolean  display(String title, String message){
        Stage alert_window = new Stage();


        // have to address this window before any other open window
        alert_window.initModality(Modality.APPLICATION_MODAL);
        alert_window.setTitle(title);
        alert_window.setMinWidth(350);

        Label alert = new Label();
        alert.setText(message);

        Button yes = new Button("yes");
        yes.setOnAction(e -> {
            confirm = true;
            alert_window.close();
        });

        Button no = new Button("no");
        no.setOnAction(e -> {
            confirm = false;
            alert_window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(alert, yes, no);
        layout.setAlignment(Pos.CENTER);

        Scene alert_scene = new Scene(layout);
        alert_window.setScene(alert_scene);
        alert_window.showAndWait(); // needs to be closed

        return confirm;
    }
}

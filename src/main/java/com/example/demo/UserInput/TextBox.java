package com.example.demo.UserInput;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * TOMMY OJO
 */
public class TextBox {
    static String userText;

    /**
     * Get text from the user in a new window. Once opened, a text must be inputted
     * @param title title of textbox window
     * @param message prompt of what text should be expecting
     * @return
     */
    public static String display(String title, String message){
        userText = ""; // clear string before every call.

        Stage text_window = new Stage();

        text_window.setOnCloseRequest(e -> {
            e.consume();
            AlertBox.display("Error adding text", "Must add text");
        });


        // have to address this window before any other open window
        text_window.initModality(Modality.APPLICATION_MODAL);
        text_window.setTitle(title);
        text_window.setMinWidth(350);

        Label text = new Label();
        text.setText(message);

        Button yes = new Button("Okay");
        yes.setOnAction(e -> {
            text_window.close();
        });

        TextField input = new TextField();

        Button okay = new Button("Okay");
        okay.setOnAction(e -> {
            if(input.getText().isBlank()){
                AlertBox.display("Invalid Input", "Need to input text");
            }
            else{
                userText = input.getText();
                text_window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, input, okay);
        layout.setAlignment(Pos.CENTER);

        Scene text_scene = new Scene(layout);
        text_window.setScene(text_scene);
        text_window.showAndWait(); // needs to be closed

        return userText;
    }
}

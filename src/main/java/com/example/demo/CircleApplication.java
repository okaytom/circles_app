package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.scene.control.Button;

public class CircleApplication extends Application {
    // Main stage for application
    Stage window;

    // Button for adding events
    Button add_event;

    // Scenes for Calender
    Scene event_scene;
    Scene calender_scene;



    @Override
    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");
        //FXMLLoader fxmlLoader = new FXMLLoader(CircleApplication.class.getResource("hello-view.fxml"));

        // creating layout with buttons and labels
        VBox layout =  new VBox(30);
        add_event = new Button("Add event");
        add_event.setOnAction(e -> AddEvent());
        layout.getChildren().addAll(add_event);

        calender_scene = new Scene(layout, 640, 480);
        window.setScene(calender_scene);


        window.show();


    }

    void AddEvent(){
        VBox event_layout = new VBox(20);

        //Go back to calender
        Button calender = new Button("Confirm added event");
        calender.setOnAction(e -> window.setScene(calender_scene));

        //test AlertBox
        Button alert = new Button("Will create alertbox");
        alert.setOnAction(e -> AlertBox.display("DON'T DO WHAT YOU JUST DID", "ya dummy"));

        event_layout.getChildren().addAll(calender, alert);
        event_scene = new Scene(event_layout, 240, 280);

        window.setScene(event_scene);


    }
    public static void main(String[] args) {
        launch();
    }
}
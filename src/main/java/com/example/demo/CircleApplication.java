package com.example.demo;

/**
 * TOMMY OJO
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.Button;

public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    Stage window;

    /**
     * Button for adding events
     */
    Button add_event;

    /**
     * Scene for Calender view
     */
    Scene event_scene;

    /**
     * Scenes for event view
     */
    Scene calender_scene;

    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<Events>();

    String[] months_lst = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};
    @Override
    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });


        // creating layout with buttons and labels
        VBox layout = new VBox(30);
        add_event = new Button("Add event");
        add_event.setOnAction(e -> AddEvent());
        layout.getChildren().addAll(add_event);

        calender_scene = new Scene(layout, 640, 480);
        window.setScene(calender_scene);


        window.show();


    }

    /**
     * Makes a new scene for adding events into the calender
     */
    void AddEvent() {
        //VBox event_layout = new VBox(20);

        Events new_event = new Events();

        //Grid for layout
        GridPane event_layout = new GridPane();
        event_layout.setPadding(new Insets(10, 10, 10, 10));
        event_layout.setHgap(7);
        event_layout.setVgap(7);

        // label on the left, choice on the right
        Label name = new Label("Event");
        GridPane.setConstraints(name, 0, 0);
        TextField subject = new TextField();
        GridPane.setConstraints(subject, 1, 0);

        Label year = new Label("Year");
        GridPane.setConstraints(year, 0, 1);
        ChoiceBox<String> years = new ChoiceBox<>();
        years.getItems().addAll("2023", "2024", "2025", "2026",
                "2027", "2028", "2029", "2030", "2031", "2032",
                "2033", "2034");
        GridPane.setConstraints(years, 1, 1);


        Label month = new Label("Month");
        GridPane.setConstraints(month, 0, 2);
        ChoiceBox<String> months = new ChoiceBox<>();
        months.getItems().addAll(months_lst);
        GridPane.setConstraints(months, 1, 2);

        Label day = new Label("Day");
        GridPane.setConstraints(day, 0, 3);
        TextField chosen_day = new TextField();
        GridPane.setConstraints(chosen_day, 1, 3);

        Label occurence = new Label("Occurence");
        GridPane.setConstraints(occurence, 0, 4);
        ChoiceBox<String> repeating = new ChoiceBox<>();
        repeating.getItems().addAll("One-Time", "Daily", "Weekly");
        // default value
        repeating.setValue("One-Time");
        GridPane.setConstraints(repeating, 1, 4);

        //Go back to calender
        Button calender = new Button("Confirm added event");
        calender.setOnAction(e -> {
            window.setScene(calender_scene);
            new_event.setDate(chosen_day.getText() + "-" + months.getValue() + "-" + years.getValue());
            new_event.setSubject(subject.getText());
            new_event.setCategory(repeating.getValue());
            System.out.println(new_event.getDate());
            System.out.println(new_event.getSubject());
            System.out.println(new_event.getCategory());
            events.add(new_event);

        });
        GridPane.setConstraints(calender, 0, 5);

        Button go_back = new Button("Go back");
        go_back.setOnAction(e -> window.setScene(calender_scene));
        GridPane.setConstraints(go_back, 0, 6);


        //test AlertBox
//        Button alert = new Button("Will create alertbox");
//        alert.setOnAction(e -> AlertBox.display("DON'T DO WHAT YOU JUST DID", "ya dummy"));

        event_layout.getChildren().addAll(name, subject, year, years, months, month,
                day, chosen_day, occurence, repeating, calender, go_back);

        event_scene = new Scene(event_layout, 640, 480);

        window.setScene(event_scene);

    }

    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            window.close();
            // TODO Add SaveState here
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
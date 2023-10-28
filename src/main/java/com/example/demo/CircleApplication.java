package com.example.demo;

/**
 * TOMMY OJO
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;


public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    Stage window;


    Scene calendar_scene;

    Scene setting_scene;

    Scene files_scene;

    Scene search_scene;

    String filepath = SaveState.devFolder + "/Events.json";

    CalendarBasic calendar_obj;

    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");
        BorderPane maindisplay = new BorderPane();

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });





        // making calendar scene
        calendar_scene = new Scene(maindisplay, 652, 480);
        calendar_scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        /* calendar object*/
        calendar_obj = new CalendarBasic(window, calendar_scene);
        calendar_obj.events = SaveState.Load(filepath, Events.class);

        //listener for whenever the calendar redraws itself
        calendar_obj.display.bottomProperty().addListener( (v,oldvalue, newvalue) -> {
            maindisplay.setRight(calendar_obj.display);
        });

        // draw the initial calendar
        calendar_obj.drawCalendar();


        // Creating layout for sidebar
        Button circle = new Button("Circle");
        circle.setFont(new Font( 20));
        circle.setPrefWidth(161);
        circle.getStyleClass().add("button");

        Button calendar = new Button("Calendar");
        calendar.setFont(new Font( 20));
        calendar.setPrefWidth(161);
        calendar.getStyleClass().add("button");
        calendar.setOnAction(e -> window.setScene(calendar_scene));


        Button files = new Button("Files");
        files.setFont(new Font( 20));
        files.setPrefWidth(161);
        files.getStyleClass().add("button");
        // TODO action for files

        Button search = new Button("Search");
        search.setFont(new Font( 20));
        search.setPrefWidth(161);
        search.getStyleClass().add("button");
        // TODO action for search

        Button settings = new Button("Settings");
        settings.setFont(new Font( 20));
        settings.setPrefWidth(161);
        settings.getStyleClass().add("button");
        // TODO action for settings


        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: blue;");
        sidebar.setFillWidth(true);

        sidebar.getChildren().addAll(circle, calendar,files, search, settings);
        maindisplay.setLeft(sidebar);


        window.setScene(calendar_scene);
        window.show();
    }


    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            // TODO Add SaveState here
            SaveState.Save(filepath, calendar_obj.events);
            window.close();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
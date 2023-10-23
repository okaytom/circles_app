package com.example.demo;

/**
 * TOMMY OJO
 */

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;


public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    Stage window;

    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        /* calender object*/
        CalendarBasic calendar = new CalendarBasic(window);

        window.setScene(calendar.CalenderScene());
        window.show();
    }


    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            // TODO Add SaveState here
            window.close();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
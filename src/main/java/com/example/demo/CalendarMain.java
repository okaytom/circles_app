package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalendarMain extends Application {
    double posX = 0;
    double posY = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalendarMain.class.getResource("calendar.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("calendar.sample.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Side Menu Title");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

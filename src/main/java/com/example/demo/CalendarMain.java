package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CalendarMain extends Application {
    double posX = 0;
    double posY = 0;
    @Override
    public void start(Stage stage) throws IOException {
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("calendar.fxml")));
        Scene scene = new Scene(root);

        root.setOnMousePressed(event -> {
            posX = event.getSceneX();
            posY = event.getSceneY();
        });

        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

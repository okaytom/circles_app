package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SideMenuMain extends Application {

    double posX = 0;
    double posY = 0;

    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("SideMenuView.fxml"));
        Scene scene = new Scene(root);

        root.setOnMousePressed(event -> {
            posX = event.getSceneX();
            posY = event.getSceneY();
        });

        stage.setTitle("Side Menu Title");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

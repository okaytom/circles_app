package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Testmain extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}


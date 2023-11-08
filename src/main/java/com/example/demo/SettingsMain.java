package com.example.demo;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsMain extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        System.out.println(getClass().getResource("Settings.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getResource("test.fxml"));
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(root1));
        stage.setScene(new Scene(root));
        stage1.show();
        stage.show();

    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}

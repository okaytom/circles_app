package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CircleApplication extends Application {
    Stage window;

    @Override
    public void start(Stage primarystage) throws IOException {
        window = primarystage;
        FXMLLoader fxmlLoader = new FXMLLoader(CircleApplication.class.getResource("hello-view.fxml"));

        //StackPane layout = new StackPane();

        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        window.setTitle("Circle");
        window.setScene(scene);
        window.show();

        Label add = new Label("Add event");
        Button add_event = new Button("Add event");
        add_event.setOnAction(e -> AddEvent());
    }

    void AddEvent(){
        Scene event_scene;
        Vbox event_layout = new Vbox(20);
        event_layout.getChildren().addAll(add, add_event);
        event_scene = new Scene(event_layout, 640, 480);
        window.setScene(event_scene);

        Vbox event_layout = new Vbox(20);
        event_layout.getChildren().addAll(add, add_event);

        //Go back to calender
        Button calender = new Button("Confirm added event");
        calender.setOnAction(e -> window.setScene(scene));

    }
    public static void main(String[] args) {
        launch();
    }
}
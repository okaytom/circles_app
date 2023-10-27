package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
* Sakhana Shree Suresh Kumar
*/

public class CreateFlashcardView extends Application {

    private TextField frontCard = new TextField();
    private TextField backCard = new TextField();


    public void start(Stage stage){
        VBox root = new VBox();

        HBox row1 = new HBox();
        Rectangle rect1 = new Rectangle(100, 75);
        rect1.setFill(Color.CYAN);
        frontCard.setEditable(true);
        Rectangle rect2 = new Rectangle(100, 75);
        rect2.setFill(Color.CYAN);
        backCard.setEditable(true);
        row1.setPadding(new Insets(20, 20, 20, 20));
        row1.getChildren().addAll(rect1, rect2);

        HBox row2 = new HBox();
        Button add = new Button("ADD FLASHCARD");
        row2.getChildren().addAll(add);


        root.getChildren().addAll(row1, row2);

        stage.setScene(new Scene(root, 600, 480));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}

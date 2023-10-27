package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FilesNotesFlashcards extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox root = new VBox();

        HBox row1 = new HBox();
        TextField myText = new TextField("No Title");
        myText.setEditable(true);
        myText.setDisable(false);
        row1.getChildren().addAll(myText);

        HBox row2 = new HBox();
        TabPane tabPane = new TabPane();
        Tab files = new Tab("Files");
        files.setContent(new Rectangle());
        Tab notes = new Tab("Notes");
        notes.setContent(new Rectangle());
        Tab flashcards = new Tab("Flashcards");
        flashcards.setContent(new Rectangle(100, 50, Color.SKYBLUE));
        tabPane.getTabs().addAll(files, notes, flashcards);
        row2.getChildren().addAll(tabPane);



        Button newFiles = new Button();
        Button newNotes = new Button();
        Button newFlashCard = new Button();

        newButton(75, newFiles, "Import Files");
        newButton(75, newNotes, "Create Blank Note");
        newButton(75, newFlashCard, "Create New Flashcard");


        root.getChildren().addAll(row1, row2);

        stage.setScene(new Scene(root, 562, 480));
        stage.show();


    }

    public void newButton(double radius, Button myButton, String myString){
        myButton.setText(myString);
        myButton.setShape(new Circle(radius));
        myButton.setPrefSize(radius, radius);
        myButton.setTextFill(Color.CORNFLOWERBLUE);
        myButton.setFont(new Font("Times New Roman", 25));
        //myButton.setBackground(Background.fill(Color.SKYBLUE));
    }


}

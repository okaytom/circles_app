package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

// Tanner
public class SearchController {

    // Search button
    @FXML
    private Button button_search;

    // Box for output of search
    @FXML
    private VBox searchOut;


    // Box for input of search
    @FXML
    private TextField search_field;

    // These 2 functions here do the same thing, just on different user inputs, so they call a shared method
    @FXML
    void onSearchButtonClick(MouseEvent event) {
        search();
    }

    @FXML
    void onEnterPressed(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            search();
        }
    }

    public void search(){
        searchOut.getChildren().clear();
        // Get the text from the searchField
        Label searchIn = new Label(search_field.getText());
        // Run search
        String stringOutput = Searchable.Search(searchIn.getText());
        Label output = new Label();
        output.setText(stringOutput);
        // Enter search into the output box
        searchOut.getChildren().add(output);
        search_field.clear(); // clear search field afterward
    }
}

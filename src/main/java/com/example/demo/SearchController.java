package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        // Fix weird error
        search();
    }

    @FXML
    void onEnterPressed(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            search();
        }
    }

    public void search(){
        // Get the text from the searchField
        Text searchIn = new Text(search_field.getText());
        // Run search
        String out = CalendarBasic.Search(searchIn.getText());
        Text output = new Text();
        output.setText(out);
        // Enter search into the output box
        searchOut.getChildren().add(output);
    }
}

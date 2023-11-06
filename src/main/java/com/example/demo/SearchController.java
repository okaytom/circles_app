package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchController {
    @FXML
    private Button button_search;

    @FXML
    private VBox searchOut;

    @FXML
    private TextField search_field;

    @FXML
    void onSearchButtonClick(MouseEvent event) {
        Text t = new Text(search_field.getText());
        searchOut.getChildren().add(t);

        // Current plan is to take the text from search_field, run it through searchable, then output it into the VBox
    }
}

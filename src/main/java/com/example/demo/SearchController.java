package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchController implements Searchable{
    @FXML
    private Button button_search;

    @FXML
    private VBox searchOut;

    @FXML
    private TextField search_field;

    @FXML
    void onSearchButtonClick(MouseEvent event) {
        search();
        // Current plan is to take the text from search_field, run it through searchable, then output it into the VBox
    }

    @FXML
    void onEnterPressed(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            search();
        }
    }

    public void search(){
        Text searchIn = new Text(search_field.getText());
        String out = Searchable.Search(String.valueOf(searchIn));
        searchOut.getChildren().add(searchIn);
    }
}

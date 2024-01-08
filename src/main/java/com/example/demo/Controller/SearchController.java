package com.example.demo.Controller;

import com.example.demo.Model.Searchable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

// Tanner
public class SearchController {

    // Search button
    @FXML
    private Button button_search;
    private static String searchText = "";

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
        searchText = "";
        ListThing(Searchable.Search(searchIn.getText()), 0);
        Label output = new Label();
        // Check for the user searching nothing
        if(Objects.equals(search_field.getText(), "")){
            output.setText("Nothing has been searched!");
            searchOut.getChildren().add(output);
            search_field.clear(); // clear search field afterward
            return;
        }
        output.setText(searchText);
        // Enter search into the output box
        searchOut.getChildren().add(output);
        search_field.clear(); // clear search field afterward
    }

    public static void ListThing(ArrayList list, int indentation){
        if (list.size() > 0){

            //create the indentation
            String spacing = "";
            for (int index = 0; index < indentation; index++){spacing = spacing + "   ";}

            //recursively iterate through the list of strings and array lists
            for (int index=0; index < list.size(); index++){

                if (list.get(index) instanceof String){
                    searchText += spacing + list.get(index) + "\n";
                }
                else if (list.get(index) instanceof ArrayList){
                    ListThing((ArrayList) list.get(index), indentation + 1);
                }
            }


        }
    }
}

package com.example.demo;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
//import com.jfoenix.controls.JFXDrawer;

public class SideMenuController implements Initializable {

    @FXML
    private Button btn_cal, btn_fil, btn_sea, btn_menu, settingsButton;

    @FXML
    private Pane myMenu;

    boolean extended = true;

    private Parent calendar, files, search, setting;

    @FXML
    private Pane myArea;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        // slides side menu in and out
        if (event.getSource() == btn_menu){
            myMenu.setTranslateX(-150);
            TranslateTransition transition = new TranslateTransition(Duration.millis(1000), myMenu);
            transition.setFromX(-150);
            transition.setToX(0);
            if (extended){
                transition.setRate(-1);
                transition.play();
                extended = false;
            }
            else{
                transition.setRate(1);
                transition.play();
                extended = true;
            }
        }
        // switch to calendar page
        else if (event.getSource() == btn_cal) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(calendar);
            // switch to files page
        } else if (event.getSource() == btn_fil) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(files);
        // switch to search page
        } else if (event.getSource() == btn_sea) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(search);
        }
        // switch to settings page
        else if (event.getSource() == settingsButton){
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(setting);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            calendar = FXMLLoader.load(getClass().getResource("calendar.fxml"));
            files = FXMLLoader.load(getClass().getResource("Tabs.fxml"));
            search = FXMLLoader.load(getClass().getResource("SearchView.fxml"));
            setting = FXMLLoader.load(getClass().getResource("SettingsView.fxml"));

            myArea.getChildren().setAll(calendar); // sets initial view to calendar
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SettingsController.darkMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                SettingsController settingsController = new SettingsController();
                settingsController.changeMode(myArea);
                settingsController.changeMode(myMenu);
            }
        });
    }

}





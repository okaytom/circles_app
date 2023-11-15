package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
//import com.jfoenix.controls.JFXDrawer;

public class SideMenuController implements Initializable {

    @FXML
    private Pane panelCalendar, panelFiles, panelSearch, panelSetting;

//    @FXML
//    private JFXPanel leftPane;

//    @FXML
//    private JFXDrawer drawer;

    @FXML
    private Button btn_cal, btn_fil, btn_sea, menuButton;
    @FXML
    private ImageView btn_set;





    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btn_cal) {
            panelCalendar.toFront();
        } else if (event.getSource() == btn_fil) {
            panelFiles.toFront();
        } else if (event.getSource() == btn_sea) {
            panelSearch.toFront();
        }
    }

    @FXML
    private void SettingAction(){
        panelSetting.toFront();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            //
            panelCalendar.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("calendar.fxml")));
            panelSetting.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("SettingsView.fxml")));
            panelFiles.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("Tabs.fxml")));

            // TODO action for files
            // TODO action for search
            // TODO action for settings

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // whenever dark mode is changed, change the modes for the other panes as well
        SettingsController.darkMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                SettingsController settingsController = new SettingsController();
                settingsController.changeMode(panelCalendar);
                settingsController.changeMode(panelFiles);
            }
        });

    }

}




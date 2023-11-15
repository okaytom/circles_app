package com.example.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;


import java.net.URL;
import java.util.ResourceBundle;

/** Kayden */
public class SettingsController implements Initializable {

    @FXML
    private Pane parent;
    public static BooleanProperty darkMode = new SimpleBooleanProperty();
    private String name;
    private Role role;

    @FXML
    private ChoiceBox<Role> roles;

    @FXML
    private TextField nameBox;

    private enum Role{
        Student_High,
        Student_Uni,
        Teacher,
        Trainee,
        Individual,
        Other,
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roles.getItems().addAll(Role.Student_High, Role.Student_Uni, Role.Teacher, Role.Trainee, Role.Individual, Role.Other);
        if (roles.getSelectionModel().getSelectedItem() == null) {
            roles.setValue(Role.Other);
        }

    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Role getRole(){
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void save() {
        setRole(roles.getSelectionModel().getSelectedItem());
        setName(nameBox.getText());
    }

    public void exit() {
        System.out.println(getRole());
        System.out.println(getName());
    }

    private void setDarkMode(){
        parent.getStylesheets().remove(getClass().getResource("lightMode.css").toExternalForm());
        parent.getStylesheets().add(getClass().getResource("darkMode.css").toExternalForm());
        SettingsController.darkMode.setValue(true);
    }

    private void setLightMode(){
        parent.getStylesheets().remove(getClass().getResource("darkMode.css").toExternalForm());
        parent.getStylesheets().add(getClass().getResource("lightMode.css").toExternalForm());
        SettingsController.darkMode.setValue(false);
    }

    // For internal use only
    @FXML
    public void changeMode(){
        if (SettingsController.darkMode.getValue()) {
            setLightMode();
        }
        else {
            setDarkMode();
        }
    }


    // For external use
    public void changeMode(Parent parent){
        if (SettingsController.darkMode.getValue()) {
            setDarkMode(parent);
        }
        else {
            setLightMode(parent);
        }
    }

    public void setDarkMode(Parent parent){
        parent.getStylesheets().remove(getClass().getResource("lightMode.css").toExternalForm());
        parent.getStylesheets().add(getClass().getResource("darkMode.css").toExternalForm());
        System.out.println("Called dark");
    }

    public void setLightMode(Parent parent){
        parent.getStylesheets().remove(getClass().getResource("darkMode.css").toExternalForm());
        parent.getStylesheets().add(getClass().getResource("lightMode.css").toExternalForm());
        System.out.println("Called light");
    }

}


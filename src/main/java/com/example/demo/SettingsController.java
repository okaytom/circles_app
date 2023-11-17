package com.example.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/** Kayden */
public class SettingsController extends SaveState implements Initializable {


    // Variable used across multiply instances to check if darkMode is enabled or not
    public static BooleanProperty darkMode = new SimpleBooleanProperty();

    // Temp save variable
    private boolean isDarkMode;

    //
    private String name;
    private Role role;
    private transient String path = devFolder + "/Settings.json";

    @FXML
    private transient ChoiceBox<Role> roles;

    @FXML
    private transient TextField nameBox;

    @FXML
    private transient Pane parent;

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

        SettingsController temp = SaveState.LoadObject(path, SettingsController.class);


            this.setDarkMode(temp.getDarkMode());
            this.setName(temp.getName());
            this.setRole(temp.getRole());




        roles.getItems().addAll(Role.Student_High, Role.Student_Uni, Role.Teacher, Role.Trainee, Role.Individual, Role.Other);
        if (this.role == null) {
            roles.setValue(Role.Other);
        }
        else {
            roles.setValue(this.role);
        }
        nameBox.setText(this.name);
        this.changeMode(parent);

    }

    public void save() {
        setRole(roles.getSelectionModel().getSelectedItem());
        setName(nameBox.getText());

        this.isDarkMode = SettingsController.darkMode.getValue();

        SettingsController temp = new SettingsController();
        temp.setName(this.getName());
        temp.setRole(this.getRole());
        temp.isDarkMode = this.getDarkMode();

        System.out.println(this.getDarkMode());

        SaveState.Save(path, temp);


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


    private void setDarkMode(Boolean darkMode) {
        SettingsController.darkMode.setValue(darkMode);
    }

    public boolean getDarkMode(){ return this.isDarkMode;
    }

}


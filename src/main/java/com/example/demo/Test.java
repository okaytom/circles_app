package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Test {

    @FXML
    private AnchorPane parent;
    public void test() {
        System.out.println("sdakflj");
        new SettingsController().changeMode(parent);
        System.out.println("sdakflj");

    }
}

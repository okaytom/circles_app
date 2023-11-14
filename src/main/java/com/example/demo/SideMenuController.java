package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
//import com.jfoenix.controls.JFXDrawer;

public class SideMenuController implements Initializable {

    @FXML
    private Pane panelCalendar, panelFiles, panelSearch;

//    @FXML
//    private JFXPanel leftPane;

//    @FXML
//    private JFXDrawer drawer;

    @FXML
    private Button btn_cal, btn_fil, btn_sea, menuButton;



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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO

//        BasicTransition transition = new BasicTransition(menuButton);
//        transition.setRate(-1);
//
//        menuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//            transition.setRate(transition.getRate() * -1) ;
//            transition.play();
//        });
//
//        // close the menu if it's open
//        // open the menu if it's closed
//        if(drawer.isShown()){
//            drawer.close();
//        }
//        else{
//            drawer.open();
//        }
//
//
//
//        try {
//            VBox vbox = FXMLLoader.load(getClass().getResource("SideMenuView.fxml"));
//            leftPane.setSidePane(vbox);
//        }
//
//        catch (IOException exception) {
//            Logger.getLogger(SideMenuController.class.getName()).log(Level.SEVERE, null, exception);
//        }
//
//        else if (event.getSource() == menuButton){
//
//            leftPane.open();
//            menuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> {
//
//            }));
//
//            transition.setRate(transition.getRate()*-1);
//            transition.play();
//
//
//
//            StackPane root = new StackPane();
//            HBox hbox = new HBox();
//            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), hbox);
//            fadeTransition1.setFromValue(1.0);
//            fadeTransition1.setToValue(0.0);
//
//            FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(0.5), hbox);
//            fadeTransition2.setFromValue(0.0);
//            fadeTransition2.setToValue(1.0);
//
//            fadeTransition2.play();
//            fadeTransition1.play();
//            root.getChildren().remove(hbox);

//            leftPane.toBack();
    }

}






//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // minimize window
//        minimize.setOnMouseClicked(event -> {
//            Stage myStage = (Stage) minimize.getScene().getWindow();
//            myStage.setIconified(true);
//        });
//
//        // close window
//        exit.setOnMouseClicked(event -> {
//            System.exit(0);
//        });
//
//        try{
//            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PageView1.fxml")));
//            sp.getChildren().removeAll();
//            sp.getChildren().setAll();
//
//        }
//        catch (IOException exception){
//            Logger.getLogger(SideMenuController.class.getName()).log(Level.SEVERE, null, exception);
//
//        }
//    }
//
//
//
//    public void page1(ActionEvent event) throws IOException {
//        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PageView1.fxml")));
//        sp.getChildren().removeAll();
//        sp.getChildren().setAll();
//    }
//
//    public void page2(ActionEvent event) throws IOException {
//        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PageView2.fxml")));
//        sp.getChildren().removeAll();
//        sp.getChildren().setAll();
//    }
//
//    public void page3(ActionEvent event) throws IOException {
//        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PageView3.fxml")));
//        sp.getChildren().removeAll();
//        sp.getChildren().setAll();
//    }




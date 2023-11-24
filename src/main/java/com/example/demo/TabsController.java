package com.example.demo;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class TabsController implements Initializable{

    @FXML
    private Pane AddFlashCardPane, StudyFlashCardPane, PlanePane;

    @FXML
    private TextArea createQuestion, createAnswer;

    @FXML
    private Button shuffle_btn, prev_card_btn, next_card_btn;

    @FXML
    private Button flip_btn, back_btn;

    @FXML
    private Label question, answer, subjectTextPDF, subjectTextNotes, subjectTextCards;

    @FXML
    private Pane SubjectPage;

    @FXML
    private TabPane TabsPage;

    @FXML
    private GridPane subject_grid;

    ExtensionFilter extensionFilter = new ExtensionFilter("All PDFs", "*.pdf", "*.PDF");
    HostServices hostServices;

    public static ArrayList<String> Subjects = new ArrayList<>();

    // buttons for tabs view, import pdfs
    @FXML
    private Button btn_importFile, btn_openFile, add_card_btn, btn_add_sub, btn_remove_sub;

    // buttons for tabs view, flashcard view
    @FXML
    private Button add_btn, study_btn;

    @FXML
    // Open our file when necessary
    public void myHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private String pdf_filepath =  ".\\Circle App\\files";;

    private boolean question_showing;

    // import and save file to our folder
    @FXML
    public void handleImport(ActionEvent event) throws IOException {
        if (event.getSource() == btn_importFile){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setTitle("Import File");
            Stage stage = (Stage) btn_importFile.getScene().getWindow();
            File myFile = fileChooser.showOpenDialog(stage);
            if (myFile != null){
                //checking if the file already exists
                String tempString = NoteTaker.GetPDFFilePath();
                //TODO: change / remove this if we don't want each subject to have a pdf file
                pdf_filepath =  ".\\" + tempString.replace("/", "\\") + "\\";;

                if (SaveState.FileExists(myFile.getName(), NoteTaker.GetPDFFilePath())){
                    if(!ConfirmBox.display("Warning","A file with the name " + myFile.getName() + " already exists,\n would you like to overwrite it?")){
                        System.out.println("No file imported");
                        return;//user doesn't want to overwrite their existing file
                    }
                }

                // copies file from chosen path to our myFiles package, REPLACE_EXISTING overwrites file if same name
                Files.copy(myFile.toPath(), Path.of(pdf_filepath+myFile.getName()), REPLACE_EXISTING );
                File source = new File(pdf_filepath+myFile.getName());

                boolean success = source.exists();
                // prints message that file was imported and saved
                System.out.println("Operation success " + success);
            }
            else{
                System.out.println("No file imported");
            }
        }
    }

    // open files in our folder
    @FXML
    public void handleOpen() {
        //reformatting the file path
        //TODO: change / remove this if we don't want each subject to have a pdf file
        String tempString = NoteTaker.GetPDFFilePath();
        pdf_filepath = ".\\" + tempString.replace("/", "\\") + "\\";


        // Choose file to be opened
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Open File");
        // This part is hardcoded to a pdf_filepath
        fileChooser.setInitialDirectory(new File(pdf_filepath));
        // Show open file dialog
        Stage stage = (Stage) btn_openFile.getScene().getWindow();
        File myFile = fileChooser.showOpenDialog(stage);
        // Print message in console that file has or hasn't been opened
        if (myFile != null){
            // Open the PDF
            hostServices.showDocument(myFile.getAbsolutePath());
            System.out.println("File at location " + myFile.getPath() + " opened");
        }
        else{
            System.out.println("No file opened");
        }
    }

    @FXML
    private void handleFlip(ActionEvent event){
        if (event.getSource() == flip_btn){
            question_showing = !question_showing;
            handleStudyCard();
        }
    }
    @FXML
    private void handleButton(ActionEvent event){
        if (event.getSource() == add_btn) {
            AddFlashCardPane.setVisible(true);
            StudyFlashCardPane.setVisible(false);
            PlanePane.setVisible(false);
            AddFlashCardPane.toFront();

        } else if (event.getSource() == study_btn) {
            AddFlashCardPane.setVisible(false);
            StudyFlashCardPane.setVisible(true);
            PlanePane.setVisible(false);
            StudyFlashCardPane.toFront();
            handleStudyCard();
        }
    }

    private void handleStudyCard() {
        if(question_showing){
            question.toFront();
            answer.setVisible(false);
            question.setVisible(true);
        }
        else{
            answer.toFront();
            question.setVisible(false);
            answer.setVisible(true);
        }
    }

    @FXML
    private void handleBackButton(){
        PlanePane.setVisible(true);
        AddFlashCardPane.setVisible(false);
        StudyFlashCardPane.setVisible(false);
        PlanePane.toFront();
    }

    @FXML
    private void handleAddCard(ActionEvent event) {
        if (event.getSource() == add_card_btn){
            NoteTaker.AddCueCard(createQuestion.getText(), createAnswer.getText());
        }
    }

    @FXML
    private void handleShuffleCard(ActionEvent event) {
        if (event.getSource() == shuffle_btn){
            NoteTaker.RandomizeCards();
            ArrayList<String > card = NoteTaker.GetNextCard();
            if(card.size() != 0){
                question.setText(card.get(0));
                answer.setText(card.get(1));
            }
            handleStudyCard();
        }
    }


    @FXML
    private void handlePreviousCard(ActionEvent event) {
        if (event.getSource() == prev_card_btn){
            ArrayList<String> card = NoteTaker.GetPreviousCard(); // 0 is question, 1 is answer
            if(card.size() != 0){
                question.setText(card.get(0));
                answer.setText(card.get(1));
            }
            handleStudyCard();
        }
    }

    @FXML
    private void handleNextCard(ActionEvent event) {
        if (event.getSource() == next_card_btn){
            ArrayList<String> card = NoteTaker.GetNextCard();
            if(card.size() != 0){
                question.setText(card.get(0));
                answer.setText(card.get(1));
            }
            handleStudyCard();
        }
    }

    @FXML
    private void handleAddSubject(){
        String newSubject = TextBox.display("New Subject", "Enter the name of the Subject you will be adding");
        NoteTaker.AddSubject(newSubject);
        Subjects.add(newSubject);
        ShowSubjects();
    }

    @FXML
    private void handleRemoveSubject(){
        String deleteSubject = TextBox.display("Delete Subject", "Enter the name of the Subject you will be deleting");
        NoteTaker.SelectSubject(deleteSubject);
        NoteTaker.RemoveSubject();
        Subjects.remove(deleteSubject);
        ShowSubjects();
    }

    @FXML
    private void handleSubjectPage(){
        SubjectPage.setVisible(true);
        TabsPage.setVisible(false);
        SubjectPage.toFront();
        ShowSubjects();
    }

    private void GoToTabsPage(){
        SubjectPage.setVisible(false);
        TabsPage.setVisible(true);
        TabsPage.toFront();
    }

    private void ShowSubjects(){
        Button subject_folder;

        int rows = subject_grid.getRowCount();
        int cols = subject_grid.getColumnCount();
        subject_grid.getChildren().clear();

        int row_spot = 0;
        int col_spot = 0;
        for (String Subject : Subjects){
            subject_folder = new Button();
            newButton(100, subject_folder, Subject);
            subject_grid.add(subject_folder, col_spot, row_spot);
            if(col_spot >= cols-1){
                col_spot = 0;
                row_spot++;
            }
            else{
                col_spot++;
            }
        }
    }

    // SAKHANA
    private void newButton(double radius, Button myButton, String myString){
        myButton.setText(myString);
        myButton.setShape(new Circle(radius));
        myButton.setPrefSize(radius, radius);
        myButton.setFont(new Font("Times New Roman", radius/10));
        myButton.getStyleClass().add("button");
        myButton.setOnAction(e -> {
            NoteTaker.SelectSubject(myString);
            subjectTextPDF.setText(myString);
            subjectTextCards.setText(myString);
            subjectTextNotes.setText(myString);
            GoToTabsPage();
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Subjects = NoteTaker.GetAllSubjectNames();

        question.setText("Press the arrows to study!");
        answer.setText("C'mon do it!");
        question_showing = true;

        handleBackButton(); // plain view at first

        handleSubjectPage();
    }
}

//    public Stage stage;
//    private Pane myPane;
//    public ActionEvent event;


//    public ImportFileViewController(Stage stage, ActionEvent event){
//        this.stage = stage;
//        handleImportButtonAction(event);
//        handleOpenButtonAction(event);
//    }









//
//    @FXML
//    private void handleImportButtonAction(ActionEvent event) {
//        if (event.getSource() == btn_importFile) {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Import PDF");
//            fileChooser.getExtensionFilters().add(extensionFilter);
//
//            // we're choosing multiple files
//            List<File> myFiles = fileChooser.showOpenMultipleDialog(null);
//
//            // print out the path of all the chosen files
//            for (File file: myFiles){
//                System.out.println(file.getAbsolutePath());
//
//                for (Label label: labels){
//                    label.setText(file.getAbsolutePath());
//                }
//            }
//
//            // default file path
//            File filePath = new File("C:/Downloads");
//            fileChooser.setInitialDirectory(filePath);
//
//            // print message that file path has been chosen
////            File myFile = fileChooser.showOpenDialog(stage.getScene().getWindow());
////            if (myFile != null) {
////                System.out.println("Opened file located at " + myFile.getPath());
////            }
//        }
//    }
//
//
//    @FXML
//    private void handleSaveButtonAction(ActionEvent event) {
//        if (event.getSource() == btn_saveFile) {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Save PDF");
//            fileChooser.getExtensionFilters().add(extensionFilter);
//
//            // default file path
//            File filePath = new File("C:/Downloads");
//            fileChooser.setInitialDirectory(filePath);
//
//            // print message that file path has been saved
//            File myFile = fileChooser.showSaveDialog(stage.getScene().getWindow());
//            if (myFile != null) {
//                System.out.println("Opened file located at " + myFile.getPath());
//            }
//        }
//    }
//
//    public void initialize(URL url, ResourceBundle resourceBundle){
//        allLabels = new ArrayList<>();
//        allLabels.add("*.pdf");
//        allLabels.add("*.PDF");
//    }



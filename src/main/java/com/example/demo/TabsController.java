package com.example.demo;

import javafx.application.HostServices;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
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

    //TODO make sure archiving saves properly

    @FXML
    private ListView<String> archivedListView;

    private Parent notes;
    @FXML
    private ListView<String> NotesListView;

    @FXML
    private Button unarchive, deleteSub;

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
    private Pane SubjectPage, ArchivedSubjectPage;

    @FXML
    private TabPane TabsPage;

    @FXML
    private GridPane subject_grid;

    ExtensionFilter extensionFilter = new ExtensionFilter("All PDFs", "*.pdf", "*.PDF");
    HostServices hostServices;

    public static ArrayList<String> Subjects;

    public static ArrayList<String> ArchivedSubjects = new ArrayList<>();

    // buttons for tabs view, import pdfs
    @FXML
    private Button btn_importFile, btn_openFile, add_card_btn, btn_add_sub, btn_remove_sub;

    // buttons for tabs view, flashcard view
    @FXML
    private Button add_btn, study_btn;

    @FXML
    private ListView<String> PDFListView;

    @FXML
    private ListView<String> CardsListView;

    @FXML
    // Open our file when necessary
    public void myHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private String pdf_filepath;

    private boolean question_showing;

    private NoteController noteController;

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
                PDFListView.getItems().add(myFile.getName());
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
    private void openPDF(MouseEvent click){
        if(click.getClickCount() == 2){ // doubleclick
            String tempString = NoteTaker.GetPDFFilePath();
            pdf_filepath = ".\\" + tempString.replace("/", "\\") + "\\";
            if(PDFListView.getSelectionModel().getSelectedItem() != null) {
                File myFile = new File(pdf_filepath + PDFListView.getSelectionModel().getSelectedItem() + ".pdf");
                if (myFile != null) {
                    // Open the PDF
                    hostServices.showDocument(myFile.getAbsolutePath());
                    System.out.println("File at location " + myFile.getPath() + " opened");
                } else {
                    System.out.println("No file opened");
                    AlertBox.display("Error with pdfs", "Could not open PDF, file does not exist in directory or is not a pdf");
                }
            }
        }
    }

    @FXML
    private void openNotes(MouseEvent click) throws IOException {
        if(click.getClickCount() == 2){
            if(NotesListView.getSelectionModel().getSelectedItem() != null){
                noteController.load(NoteTaker.GetNotesFilePath() + "\\" + NotesListView.getSelectionModel().getSelectedItem() + ".pdf");
            }
        }
    }

    @FXML
    private void deletePDF(DragEvent event){
        if(PDFListView.getSelectionModel().getSelectedItem() != null){
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String deleted = PDFListView.getSelectionModel().getSelectedItem();
                System.out.println(NoteTaker.GetPDFFilePath() + "\\" + deleted);
                File deletedFile = new File(NoteTaker.GetPDFFilePath() + "\\" + deleted + ".pdf");
                if(ConfirmBox.display("Delete confirmation", "Are you sure you want to delete " + deleted)){
                    if(deletedFile.delete()){
                        PDFListView.getItems().remove(deleted);
                        event.setDropCompleted(true);
                    }
                    else{
                        AlertBox.display("Could not delete file", "File does no longer exist or is not a pdf");
                        event.setDropCompleted(false);
                    }
                }
            }
            else{
                event.setDropCompleted(false);
            }

        }
    }

    @FXML
    private void deleteNote(DragEvent event){
        if(NotesListView.getSelectionModel().getSelectedItem() != null){
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String deleted = NotesListView.getSelectionModel().getSelectedItem();
                File deletedFile = new File(NoteTaker.GetNotesFilePath() + "\\" + deleted + ".pdf");
                if(ConfirmBox.display("Delete confirmation", "Are you sure you want to delete " + deleted)){
                    if(deletedFile.delete()){
                        NotesListView.getItems().remove(deleted);
                        event.setDropCompleted(true);
                    }
                    else{
                        AlertBox.display("Could not delete file", "File does no longer exist or is not a pdf");
                        event.setDropCompleted(false);
                    }
                }
            }
            else{
                event.setDropCompleted(false);
            }

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
        if(!Subjects.contains(newSubject)){
            Subjects.add(newSubject);
            ShowSubjects();
        }
    }

    @FXML
    private void handleRemoveSubject(){
        String deleteSubject = TextBox.display("Delete Subject", "Enter the name of the Subject you will be deleting");
        if(ConfirmBox.display("Delete Confirmation", "Are you sure you want to delete this subject and all its files? You can just to archive this subject instead.")){
            NoteTaker.SelectSubject(deleteSubject);
            NoteTaker.DeleteSubjectFolder();
            Subjects.remove(deleteSubject);
            ShowSubjects();
        }
    }

    @FXML
    private void handleArchiveSubject(){
        String archiveSubject = TextBox.display("Archive Subject", "Enter the name of the Subject you will be archiving");
        if(Subjects.contains(archiveSubject)){
            NoteTaker.SelectSubject(archiveSubject);
            NoteTaker.RemoveSubject();
            Subjects.remove(archiveSubject);
            ArchivedSubjects.add(archiveSubject);
            ShowSubjects();
        }
        else{
            AlertBox.display("Invalid input", "No subjects in subjectlist with that name");
        }
    }

    @FXML
    private void handleSubjectPage(){
        SubjectPage.setVisible(true);
        TabsPage.setVisible(false);
        ArchivedSubjectPage.setVisible(false);
        SubjectPage.toFront();
        ShowSubjects();
    }

    private void GoToTabsPage(){
        SubjectPage.setVisible(false);
        ArchivedSubjectPage.setVisible(false);
        TabsPage.setVisible(true);
        TabsPage.toFront();
    }

    @FXML
    private void GoToArchivedSubjectPage(){
        SubjectPage.setVisible(false);
        ArchivedSubjectPage.setVisible(true);
        TabsPage.setVisible(false);
        ArchivedSubjectPage.toFront();

        if(ArchivedSubjects.size() != 0){
            archivedListView.getItems().clear();

            for(String subject : ArchivedSubjects){
                archivedListView.getItems().add(subject);

            }
            // can only pick one event at a time
            archivedListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



            // remove subject from archived list and add it to subjects
            unarchive.getStyleClass().add("button");
            unarchive.setOnAction(e -> {
                String sub = archivedListView.getSelectionModel().getSelectedItem();
                if(sub != null){
                    if(!sub.isBlank()){
                        ArchivedSubjects.remove(sub);
                        archivedListView.getItems().remove(sub);
                        NoteTaker.RemoveArchivedSubject(sub);
                        NoteTaker.AddSubject(sub);
                        Subjects.add(sub);
                    }
                }
            });

            // delete subject and all its files
            deleteSub.getStyleClass().add("button");
            deleteSub.setOnAction(e -> {
                String sub = archivedListView.getSelectionModel().getSelectedItem();
                if(sub != null){
                    if(!sub.isBlank()){
                        ArchivedSubjects.remove(sub);
                        archivedListView.getItems().remove(sub);
                        NoteTaker.RemoveArchivedSubject(sub);
                        // needs to be added to subject list to have all its folders deleted
                        NoteTaker.AddSubject(sub);
                        NoteTaker.SelectSubject(sub);
                        NoteTaker.DeleteSubjectFolder();
                        // Since DeleteSubjectFolder calls RemoveSubject, and RemoveSubject adds to the
                        // ArchivedSubject arraylist, we have to remove the subject again
                        NoteTaker.RemoveArchivedSubject(sub);
                    }
                }
            });
        }
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
            ArrayList<String> pdfs = NoteTaker.GetAllPDFs();
            ArrayList<String> notes = NoteTaker.GetAllNotes();

            //Add all pdfs to pdf list
            PDFListView.getItems().clear();
            for(String pdf : pdfs){
                PDFListView.getItems().add(pdf);
            }

            //Add all notes to note list
            NotesListView.getItems().clear();
            for(String note : notes){
                NotesListView.getItems().add(note);
            }
            GoToTabsPage();
        });
    }

    @FXML
    private void openNotesWindow(){
        Stage notes_window = new Stage();
        notes_window.setTitle("New Note");
        notes_window.setScene(new Scene(notes));
        notes_window.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader notesLoader = new FXMLLoader(getClass().getResource("NoteView.fxml"));
            notes = notesLoader.load();
            noteController = notesLoader.getController();
            noteController.getTabsController(this); // sets up communication between controllers
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Subjects = NoteTaker.GetAllSubjectNames();
        ArchivedSubjects = NoteTaker.GetAllArchivedSubjectNames();

        setCellDragable(PDFListView);
        setCellDragable(NotesListView);
        setCellDragable(CardsListView);

        question.setText("Press the arrows to study!");
        answer.setText("C'mon do it!");
        question_showing = true;

        handleBackButton(); // plain view at first for flash cards
        handleSubjectPage();
    }

    public  ListView<String> getNotesListView(){
        return NotesListView;
    }

    @FXML
    private void DeleteDragOver(DragEvent event){
        if(event.getGestureSource() != this && event.getDragboard().hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private void setCellDragable(ListView<String> listView){

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory( lv -> {
            ListCell<String> cell = new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            ObjectProperty<ListCell<String>> drag = new SimpleObjectProperty<>();

            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent info = new ClipboardContent();
                    info.putString(cell.getItem());
                    db.setContent(info);
                    drag.set(cell);
                }
            });

            cell.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            });

//            cell.setOnDragDone(event ->{
//                if(event.isDropCompleted()){
//
//                }
//            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString() && drag.get() != null) {
                    event.setDropCompleted(true);
                }
                else{
                    event.setDropCompleted(false);
                }
            });
            return cell;
        });
    }
}

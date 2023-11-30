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

    /**
     * Other scenes and the windows for the scene
     */
    private Parent notes;

    private Parent cards;

    private Stage cardsWindow;

    private Stage notesWindow;

    /**
     * For Subject
     */
    @FXML
    private Pane SubjectPage;

    @FXML
    private TabPane TabsPage;
    @FXML
    private Label subjectTextPDF, subjectTextNotes, subjectTextCards;
    @FXML
    private GridPane subject_grid;
    public static ArrayList<String> Subjects;

    /**
     * For PDFs
     */
    private ExtensionFilter extensionFilter = new ExtensionFilter("All PDFs", "*.pdf", "*.PDF");
    private HostServices hostServices;
    @FXML
    private Button btn_importFile, btn_openFile;

    /**
     * Controllers
     */
    private NoteController noteController;
    private FlashCardsController flashCardsController;

    /**
     * ListViews for tabs
     */
    @FXML
    private ListView<String> NotesListView;
    @FXML
    private ListView<String> CardsListView;
    @FXML
    private ListView<String> PDFListView;

    /**
     *Methods for PDF
     */
    @FXML
    public void myHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

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
                String pdf_filepath =  ".\\" + tempString.replace("/", "\\") + "\\";;

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

    // open files in our folder using the open button
    @FXML
    public void handleOpen() {
        //reformatting the file path
        String tempString = NoteTaker.GetPDFFilePath();
        String pdf_filepath = ".\\" + tempString.replace("/", "\\") + "\\";


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

    // open files by double clicking in list view
    @FXML
    private void openPDF(MouseEvent click){
        if(click.getClickCount() == 2){ // double click
            String tempString = NoteTaker.GetPDFFilePath();
            String pdf_filepath = ".\\" + tempString.replace("/", "\\") + "\\";
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

    // For trashcan drag
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
    /**
     *Methods for Notes
     */
    @FXML
    private void LoadNotes(MouseEvent click) throws IOException {
        if(click.getClickCount() == 2){
            if(NotesListView.getSelectionModel().getSelectedItem() != null){
                String noteName = NotesListView.getSelectionModel().getSelectedItem();
                notesWindow.setTitle(noteName);
                notesWindow.show();
                noteController.load(NoteTaker.GetNotesFilePath(), noteName);
            }
        }
    }
    @FXML
    private void openNotesWindow(){
        noteController.NewSession();
        notesWindow.setTitle("New Note");
        notesWindow.show();
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
                        NotesListView.getSelectionModel().setSelectionMode(null);
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

    /**
     *Methods for FlashCards
     */
    @FXML
    private void AddFlashcard() {
        flashCardsController.AddButton();
        cardsWindow.show();
    }
    @FXML
    private void StudyFlashCards(){
        flashCardsController.StudyButton();
        cardsWindow.show();
    }

    @FXML
    private void ModifyCards(MouseEvent click) throws IOException {
        if(click.getClickCount() == 2){
            if(CardsListView.getSelectionModel().getSelectedItem() != null){
                String cardName = CardsListView.getSelectionModel().getSelectedItem();

                ArrayList<ArrayList<String>> cards = NoteTaker.GetAllCueCards();

                for(ArrayList<String> card : cards) {
                    if (card.get(0).equals(cardName)) {
                        flashCardsController.ModifyButton(card.get(0), card.get(1));
                        cardsWindow.show();
                    }
                }

            }
        }
    }


    // for deleting on drag
    @FXML
    private void deleteCard(DragEvent event){
        if(CardsListView.getSelectionModel().getSelectedItem() != null){
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String deleted = CardsListView.getSelectionModel().getSelectedItem();

                if(ConfirmBox.display("Delete confirmation", "Are you sure you want to delete " + deleted)){
                    ArrayList<ArrayList<String>> cards = NoteTaker.GetAllCueCards();

                    for(ArrayList<String> card : cards) {
                        if (card.get(0).equals(deleted)) {
                            NoteTaker.RemoveCard(card.get(0), card.get(1));
                            CardsListView.getItems().remove(card.get(0));
                        }
                    }
                }
            }
            else{
                event.setDropCompleted(false);
            }

        }
    }


    // Subjects
    @FXML
    private void handleAddSubject(){
        String newSubject = TextBox.display("New Subject", "Enter the name of the Subject you will be adding");
        NoteTaker.AddSubject(newSubject);
        if(!Subjects.contains(newSubject)){
            if(Subjects.size() < 15){
                Subjects.add(newSubject);
                ShowSubjects();
            }
            else{
                AlertBox.display("Max Subject Count", "There can only be 15 subjects");
            }
        }
    }

    @FXML
    private void handleRemoveSubject(){
        String deleteSubject = TextBox.display("Delete Subject", "Enter the name of the Subject you will be deleting");
        if(ConfirmBox.display("Delete Confirmation", "Are you sure you want to delete this subject and all its files?")){
            if(Subjects.contains(deleteSubject)){
                NoteTaker.SelectSubject(deleteSubject);
                NoteTaker.DeleteSubjectFolder();
                Subjects.remove(deleteSubject);
                ShowSubjects();
            }
            else{
                AlertBox.display("Error in deleteing subject", "No subject with the name given exists");
            }
        }
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
            newButton(70, subject_folder, Subject);
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
        myButton.setFont(new Font("Times New Roman", radius/8));
        myButton.getStyleClass().add("button");
        myButton.setOnAction(e -> {
            NoteTaker.SelectSubject(myString);
            subjectTextPDF.setText(myString);
            subjectTextCards.setText(myString);
            subjectTextNotes.setText(myString);
            ArrayList<String> pdfs = NoteTaker.GetAllPDFs();
            ArrayList<String> notes = NoteTaker.GetAllNotes();
            ArrayList<ArrayList<String>> cards = NoteTaker.GetAllCueCards();

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

            CardsListView.getItems().clear();
            for(ArrayList<String> card : cards){
                CardsListView.getItems().add(card.get(0)); // adds the questions to listview
            }
            GoToTabsPage();
        });
    }

    /**
     * Sets the cell in the listview as draggable
     * @param listView listview from UI
     */
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

    /**
     *Methods for other controllers
     */
    public  ListView<String> getNotesListView(){
        return NotesListView;
    }
    public  ListView<String> getCardsListView(){
        return CardsListView;
    }

    // For trashcans
    @FXML
    private void DeleteDragOver(DragEvent event){
        if(event.getGestureSource() != this && event.getDragboard().hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader notesLoader = new FXMLLoader(getClass().getResource("NoteView.fxml"));
            notes = notesLoader.load();
            notesWindow = new Stage();
            notesWindow.setScene(new Scene(notes));
            notesWindow.setTitle("Notes");
            noteController = notesLoader.getController();
            noteController.getTabsController(this); // sets up communication between controllers

            FXMLLoader cardsLoader = new FXMLLoader(getClass().getResource("FlashCardView.fxml"));
            cards = cardsLoader.load();
            cardsWindow = new Stage();
            cardsWindow.setScene(new Scene(cards));
            cardsWindow.setTitle("Flashcards");
            flashCardsController = cardsLoader.getController();
            flashCardsController.getTabsController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Subjects = NoteTaker.GetAllSubjectNames();

        setCellDragable(PDFListView);
        setCellDragable(NotesListView);
        setCellDragable(CardsListView);

        handleSubjectPage(); // goes to subject page at the start
    }
}

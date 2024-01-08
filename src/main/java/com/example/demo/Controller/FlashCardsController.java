package com.example.demo.Controller;

import com.example.demo.UserInput.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FlashCardsController implements Initializable {
    /**
     * TOMMY OJO AND SAKHANA
     */

    /**
     * For Flash cards
     */
    @FXML
    private Pane AddFlashCardPane, StudyFlashCardPane, ModifyFlashCardPane;
    @FXML
    private TextArea createQuestion, createAnswer, modifyQuestion, modifyAnswer;
    @FXML
    private Label question, answer;

    private boolean question_showing;
    private String modifyString;
    private TabsController tabsController;


    /**
     * Shows Add Flashcard window
     */
    public void AddButton(){
        AddFlashCardPane.setVisible(true);
        StudyFlashCardPane.setVisible(false);
        ModifyFlashCardPane.setVisible(false);
    }

    /**
     * Shows Study Flashcard window
     */
    public void StudyButton(){
        StudyFlashCardPane.setVisible(true);
        AddFlashCardPane.setVisible(false);
        ModifyFlashCardPane.setVisible(false);
        handleStudyCard();
    }

    /**
     * Shows Modify Flashcard window
     */
    public void ModifyButton(String question, String answer){
        StudyFlashCardPane.setVisible(false);
        AddFlashCardPane.setVisible(false);
        ModifyFlashCardPane.setVisible(true);
        modifyQuestion.setText(question);
        modifyAnswer.setText(answer);
        modifyString = question;
    }

    /**
     * Adds a flashcard to the list of flashcards for the subject if it is valid
     */
    @FXML
    private void handleAddCard() {
        ArrayList<ArrayList<String>> cards = NoteTaker.GetAllCueCards();
        boolean add = true;

        String question = createQuestion.getText();

        // ensuring that the questions are unique
        for(ArrayList<String> card : cards){
            if (card.get(0).equals(question)){
                AlertBox.display("Cannot add flashcard", "Flashcard questions have to be unique");
                add = false;
            }
        }

        if(add){
            if(NoteTaker.AddCueCard(createQuestion.getText(), createAnswer.getText()) == 0){
                tabsController.getCardsListView().getItems().add(createQuestion.getText());
            }
            createAnswer.clear();
            createQuestion.clear();
        }
    }

    /**
     * Updates view for Studying
     */
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

    /**
     * Modifies a flashcard  if it is valid
     */
    @FXML
    private void handleModifyCard(){
        String newQuestion = modifyQuestion.getText();
        String newAnswer = modifyAnswer.getText();
        boolean modify = true;
        ArrayList<ArrayList<String>> cards = NoteTaker.GetAllCueCards();

        System.out.println(modifyString);
        System.out.println(newQuestion);

        // ensuring that the questions are unique
        for(ArrayList<String> card : cards){
            if (card.get(0).equals(newQuestion) && !(newQuestion.equals(modifyString))){
                AlertBox.display("Cannot modify flashcard", "Flashcard questions have to be unique");
                modify = false;
            }
        }

        if(modify){
            for(ArrayList<String> card : cards) {
                if (card.get(0).equals(modifyString)) {
                    NoteTaker.ChangeCard(card.get(0), card.get(1), newQuestion, newAnswer);
                    tabsController.getCardsListView().getItems().remove(card.get(0));
                    tabsController.getCardsListView().getItems().add(newQuestion);
                    modifyString = newQuestion;
                }
            }
        }


    }

    @FXML
    private void handleFlip(){
        question_showing = !question_showing;
        handleStudyCard();
    }

    @FXML
    private void handleShuffleCard() {
        NoteTaker.RandomizeCards();
        ArrayList<String > card = NoteTaker.GetNextCard();
        if(card.size() != 0){
            question.setText(card.get(0));
            answer.setText(card.get(1));
        }
        handleStudyCard();
    }
    @FXML
    private void handlePreviousCard() {
        ArrayList<String> card = NoteTaker.GetPreviousCard(); // 0 is question, 1 is answer
        if(card.size() != 0){
            question.setText(card.get(0));
            answer.setText(card.get(1));
        }
        handleStudyCard();
    }
    @FXML
    private void handleNextCard() {
        ArrayList<String> card = NoteTaker.GetNextCard();
        if(card.size() != 0){
            question.setText(card.get(0));
            answer.setText(card.get(1));
        }
        handleStudyCard();
    }

    public void getTabsController(TabsController tabsController) {
        this.tabsController = tabsController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        question.setText("Press the arrows to study!");
        answer.setText("C'mon do it!");
        question_showing = true;
    }

}

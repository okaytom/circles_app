package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FlashCardsController implements Initializable {
    /**
     * For Flash cards
     */
    @FXML
    private Pane AddFlashCardPane, StudyFlashCardPane;
    @FXML
    private TextArea createQuestion, createAnswer;
    @FXML
    private Button shuffle_btn, prev_card_btn, next_card_btn, add_card_btn;
    @FXML
    private Button flip_btn;
    @FXML
    private Label question, answer;

    @FXML
    private Button add_btn, study_btn;

    private boolean question_showing;

    private TabsController tabsController;


    @FXML
    private void handleFlip(ActionEvent event){
        if (event.getSource() == flip_btn){
            question_showing = !question_showing;
            handleStudyCard();
        }
    }

    public void handleStudyCard() {
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

    }

    @FXML
    private void handleAddCard(ActionEvent event) {
        if (event.getSource() == add_card_btn){
            //TODO enforce that questions are unique?

            if(NoteTaker.AddCueCard(createQuestion.getText(), createAnswer.getText()) == 0){
                tabsController.getCardsListView().getItems().add(createQuestion.getText());
            }
            createAnswer.clear();
            createQuestion.clear();
        }
    }

    public void AddButton(){
        AddFlashCardPane.setVisible(true);
        StudyFlashCardPane.setVisible(false);
    }

    public void StudyButton(){
        StudyFlashCardPane.setVisible(true);
        AddFlashCardPane.setVisible(false);
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

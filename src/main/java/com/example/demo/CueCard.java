package com.example.demo;


/***
 * a cue card for studying
 */
public class CueCard {
    public String answer;
    public String question;


    public CueCard(String question, String answer){
        this.answer = answer;
        this.question = question;
    }

    /***
     * changes the question and answer for the cue card
     * @param newQuestion the new question
     * @param newAnswer the new answer
     */
    public void ChangeCard(String newQuestion, String newAnswer){
        this.answer = newAnswer;
        this.question = newQuestion;
    }

    /***
     * gets the question
     * @return the question
     */
    public String GetQuestion(){return this.question;}

    /***
     * gets the answer for the question
     * @return the answer to the question
     */
    public String GetAnswer(){return this.answer;}
}

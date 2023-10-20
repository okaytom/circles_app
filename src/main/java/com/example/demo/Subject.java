package com.example.demo;

import java.io.File;
import java.util.ArrayList;





/***
 * TODO: check for existing an existing cue cards
 * TODO: move the creation of files to Notetaker
 * TODO: add cue card method stubs
 */

/***
 * represents a class at school
 */
public class Subject extends SaveState{


    public String name;


    private String filePath;
    private String notesPath;//TODO

    public String cardPath;//TODO: change to private after testing
    public ArrayList<CueCard> cueCardsList;


    //user interacts with cue cards
    private CueCard currentCard;
    private ArrayList<CueCard> practiceList;


    /***
     * represents a class at school and creates a folder for that class
     * @param name the name of the subject
     */
    public Subject(String name){

        //checking for invalid input
        if (name.isBlank()){name = "new folder";}


        //TODO prevent user from messing with dev files
        if(name.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");

        }


        this.name = name;

        //creating folders
        this.filePath = appName + "/" + name;

        File subjectDir = new File(this.filePath + "/notes");

        if (!subjectDir.mkdirs()){

            if (!subjectDir.exists()){System.out.println("Failed to create the directory for " + name);}
            else{
                this.cardPath = this.filePath + "/CueCards.json";

                if (new File(this.cardPath).exists()){this.cueCardsList = Load(this.cardPath, CueCard.class);}
                else{Save(this.cardPath, new ArrayList<>());}//create the json file

            }

        }
        else{//adding cue cards
            this.cueCardsList = new ArrayList<>();
            this.cardPath = this.filePath + "/CueCards.json";
            Save(this.cardPath, this.cueCardsList);//creating CueCards.json

        }
    }


    /***
     * changes the name of the subject and its file
     * @param newName the new name for the subject
     * @return 0 if succeeds, -1 if error, -2 if a file already exists with the new name, -3 if the user tried to edit the dev folder
     */
    public int ChangeName(String newName){


        //checking for invalid input
        if (newName.isBlank()){newName = "new folder";}
        if (newName.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");
            return -3;
        }


        //renaming the file
        File subjectDir = new File (this.filePath);
        File newDir = new File (appName + "/" + newName);

        if (!newDir.exists()) {

            if (subjectDir.renameTo(newDir)) {
                this.name = newName;
                this.filePath = appName + "/" + newName;

                return 0;

            }//failed to rename files
            else {
                System.out.println("something went wrong when renaming the file " + this.name + " to " + newName);
                return -1;
            }
        }
        else{//file already exists
            System.out.println("cannot change " + this.name + " to " + newName + " because a file with that name already exists");
            return -2;
        }
    }



    /***
     * deletes the file created for the subject and all of its contents
     */
    public void DeleteSubjectFolder(){
        File subjectDir = new File (this.filePath);
        RecursiveDelete(subjectDir.listFiles());

        subjectDir.delete();
    }

    /***
     * recursively goes through the directory, deleting its contents
     * @param files files to be deleted
     */
    private static void RecursiveDelete(File[] files){
        for (int x = 0; x < files.length; x++){
            if(files[x].isDirectory()){
                RecursiveDelete(files[x].listFiles());//delete contents of subdirectory
            }

            files[x].delete();
        }
    }






    // Cue card methods

    /***
     * allows other classes to get a list of all the cue cards for a subject
     * @return a list of the contents of cue cards
     */
    public ArrayList<ArrayList<String>> GetAllCueCards(){

//        if (this.cueCardsList.isEmpty()) {this.cueCardsList = Load(this.cardPath, CueCard.class);}//making sure the old cue cards were loaded

        ArrayList<ArrayList<String>> cueCards = new ArrayList<>();


        this.cueCardsList.forEach(card ->{
            ArrayList<String> newCard = new ArrayList<>();
            newCard.add(card.GetQuestion());
            newCard.add(card.GetAnswer());

            cueCards.add(newCard);
        });

        return cueCards;
    }



//TODO: add return values to indicate the outcome
    /***
     * creates a new cue card and stores it in an array list and json file
     * @param question the question on the cue card
     * @param answer the answer for the question
     * @return TODO
     */
    public void AddCueCard(String question, String answer){
        //TODO
        if (question.isBlank() || answer.isBlank()){//checking for invalid input
            System.out.println("to create a cue card, user needs to provide a question and answer");
            return;
        }

//        if (this.cueCardsList.isEmpty()) {this.cueCardsList = Load(this.cardPath, CueCard.class);}//making sure the old cue cards were loaded


        this.cueCardsList.add(new CueCard(question, answer));

        if (!Save(this.cardPath, this.cueCardsList)){
            System.out.println("something went wrong with saving to " + this.cardPath);
            return;
        }


    }


    /***
     * changes the content of a cue card given when given a way to find that cue card
     * @param oldQuestion the original question
     * @param oldAnswer the original answer
     * @param newQuestion the new question
     * @param newAnswer the new answer
     * @return TODO
     */
    public void ChangeCard(String oldQuestion, String oldAnswer, String newQuestion, String newAnswer){
        //TODO

        CueCard testCard = new CueCard(oldQuestion, oldAnswer);//for comparing to the cards in ArrayList

        this.cueCardsList.forEach(card ->{
            if (testCard.compareTo(card) == 0){
                card.ChangeCard(newQuestion, newAnswer);
                return;//TODO
            }
        });


//        if (this.cueCardsList.isEmpty()) {this.cueCardsList = Load(this.cardPath, CueCard.class);}//making sure the old cue cards were loaded
        return;//TODO
    }


    public void ChangeCard(String newQuestion, String newAnswer){
        //TODO
    }


    /***
     * removes a cue card given a way to find that cue card
     * @param question the question of the cue card to be removed
     * @param answer the answer of the question to be removed
     * @return TODO
     */
    public void RemoveCard(String question, String answer){

        if (question.isBlank() || answer.isBlank()){//Invalid input
            System.out.println("invalid input for RemoveCard");
            return;//TODO
        }

//        if (this.cueCardsList.isEmpty()) {this.cueCardsList = Load(this.cardPath, CueCard.class);}//making sure the old cue cards were loaded

        if (this.cueCardsList.isEmpty()){
            System.out.println("no cue cards to remove for " + this.name);return;//TODO
        }


        CueCard testCard = new CueCard(question, answer);//for comparing to the cards in ArrayList

        //searching for the card to remove
        for (int i = 0; i < this.cueCardsList.size(); i++){
            if (this.cueCardsList.get(i).compareTo(testCard) == 0){
                this.cueCardsList.remove(i);
                return;//TODO
            }
        }

        return;//TODO: no card found
    }

    public void RemoveCard(){
        //TODO
    }




    public String NextQuestion(){
        //TODO

        if(cueCardsList.size() == 0){
            return "No cue cards have been made";
        }

        return "temp";
    }

    public String GetAnswer(){
        //TODO

        if(cueCardsList.size() == 0){
            return "No cue cards have been made";
        }


        return "temp";
    }

//testing
//    public static void main(String[] args){
//
//        try {//removing past test
//            new Subject("cmpt111").DeleteSubjectFolder();
//        }catch(Exception e){}
//
//        Subject cmpt111 = new Subject("cmpt111");
//
//        Subject nothing = new Subject("");
//
//
////        cmpt111.ChangeName("econ111");
//
//        cmpt111.AddCueCard("q1", "a1");
//        cmpt111.AddCueCard("q2", "a2");
//        cmpt111.AddCueCard("q3", "a3");
//
//        cmpt111.RemoveCard("q2", "a2");
//
//        ArrayList<ArrayList<String>> testCardList = cmpt111.GetAllCueCards();
//        testCardList.forEach(x ->{
//            System.out.println(x.get(0) + ", " + x.get(1));
//        });
//
//
//    }
}

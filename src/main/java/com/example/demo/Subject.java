//Created by Tyler Chow

package com.example.demo;

import java.io.File;
import java.util.ArrayList;

import java.util.concurrent.ThreadLocalRandom;


//TODO: implement notes

/***
 * represents a class at school
 */
public class Subject extends SaveState{

    private String name;

    public String filePath;
    private String notesPath;//TODO

    public String cardPath;
    private ArrayList<CueCard> cueCardsList;


    //user interacts with cue cards
    private boolean updated = true;//for checking if cueCardList was changed since practiceList was last updated
    private transient CueCard currentCard;
    private transient ArrayList<CueCard> practiceList;


    /***
     * represents a class at school and creates a folder for that class, name will be an empty string if it failed
     * @param name the name of the subject
     */
    public Subject(String name){

        this.name = name;

        //creating folders
        this.filePath = appName + "/" + name;

        File subjectDir = new File(this.filePath + "/notes");

        if (!subjectDir.mkdirs()){// creating the folders/ checking if they already exist

            if (!subjectDir.exists()){
                System.out.println("Failed to create the directory for " + name);
                this.name = "";//using to represent failure
            }
            else{//might not be possible to happen
                this.cardPath = this.filePath + "/CueCards.json";

                if (new File(this.cardPath).exists()) {//loading the existing cue cards
                    this.cueCardsList = Load(this.cardPath, CueCard.class);
                }
                else{Save(this.cardPath, new ArrayList<>());}//create the json file

                this.practiceList = new ArrayList<>();
            }

        }
        else if (subjectDir.exists()){//preexisting subject file
            this.cueCardsList = new ArrayList<>();
            this.cardPath = this.filePath + "/CueCards.json";
            Save(this.cardPath, this.cueCardsList);//creating CueCards.json

            this.practiceList = new ArrayList<>();
        }
        else { //making the folders failed and the folders didn't already exist
            System.out.println("Failed to create the directory for " + name);
            this.name = "";//using to represent failure
        }
    }



    /***
     * get method for the name of the subject
     * @return name of the subject
     */
    public String GetName(){
        return this.name;
    }



    /***
     * changes the name of the subject and its file
     * @param newName the new name for the subject
     * @return 0 if succeeds, -1 if error, -2 if a file already exists with the new name
     */
    public int ChangeName(String newName){


        //checking for invalid input
        if (newName.isBlank()){newName = "new folder";}
        if (newName.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");
            return -2;
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
            return -2;//TODO set value unique to this
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
        ArrayList<ArrayList<String>> cueCards = new ArrayList<>();


        this.cueCardsList.forEach(card ->{
            ArrayList<String> newCard = new ArrayList<>();
            newCard.add(card.GetQuestion());
            newCard.add(card.GetAnswer());

            cueCards.add(newCard);
        });

        return cueCards;
    }



    /***
     * creates a new cue card and stores it in an array list and json file
     * @param question the question on the cue card
     * @param answer the answer for the question
     * @return 0 on success, -1 on error, -2 for invalid input
     */
    public int AddCueCard(String question, String answer){
        if (question.isBlank() || answer.isBlank()){//checking for invalid input
            System.out.println("to create a cue card, user needs to provide a question and answer");
            return -2;
        }

        if (this.practiceList == null){this.practiceList = new ArrayList<>();}

        CueCard newCard = new CueCard(question, answer);
        this.cueCardsList.add(newCard);
        this.practiceList.add(newCard);

        if (!Save(this.cardPath, this.cueCardsList)){
            System.out.println("failed to save the changes to " + this.cardPath + " when adding a cue card");
            return -1;
        }

        this.updated = true;

        return 0;
    }


    /***
     * changes the content of a cue card given when given a way to find that cue card
     * @param oldQuestion the original question
     * @param oldAnswer the original answer
     * @param newQuestion the new question
     * @param newAnswer the new answer
     * @return 0 if success, -1 if there was an issue saving, -3 if the card wasn't found
     */
    public int ChangeCard(String oldQuestion, String oldAnswer, String newQuestion, String newAnswer){
        CueCard testCard = new CueCard(oldQuestion, oldAnswer);//for comparing to the cards in ArrayList


        int index = 0;

        //searching for the desired card
        while (index < this.cueCardsList.size()){
            CueCard card = this.cueCardsList.get(index);

            if (card.compareTo(testCard) == 0){//found the right card
                card.ChangeCard(newQuestion, newAnswer);

                //saving the changes
                if(! Save(this.cardPath, this.cueCardsList)){
                    System.out.println("failed to save the changes to " + this.cardPath + " when changing a cue card");
                    return -1;
                }


                this.updated = true;
                return 0;
            }

            index += 1;
        }

        return -3;
    }





    /***
     * removes a cue card given a way to find that cue card
     * @param question the question of the cue card to be removed
     * @param answer the answer of the question to be removed
     * @return 0 if success, -1 if there was an issue saving, -2 for invalid input, -3 if the cue card wasn't found
     */
    public int RemoveCard(String question, String answer){

        if (question.isBlank() || answer.isBlank()){//Invalid input
            System.out.println("invalid input for RemoveCard");
            return -2;
        }

        if (this.cueCardsList.isEmpty()){
            System.out.println("no cue cards to remove for " + this.name);return -3;
        }


        CueCard testCard = new CueCard(question, answer);//for comparing to the cards in ArrayList

        //searching for the card to remove
        for (int i = 0; i < this.cueCardsList.size(); i++){
            if (this.cueCardsList.get(i).compareTo(testCard) == 0){

                if (this.currentCard != null && this.cueCardsList.get(i).compareTo(this.currentCard) == 0){this.currentCard = null;}

                this.cueCardsList.remove(i);

                //save changes
                if(!Save(this.cardPath, this.cueCardsList)){
                    System.out.println("failed to save the changes to " + this.cardPath + " when removing a cue card");
                    return -1;
                }

                this.updated = true;
                return 0;
            }
        }

        return -3;
    }





    //methods for cue cards study mode

    /***
     * gets the next card in the list when studying
     * @return the next question and answer or an empty ArrayList if there was an error
     */
    public ArrayList<String> GetNextCard(){
        ArrayList<String> card = new ArrayList<>();

        //checking for errors
        if(this.cueCardsList.size() == 0){
            System.out.println("No cue cards have been made");
            return card;
        }

        if (this.practiceList == null){this.practiceList = new ArrayList<>();}



        //making sure that practice list is up to date
        if (this.updated){
            this.practiceList.clear();
            this.practiceList.addAll(this.cueCardsList);

            this.updated = false;
        }

        //changing the current card
        if (this.currentCard == null) {this.currentCard = this.practiceList.get(0);}
        else{//increase the index by one
            this.currentCard = this.practiceList.get((this.practiceList.indexOf(this.currentCard) + 1) % this.practiceList.size());
        }

        card.add(this.currentCard.GetQuestion());
        card.add(this.currentCard.GetAnswer());

        return card;
    }

    /***
     * gets the previous card in the list when studying
     * @return the previous question and answer or an empty ArrayList if there was an error
     */
    public ArrayList<String> GetPreviousCard(){

        ArrayList<String> card = new ArrayList<>();

        //checking for errors
        if(this.cueCardsList.size() == 0){
            System.out.println("No cue cards have been made");
            return card;
        }

        if (this.practiceList == null){this.practiceList = new ArrayList<>();}



        //making sure that practice list is up to date
        if (this.updated){
            this.practiceList.clear();
            this.practiceList.addAll(this.cueCardsList);

            this.updated = false;
        }

        //changing the current card
        if (this.currentCard == null) {this.currentCard = this.practiceList.get(0);}
        else{
            if (this.practiceList.indexOf(this.currentCard) == 0){//wrap around to end
                this.currentCard = this.practiceList.get(this.practiceList.size() - 1);
            }
            else{
                this.currentCard = this.practiceList.get(this.practiceList.indexOf(this.currentCard) - 1);
            }
        }

        card.add(this.currentCard.GetQuestion());
        card.add(this.currentCard.GetAnswer());

        return card;
    }


    /***
     * randomizes the order of the cue cards for study mode
     */
    public void RandomizeCards(){

        //checking for errors
        if(this.cueCardsList.size() <= 1){
            System.out.println("Not enough cards to randomize");
            return;
        }

        if (this.practiceList == null){this.practiceList = new ArrayList<>();}



        //making sure that practice list is up to date
        if (this.updated){
            this.practiceList.clear();
            this.practiceList.addAll(this.cueCardsList);

            this.updated = false;
        }


        ArrayList<CueCard> tempList = new ArrayList<>();

        //randomizing the cards
        while (this.practiceList.size() > 0){
            int card = ThreadLocalRandom.current().nextInt(0, this.practiceList.size());

            tempList.add(this.practiceList.get(card));
            this.practiceList.remove(card);
        }

        this.practiceList.addAll(tempList);
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
////        cmpt111.RemoveCard("q2", "a2");
//
////        cmpt111.RandomizeCards();
//
//
//        for (int i = 0; i < 3; i++){
//            System.out.println(cmpt111.GetNextCard());
//        }
//
//
//
//        ArrayList<ArrayList<String>> testCardList = cmpt111.GetAllCueCards();
//        testCardList.forEach(x ->{
//            System.out.println("\n" + x.get(0) + ", " + x.get(1));
//        });
//
//
//        System.out.println("done tests");
//    }
}

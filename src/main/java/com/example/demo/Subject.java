package com.example.demo;

import java.io.File;
import java.util.ArrayList;

/***
 * represents a class at school
 */


public class Subject extends SaveState{


    public String name;


    private String filePath;
    private String notesPath;//TODO

    private String cardPath;//TODO
    private ArrayList<CueCard> cueCardsList;


    //user interacts with cue cards
    private CueCard currentCard;
    private ArrayList<CueCard> practiceList;


    public Subject(String name){

        //checking for invalid input
        if (name.isBlank()){name = "new folder";}

        this.name = name;
        //TODO prevent user from messing with dev files
//        name.contains()

        //creating folders
        this.filePath = appName + "/" + name;

        File subjectDir = new File(this.filePath + "/notes");

        if (!subjectDir.mkdirs()){
            if (!subjectDir.exists()){System.out.println("Failed to create the directory for " + name);}
        }
        else{//adding cue cards
            this.cueCardsList = new ArrayList<>();
            this.cardPath = this.filePath + "/CueCards";
        }
    }


    /***
     * changes the name of the subject and its file
     * @param newName the new name for the subject
     * @return 0 if succeeds, 1 if error, 2 if a file already exists with the new name
     */
    public int ChangeName(String newName){
        //TODO prevent user from messing with dev folder and empty name

        //checking for invalid input
        if (newName.isBlank()){newName = "new folder";}


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
                return 1;
            }
        }
        else{//file already exists
            System.out.println("cannot change " + this.name + " to " + newName + " because a file with that name already exists");
            return 2;
        }
    }




    /***
     * deletes the file created for the subject and all of its contents
     */
    public void Delete(){
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


    /***
     * creates a new cue card and stores it in an array list and json file
     * @param question
     * @param answer
     */
    public void AddCueCard(String question, String answer){
        //TODO
        if (question.isBlank() || answer.isBlank()){//checking for invalid input
            System.out.println("to create a cue card, user needs to provide a question and answer");
        }
        else{
            this.cueCardsList.add(new CueCard(question, answer));

            if (!Save(this.cardPath, this.cueCardsList)){
                System.out.println("something went wrong with saving to " + this.cardPath);
            }
        }

    }

    public void RemoveCurrentCard(){
        //TODO
    }

    public void ChangeCard(){
        //TODO
    }

    public void ChangeCard(String question){
        //TODO
    }

    public String NextQuestion(){
        //TODO

        if(cueCardsList.size() == 0){
            return "No cue cards have been made";
        }

        return "temp";
    }

    public String NextAnswer(){
        //TODO

        if(cueCardsList.size() == 0){
            return "No cue cards have been made";
        }


        return "temp";
    }

//testing
//    public static void main(String[] args){
//        Subject cmpt111 = new Subject("cmpt111");
//
//        Subject nothing = new Subject("");
//
//
//        cmpt111.ChangeName("econ111");
//
//
//    }
}

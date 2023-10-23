//Created by Tyler Chow
package com.example.demo;

import java.util.ArrayList;




//TODO: test each method
//TODO: set up the return values and header for each method

public class NoteTaker extends SaveState{

    public static ArrayList<Subject> subjectList;

    public static String subjectListFilePath = SaveState.devFolder + "/Subjects.json";


    private static Subject currentSubject;


    public static ArrayList<String> GetAllSubjectNames(){
        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}

        ArrayList<String> nameList = new ArrayList<>();

        subjectList.forEach(sub ->{
            nameList.add(sub.GetName());
        });

        return nameList;
    }

    public static int AddSubject(String name){

        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}


        //checking for invalid input
        if (name.isBlank()){name = "new folder";}

        if(name.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");
            return -1;//TODO set up return value
        }



        Subject newSubject = new Subject(name);

        if (newSubject.GetName().isBlank()){//failed to create a new Subject
            return -1; //TODO set up return value
        }
        else{//add the new subject to the list and save it
            subjectList.add(newSubject);
            if (!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to save subject: " + name + " to the json file");
                return -1; //TODO set up return value
            }
            return 0; //TODO set up return value
        }
    }


    //TODO
    /***
     * changes the current subject
     * @param name the name of the desired subject
     * @return 0 if successful, -1 if it couldn't find a subject with that name
     */
    public static int ChangeSubject (String name){

        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}


        if (subjectList.isEmpty()){
            System.out.println("cannot change the current subject when no subjects have been created");
            return -2;//TODO set up return value
        }


        //searching for the desired subject
        int index = 0;

        while (index < subjectList.size()){
            Subject sub = subjectList.get(index);

            if (sub.GetName().equals(name)){
                currentSubject = sub;
                return 0; //TODO set up return value
            }

            index += 1;
        }

        //failed to find the desired subject
        return -1; //TODO set up return value
    }



//TODO
    public static int RemoveSubject(){
        if (currentSubject != null){
            if(subjectList.remove(currentSubject)){
                currentSubject = null;

                if(!Save(subjectListFilePath, subjectList)){
                    System.out.println("failed to update " + subjectListFilePath + " when removing a subject");
                    return -1;
                }

                return 0;
            }
        }

        return -1;//TODO set up return value
    }



    public static int DeleteSubjectFolder(){
        //TODO
        if (currentSubject != null){
            currentSubject.DeleteSubjectFolder();
            return RemoveSubject();
        }

        return -1;
    }



    //TODO
    public static int ChangeSubjectName(String newName){

        if (currentSubject == null){
            System.out.println("need to select a subject before you can change its name");
            return -1;//TODO set up return values
        }

        int result = currentSubject.ChangeName(newName);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to update " + subjectListFilePath + " when changing the name of " + currentSubject.GetName());
            }

        }
        return result;
    }


    //TODO
    public static String GetName(){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its name");
            return "";//TODO set up return values
        }
        return currentSubject.GetName();
    }


    //TODO
    public static ArrayList<ArrayList<String>> GetAllCueCards(){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its cue cards");
            return new ArrayList<ArrayList<String>>();//TODO set up return values
        }
        return currentSubject.GetAllCueCards();
    }

    //TODO
    public static int AddCueCard(String question, String answer){

        if (currentSubject == null){
            System.out.println("need to select a subject before you can create a cue card");
            return -1;//TODO set up return values
        }

        int result = currentSubject.AddCueCard(question, answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to update " + subjectListFilePath + " when adding a cue card from " + currentSubject.GetName());
            }

        }
        return result;
    }

    //TODO
    public static int ChangeCard(String oldQuestion, String oldAnswer, String newQuestion, String newAnswer){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can change a cue card");
            return -1;//TODO set up return values
        }


        int result = currentSubject.ChangeCard(oldQuestion, oldAnswer, newQuestion, newAnswer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to update " + subjectListFilePath + " when changing a cue card from " + currentSubject.GetName());
            }

        }
        return result;
    }

    //TODO
    public static int RemoveCard (String question, String Answer){

        if (currentSubject == null){
            System.out.println("need to select a subject before you can remove a cue card");
            return -1;//TODO set up return values
        }

        int result = currentSubject.RemoveCard(question, Answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to update " + subjectListFilePath + " when removing a cue card from " + currentSubject.GetName());
            }

        }
        return result;
    }


    //TODO
    public static ArrayList<String> GetNextCard(){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            return new ArrayList<String>();//TODO set up return values
        }

        return currentSubject.GetNextCard();
    }


    //TODO
    public static ArrayList<String> GetPreviousCard(){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            return new ArrayList<String>();
        }

        return currentSubject.GetPreviousCard();
    }


    //TODO
    public static void RandomizeCards(){
        if (currentSubject == null){
            System.out.println("need to select a subject before you can randomize the cue cards");
        }
        else {currentSubject.RandomizeCards();}
    }








    //testing
    public static void main(String[] args){

    }

}

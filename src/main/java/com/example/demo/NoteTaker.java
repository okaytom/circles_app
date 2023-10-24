//Created by Tyler Chow
package com.example.demo;

import java.util.ArrayList;




//TODO: test each method
//TODO: set up consistent return values and header for each method

public class NoteTaker extends SaveState{

    private static ArrayList<Subject> subjectList = new ArrayList<>();

    public static String subjectListFilePath = SaveState.devFolder + "/Subjects.json";


    private static Subject currentSubject;

    //TODO
    public static ArrayList<String> GetAllSubjectNames(){
        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}

        ArrayList<String> nameList = new ArrayList<>();

        subjectList.forEach(sub ->{
            nameList.add(sub.GetName());
        });

        return nameList;
    }

    //TODO
    public static int AddSubject(String name){

        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}


        //checking for invalid input
        if (name.isBlank()){name = "new folder";}

        if(name.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");
            return -1;//TODO set up return value
        }

        //checking if the subject already exists
        int index = 0;
        while (index < subjectList.size()){
            if (subjectList.get(index).GetName().equals(name)){return -1;}//TODO set up return value
            index += 1;
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
        if (currentSubject != null){//checking that a subject was selected
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
        //checking that a subject was selected
        if (currentSubject != null){
            currentSubject.DeleteSubjectFolder();
            return RemoveSubject();
        }

        return -1;
    }



    //TODO
    public static int ChangeSubjectName(String newName){

        //checking that a subject was selected
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

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its name");
            return "";//TODO set up return values
        }
        return currentSubject.GetName();
    }


    //TODO
    public static ArrayList<ArrayList<String>> GetAllCueCards(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its cue cards");
            return new ArrayList<ArrayList<String>>();//TODO set up return values
        }
        return currentSubject.GetAllCueCards();
    }

    //TODO
    public static int AddCueCard(String question, String answer){

        //checking that a subject was selected
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

        //checking that a subject was selected
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

        //checking that a subject was selected
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

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            return new ArrayList<String>();//TODO set up return values
        }

        return currentSubject.GetNextCard();
    }


    //TODO
    public static ArrayList<String> GetPreviousCard(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            return new ArrayList<String>();
        }

        return currentSubject.GetPreviousCard();
    }


    //TODO
    public static void RandomizeCards(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can randomize the cue cards");
        }
        else {currentSubject.RandomizeCards();}
    }








    //testing
    public static void main(String[] args){

        boolean pass;
        int numPassed = 0;
        int numTested = 0;

        //cleaning up past test run
        String[] fileList = {"test subject 1", "test subject 2", "test subject 3"};
        for (int x = 0; x < fileList.length; x++){
            ChangeSubject(fileList[x]);
            DeleteSubjectFolder();
        }

        //testing AddSubject and GetAllSubjectNames
        AddSubject("test subject 0");
        AddSubject("test subject 1");
        AddSubject("test subject 10");
        AddSubject("test subject 2");
        numTested += 1;

        if (GetAllSubjectNames().size() != 4){
            System.out.println("there is an issue with either AddSubject or GetAllSubjectNames\nresults:");
            System.out.println(GetAllSubjectNames());
        }
        else{numPassed += 1;}


        //testing what happens when no subject was selected
        pass = true;
        System.out.println("            test case, no subject was selected");
        numTested += 1;

        if (DeleteSubjectFolder() >= 0){
            pass = false;
            System.out.println("DeleteSubjectFolder didn't throw an error when it should have");
        }

        if (ChangeSubjectName("random name") >= 0){
            pass = false;
            System.out.println("ChangeSubjectName didn't throw an error when it was supposed to");
        }

        if (!GetName().isEmpty()){
            pass = false;
            System.out.println("GetName returned " + GetName() + " when it was supposed to be empty");
        }

        if (!GetAllCueCards().isEmpty()){
            pass = false;
            System.out.println("GetAllCueCards returned something when it wasn't supposed to");
        }

        if (AddCueCard("test q", "test a") >= 0){
            pass = false;
            System.out.println("AddCueCard didn't throw an error when it was supposed to");
        }

        if (ChangeCard("q", "a", "Q", "A") >= 0){
            pass = false;
            System.out.println("ChangeCard didn't throw an error when it was supposed to");
        }

        if (RemoveCard("q", "a") >= 0){
            pass = false;
            System.out.println("RemoveCard didn't throw an error when it was supposed to");
        }

        if (!GetNextCard().isEmpty()){
            pass = false;
            System.out.println("GetNextCard returned something when it was supposed to be empty");
        }

        if (!GetPreviousCard().isEmpty()){
            pass = false;
            System.out.println("GetPreviousCard returned something when it was supposed to be empty");
        }

        RandomizeCards();//will throw its own message

        if (pass){
            numPassed += 1;
            System.out.println("passed\n\n\n");
        }




        //testing ChangeSubject
        System.out.println("            testing ChangeSubject");
        System.out.println("testing changing to a subject that doesn't exist");

        numTested += 1;
        if (ChangeSubject("should not exist") < 0){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed\n");}


        System.out.println("testing base case");
        numTested += 1;

        String name = "test subject 10";
        ChangeSubject(name);
        if (GetName().equals(name)){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{
            System.out.println("failed: " + GetName() + "\n");
        }


        System.out.println("\n");


        //testing DeleteSubjectFolder
        System.out.println("            testing DeleteSubjectFolder");

        System.out.println("testing deleting a subject");
        numTested += 1;

        ChangeSubject("test subject 10");
        if (DeleteSubjectFolder() >= 0){
            System.out.println("testing if currentSubject is null using GetName");
            if (GetName().isEmpty() && GetAllSubjectNames().size() == 3){
                numPassed += 1;
                System.out.println("passed\n");
            }
            else{
                System.out.println("failed: test subject 10 is still the currentSubject\n");
            }
        }
        else{
            System.out.println("failed to delete test subject 10\n");
        }








        System.out.println(GetAllSubjectNames());
        System.out.println(numPassed + " / " + numTested + " test cases passed");


        //removing the results of testing
//        ArrayList<String> remainingFiles = GetAllSubjectNames();
//        remainingFiles.forEach(sub ->{
//            ChangeSubject(sub);
//            DeleteSubjectFolder();
//        });
    }

}

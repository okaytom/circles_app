//Created by Tyler Chow
/*
manages a list of Subjects and makes their methods accessible to the front end with static methods
 */


/*
error values
0 - success
-1 - error from something we didn't build (like a function from gson throwing an error when used)
-2 - invalid input
-3 - value wasn't found
 */


package com.example.demo;

import java.util.ArrayList;





//TODO: add any special return values the front end might want to ask the user about
//TODO: add notes

public class NoteTaker extends SaveState implements Searchable{

    private static ArrayList<Subject> subjectList = new ArrayList<>();

    public final static String subjectListFilePath = SaveState.devFolder + "/Subjects.json";//file path to json storing subject objects


    private static Subject currentSubject;//the subject the user is currently interacting with

    /***
     * gets the names of all the Subjects
     * @return list of the names of all the Subjects
     */
    public static ArrayList<String> GetAllSubjectNames(){
        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}

        ArrayList<String> nameList = new ArrayList<>();

        subjectList.forEach(sub ->{
            nameList.add(sub.GetName());
        });

        return nameList;
    }


    /***
     * creates a new Subject and adds it to a list
     * @param name the name of the new subject
     * @return 0 on success, -1 for errors thrown by things we are using, -2 for invalid input,
     */
    public static int AddSubject(String name){

        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}


        //checking for invalid input
        if (name.isBlank()){name = "new folder";}

        if(name.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");
            return -2;
        }

        //checking if the subject already exists
        int index = 0;
        while (index < subjectList.size()){
            if (subjectList.get(index).GetName().equals(name)){return -2;}
            index += 1;
        }



        Subject newSubject = new Subject(name);

        if (newSubject.GetName().isBlank()){//failed to create a new Subject
            return -1;
        }
        else{//add the new subject to the list and save it
            subjectList.add(newSubject);
            if (!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to save subject: " + name + " to the json file");
                return -1;
            }
            return 0;
        }
    }



    /***
     * changes the current subject the user is interacting with
     * @param name the name of the desired subject
     * @return 0 if successful, -2 if there are no subjects created, -3 if it couldn't find a subject with that name
     */
    public static int SelectSubject (String name){

        //making sure previous subjects are loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}


        if (subjectList.isEmpty()){
            System.out.println("cannot change the current subject when no subjects have been created");
            return -2;
        }


        //searching for the desired subject
        int index = 0;

        while (index < subjectList.size()){
            Subject sub = subjectList.get(index);

            if (sub.GetName().equals(name)){//found the desired subject
                currentSubject = sub;
                return 0;
            }

            index += 1;
        }

        //failed to find the desired subject
        return -3;
    }


    /***
     * removes the currentSubject from the list it stores, but it doesn't delete the folder
     * @return 0 on success, -1 if there was an issue saving to Subjects.json
     */
    public static int RemoveSubject() {
        if (currentSubject != null) {//checking that a subject was selected
            if (subjectList.remove(currentSubject)) {
                currentSubject = null;

                if (!Save(subjectListFilePath, subjectList)) {
                    System.out.println("failed to update " + subjectListFilePath + " when removing a subject");
                    return -1;
                }

                return 0;
            }
        }

        return -2;//didn't set the currentSubject
    }


    /***
     * removes the currentSubject from the list it stores and deletes the folder for that Subject
     * @return 0 on success, -1 if there was an issue saving to Subjects.json, -2 if currentSubject was not selected
     */
    public static int DeleteSubjectFolder(){
        //checking that a subject was selected
        if (currentSubject != null){
            currentSubject.DeleteSubjectFolder();
            return RemoveSubject();
        }

        return -2;
    }



    /***
     * changes the name of currentSubject and its folder
     * @param newName the new name for the subject
     * @return 0 on success, -1 if it failed to save to Subject.json, -2 if a file already exists with the new name
     */
    public static int ChangeSubjectName(String newName){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can change its name");
            return -2;
        }

        int result = currentSubject.ChangeName(newName);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){
                System.out.println("failed to update " + subjectListFilePath + " when changing the name of " + currentSubject.GetName());
                return -1;
            }

        }
        return result;
    }


    /***
     * get method for the name of currentSubject
     * @return name of currentSubject
     */
    public static String GetName(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its name");
            return "";//represents an error occurred
        }
        return currentSubject.GetName();
    }


    /***
     * gets a list of all the cue cards for currentSubject
     * @return list of all the cue card for currentSubject or an empty list if there was an error
     */
    public static ArrayList<ArrayList<String>> GetAllCueCards(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get its cue cards");
            return new ArrayList<ArrayList<String>>();//represents an error occurred
        }
        return currentSubject.GetAllCueCards();
    }



    /***
     * creates a new cue card for currentSubject
     * @param question the question on the cue card
     * @param answer the answer for the question
     * @return 0 on success, -1 if there was an issue saving, -2 for invalid input
     */
    public static int AddCueCard(String question, String answer){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can create a cue card");
            AlertBox.display("Error adding CueCard","need to select a subject before you can create a cue card" );
            return -2;
        }

        int result = currentSubject.AddCueCard(question, answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//issue when saving
                System.out.println("failed to update " + subjectListFilePath + " when adding a cue card from " + currentSubject.GetName());
                AlertBox.display("Error Adding CueCard","failed to update " + subjectListFilePath + " when adding a cue card from " + currentSubject.GetName());

                return -1;
            }
        }
        return result;
    }



    /***
     * changes the content of a cue card in currentSubject when given that card's contents
     * @param oldQuestion the original question
     * @param oldAnswer the original answer
     * @param newQuestion the new question
     * @param newAnswer the new answer
     * @return 0 if success, -1 if there was an issue saving, -2 if the currentSubject was not selected or the new question or answer was blank,  -3 if the card wasn't found
     */
    public static int ChangeCard(String oldQuestion, String oldAnswer, String newQuestion, String newAnswer){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can change a cue card");
            return -2;
        }


        int result = currentSubject.ChangeCard(oldQuestion, oldAnswer, newQuestion, newAnswer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//failed to save
                System.out.println("failed to update " + subjectListFilePath + " when changing a cue card from " + currentSubject.GetName());
                return -1;
            }
        }
        return result;
    }


    /***
     * removes a cue card from currentSubject given a way to find that cue card
     * @param question the question of the cue card to be removed
     * @param answer the answer of the question to be removed
     * @return 0 if success, -1 if there was an issue saving, -2 for invalid input, -3 if the cue card wasn't found
     */
    public static int RemoveCard (String question, String answer){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can remove a cue card");
            return -2;
        }

        int result = currentSubject.RemoveCard(question, answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//failed to save
                System.out.println("failed to update " + subjectListFilePath + " when removing a cue card from " + currentSubject.GetName());
                return -1;
            }

        }
        return result;
    }


    /***
     * gets the next card in the list when studying for currentSubject
     * @return the next question and answer or an empty ArrayList if there was an error
     */
    public static ArrayList<String> GetNextCard(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            return new ArrayList<String>();//represents an error has occurred
        }

        return currentSubject.GetNextCard();
    }


    /***
     * gets the previous card in the list when studying for currentSubject
     * @return the previous question and answer or an empty ArrayList if there was an error
     */
    public static ArrayList<String> GetPreviousCard(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can get a cue card");
            AlertBox.display("Error in CueCard", "need to select a subject before you can get a cue card");
            return new ArrayList<String>();
        }

        return currentSubject.GetPreviousCard();
    }


    /***
     * randomizes the order of the cue cards for studying for currentSubject
     */
    public static void RandomizeCards(){

        //checking that a subject was selected
        if (currentSubject == null){
            System.out.println("need to select a subject before you can randomize the cue cards"); //TODO use AlertBox
        }
        else {currentSubject.RandomizeCards();}
    }



    /***
     * gets the notePath for currentSubject
     * @return the notePath
     */
    public static String GetNotesFilePath(){
        return currentSubject.GetNotesFilePath();
    }


    //TODO: change / remove this if we don't want each subject to have a pdf file
    /***
     * gets the pdfPath for currentSubject
     * @return the pdfPath
     */
    public static String GetPDFFilePath(){
        return currentSubject.GetPDFFilePath();
    }

    /***
     * gets a list of all the pdf filenames in currentSubject's pdfs folder without their file extension
     * @return an empty list if no subject was selected or a list of pdf filenames without the file extension
     */
    public static ArrayList<String> GetAllPDFs(){
        if (currentSubject == null){return new ArrayList<>();}
        else {return currentSubject.GetAllPDFs();}
    }

    /***
     * gets a list of all the pdf filenames in currentSubject's notes folder without their file extension
     * @return an empty list if no subject was selected or a list of pdf filenames without the file extension
     */
    public static ArrayList<String> GetAllNotes(){
        if (currentSubject == null){return new ArrayList<>();}
        else{return currentSubject.GetAllNotes();}
    }




    public static String Search(String searchTerm) {

        String results = "Subjects";
        boolean foundSomething = false;

        //making sure the subjects were loaded
        if (subjectList.isEmpty()){subjectList = Load(subjectListFilePath, Subject.class);}

        //searching the Subjects
        int subjectIndex = 0;
        while (subjectIndex < subjectList.size()){

            Subject subject = subjectList.get(subjectIndex);
            boolean addedSubName = false;

            if (subject.GetName().contains(searchTerm)){//checking the name of the Subject
                results = results + "\n\n"  + subject.GetName();
                foundSomething = true;
                addedSubName = true;
            }



            //checking the cue cards
            ArrayList<ArrayList<String>> cards = subject.GetAllCueCards();
            int cardIndex = 0;
            boolean addedCards = false;//for adding Cards title

            while(cardIndex < cards.size()){
                ArrayList<String> card = cards.get(cardIndex);

                //checking the question and answer of a card
                if (card.get(0).contains(searchTerm) || card.get(1).contains(searchTerm)) {

                    if (!addedSubName) {//adding the name of the subject if it wasn't added already
                        results = results + "\n\n"  + subject.GetName();
                        addedSubName = true;
                    }

                    if (!addedCards){//adding title for cards if it hasn't been already
                        results = results + "\ncue cards that contain: " + searchTerm + ":";
                        addedCards = true;
                    }


                    //adding card
                    results = results + "\n\nQuestion: " + card.get(0) + "\nAnswer: " + card.get(1);

                    foundSomething = true;
                }

                cardIndex += 1;
            }



            //TODO: check notes for search term

            subjectIndex += 1;
        }



        if (foundSomething){return results;}


        return searchErrorMsg;
    }












    //testing
    public static void main(String[] args){

        boolean pass;
        int numPassed = 0;
        int numTested = 0;
        int listSize;


        //testing AddSubject and GetAllSubjectNames
        System.out.println("            testing AddSubject");

        System.out.println("testing base case for AddSubject");
        AddSubject("test subject 0");
        AddSubject("test subject 1");
        AddSubject("test subject 10");
        AddSubject("test subject 2");
        numTested += 1;

        if (GetAllSubjectNames().size() != 4){
            System.out.println("there is an issue with either AddSubject or GetAllSubjectNames\nresults:");
            System.out.println(GetAllSubjectNames());
        }
        else{
            numPassed += 1;
            System.out.println("passed\n");
        }



        //testing adding a subject with the same name
        System.out.println("test case: adding the same subject twice");
        numTested += 1;
        if (AddSubject("test subject 0") == -2){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{
            System.out.println("failed: AddSubject didn't throw an error when adding the same subject twice\n");
        }



        //testing what happens when no subject was selected
        pass = true;
        System.out.println("            test case, no subject was selected (DeleteSubjectFolder(), ChangeSubjectName(), GetName(), ChangeCard(), GetAllCueCards(), AddCueCard(), ChangeCard(), RemoveCard(), GetNextCard(), GetPreviousCard()");
        numTested += 1;

        if (DeleteSubjectFolder() >= 0){
            pass = false;
            System.out.println("DeleteSubjectFolder didn't throw an error when it should have");
        }

        if (ChangeSubjectName("random name") != -2){
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

        if (AddCueCard("test q", "test a") != -2){
            pass = false;
            System.out.println("AddCueCard didn't throw an error when it was supposed to");
        }

        if (ChangeCard("q", "a", "Q", "A") != -2){
            pass = false;
            System.out.println("ChangeCard didn't throw an error when it was supposed to");
        }

        if (RemoveCard("q", "a") != -2){
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
        if (SelectSubject("should not exist") == -3){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed\n");}


        System.out.println("testing base case");
        numTested += 1;

        String name = "test subject 10";
        SelectSubject(name);
        if (GetName().equals(name)){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: " + GetName() + "\n");}


        System.out.println("\n");


        //testing DeleteSubjectFolder
        System.out.println("            testing DeleteSubjectFolder");

        System.out.println("testing deleting a subject");
        numTested += 1;

        listSize = GetAllSubjectNames().size();

        SelectSubject("test subject 10");
        if (DeleteSubjectFolder() == 0){
            System.out.println("testing if currentSubject is null using GetName");
            if (GetName().isEmpty() && GetAllSubjectNames().size() == listSize - 1){
                numPassed += 1;
                System.out.println("passed\n");
            }
            else{System.out.println("failed: test subject 10 is still the currentSubject\n");}
        }
        else{System.out.println("failed to delete test subject 10\n");}





        //testing ChangeSubjectName
        System.out.println("            testing ChangeSubjectName");

        System.out.println("testing changing name to an already existing subject");
        numTested += 1;

        SelectSubject("test subject 2");
        if (ChangeSubjectName("test subject 1") == -2){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: changed the name of a subject to one that already exists\n");}


        //empty string for a name
        System.out.println("testing changing the name to an empty string or one with only spaces");
        numTested += 1;

        ChangeSubjectName("");
        if (GetName().equals("new folder")){

            SelectSubject("test subject 2");
            ChangeSubjectName("  ");
            if (GetName().equals("new folder")){
                numPassed += 1;
                System.out.println("passed\n");
            }
            else{System.out.println("failed: did not change the name from a string with only spaces to default name (new folder)\n");}
        }
        else{System.out.println("failed: when passed an empty string, did not set the name to the default one (new folder)\n");}


        //base case
        System.out.println("testing the base case");
        numTested += 1;

        ChangeSubjectName("test subject 2");

        if (GetName().equals("test subject 2")){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed base case of changing new folder to test subject 2\n");}

        System.out.println("\n");





        //testing cue card methods
        System.out.println("        testing cue card methods");
        SelectSubject("test subject 0");


        //testing cue card methods when no cue cards were added
        System.out.println("testing methods when no cards are added: ChangeCard, RemoveCard, GetNextCard, GetPreviousCard, and RandomizeCards");
        numTested += 1;
        pass = true;

        if (RemoveCard("q", "a") != -3){
            pass = false;
            System.out.println("failed: RemoveCard didn't throw an error when it was supposed to");
        }

        if (ChangeCard("q", "a", "Q", "A") != -3){
            pass = false;
            System.out.println("failed: ChangeCard didn't throw an error when it was supposed to");
        }

        if (!GetNextCard().isEmpty()){
            pass = false;
            System.out.println("failed: GetNextCard didn't return an empty array list when it was supposed to");
        }

        if (!GetPreviousCard().isEmpty()){
            pass = false;
            System.out.println("failed: GetPrevious didn't return an empty array list when it was supposed to");
        }

        RandomizeCards();//doesn't return anything, will print error message


        if (pass){
            numPassed += 1;
            System.out.println("passed");
        }

        System.out.println();//for readability



        //testing AddCueCard and GetAllCueCards
        System.out.println("testing AddCueCards base case and GetAllCueCards");


        numTested += 1;

        SelectSubject("test subject 1");

        listSize = GetAllCueCards().size();

        AddCueCard("1 q1", "1 a1");
        AddCueCard("1 q2", "1 a2");
        AddCueCard("1 q", "1 a");


        if (GetAllCueCards().size() == listSize + 3){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{
            System.out.println("failed: didn't add the correct number of cards\n");
            System.out.println(GetAllCueCards());
        }



        System.out.println("testing adding a cue card with a blank question or answer");
        numTested += 1;
        pass = true;

        if (AddCueCard("", "a") != -2 || AddCueCard("q", "") != -2){
            pass = false;
        }

        if (AddCueCard("   ", "a") != -2 || AddCueCard("q", "    ") != -2){
            pass = false;
        }

        if (pass){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{
            System.out.println("failed: added a cue card with a blank question or answer");
        }




        //testing ChangeCard()
        System.out.println("testing changing the cue card of another subject");
        numTested += 1;

        SelectSubject("test subject 2");

        if (ChangeCard("1 q1", "1 a1", "Q", "A") == -3){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: ChangeCard didn't throw an error when it was supposed to");}



        //base case
        SelectSubject("test subject 1");

        System.out.println("testing base case for ChangeCard");
        numTested += 1;

        if (ChangeCard("1 q", "1 a", "1 q3", "1 a3") == 0){
            ArrayList<ArrayList<String>> cards = GetAllCueCards();
            if (cards.get(2).get(0).equals("1 q3") && cards.get(2).get(1).equals("1 a3")){
                numPassed += 1;
                System.out.println("passed\n");
            }
            else{System.out.println("failed: ChangeCard didn't change the cue card to the right question and answer\n");}
        }
        else{System.out.println("failed: ChangeCard didn't change the cue card\n");}



        System.out.println("testing changing a cue card to have a blank question or answer");
        numTested += 1;
        pass = true;

        if (ChangeCard("1 q2", "1 a2", "", "A") != -2 || ChangeCard("1 q2", "1 a2", "Q", "") != -2){
            pass = false;
        }

        if (ChangeCard("1 q2", "1 a2", "     ", "A") != -2 || ChangeCard("1 q2", "1 a2", "Q", "     ") != -2){
            pass = false;
        }

        if (pass){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{
            System.out.println("failed: changed a cue card to have a blank question or answer");
        }




        //testing RemoveCard
        System.out.println("testing RemoveCard for a card that doesn't exist");
        numTested += 1;
        if (RemoveCard("qw", "aw") == -3){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: RemoveCard didn't throw an error when it was supposed to\n");}


        System.out.println("testing RemoveCard base case");
        numTested += 1;
        AddCueCard("q4", "a4");

        listSize = GetAllCueCards().size();
        if (RemoveCard("q4", "a4") == 0 && GetAllCueCards().size() == listSize - 1){
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: RemoveCard either didn't remove a card or threw an error when it wasn't supposed to\n");}



        System.out.println("testing GetNextCard");
        System.out.println("testing GetNextCard when currentCard should be null");
        numTested += 1;

        ArrayList<String> card = GetNextCard();

        if (card.get(0).equals("1 q1") && card.get(1).equals("1 a1")){
            numPassed += 1;
        }
        else{System.out.println("failed: GetNextCard returned the wrong card:\n" + card + "\n");}



        System.out.println("testing GetNextCard base case");
        numTested += 1;
        card = GetNextCard();

        if (card.get(0).equals("1 q2") && card.get(1).equals("1 a2")) {
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: GetNextCard returned the wrong card:\n" + card + "\n");}



        System.out.println("testing GetPreviousCard base case");
        numTested += 1;
        card = GetPreviousCard();

        if (card.get(0).equals("1 q1") && card.get(1).equals("1 a1")) {
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: GetPreviousCard returned the wrong card:\n" + card + "\n");}



        System.out.println("testing GetPreviousCard wrap around");
        numTested += 1;
        card = GetPreviousCard();

        if (card.get(0).equals("1 q3") && card.get(1).equals("1 a3")) {
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: GetPreviousCard returned the wrong card:\n" + card + "\n");}



        System.out.println("testing GetNextCard wrap around");
        numTested += 1;
        card = GetNextCard();

        if (card.get(0).equals("1 q1") && card.get(1).equals("1 a1")) {
            numPassed += 1;
            System.out.println("passed\n");
        }
        else{System.out.println("failed: GetNextCard returned the wrong card:\n" + card + "\n");}






        SelectSubject("test subject 1");
        RandomizeCards();
        for (int index = 0; index < GetAllCueCards().size(); index++){
            System.out.println(GetNextCard());
        }



        System.out.println(numPassed + " / " + numTested + " test cases passed");

        System.out.println(GetAllSubjectNames());
        System.out.println("current subject: " + GetName());



        String searchingFor = "a1";
        System.out.println("\n\n searching for " + searchingFor);
        System.out.println(Searchable.Search(searchingFor));

        //cleaning up the results of testing
//        ArrayList<String> remainingFiles = GetAllSubjectNames();
//        remainingFiles.forEach(sub ->{
//            SelectSubject(sub);
//            DeleteSubjectFolder();
//        });
    }

}

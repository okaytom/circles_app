package com.example.demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NoteTaker extends SaveState implements Searchable{

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
    private static ArrayList<Subject> subjectList = new ArrayList<>();

    public final static String subjectListFilePath = SaveState.devFolder + "/Subjects.json";//file path to json storing subject objects


    private static Subject currentSubject;//the subject the user is currently interacting with

    /***
     * gets the names of all the Subjects
     * @return list of the names of all the Subjects
     */
    public static ArrayList<String> GetAllSubjectNames(){
        //making sure previous subjects are loaded
        LoadSubjects();

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
        LoadSubjects();


        //checking for invalid input
        if (name.isBlank()){name = Subject.defaultName;}

        if(name.contains(SaveState.devFolder)){
            System.out.println("User is not allowed to add files to the dev folder");

            try{AlertBox.display("Error in creating a subject","user is not allowed to edit the dev folder");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return -2;
        }

        //checking if the subject already exists
        int index = 0;
        while (index < subjectList.size()){
            if (subjectList.get(index).GetName().equals(name)){
                try{AlertBox.display("Error in adding subject","there already exists a subject with that name");}
                catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
                catch(NoClassDefFoundError error){}

                return -2;
            }
            index += 1;
        }



        Subject newSubject = new Subject(name);

        if (newSubject.GetName().isBlank()){//failed to create a new Subject
            try{AlertBox.display("Error in adding subject","failed to create a new subject");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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
        LoadSubjects();


        if (subjectList.isEmpty()){
            System.out.println("cannot change the current subject when no subjects have been created");

            try{AlertBox.display("Error in selecting a subject","Cannot change a subject when none have been created");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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
        try{AlertBox.display("Error in selecting a subject","Couldn't find the specified subject");}
        catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
        catch(NoClassDefFoundError error){}

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

            try{AlertBox.display("Error in changing subject's name","Need to select a Subject before you can change its name");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}
            return -2;
        }

        //checking if there is an existing Subject with that name
        for (int index = 0; index < subjectList.size(); index++){
            if (newName.equals(subjectList.get(index).GetName())){

                try{AlertBox.display("Error changing the Subject's name","Cannot change a subject's name to the name of another subject");}
                catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
                catch(NoClassDefFoundError error){}

                return -2;
            }
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

            try{AlertBox.display("Error in getting a subject's name","Need to select a subject before you can get its name");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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

            try{AlertBox.display("Error in getting the cue cards","Need to select a subject before you can get its cue cards");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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

            try {AlertBox.display("Error adding CueCard", "need to select a subject before you can create a cue card");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return -2;
        }

        int result = currentSubject.AddCueCard(question, answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//issue when saving
                //alert box will be in SaveState
                System.out.println("failed to update " + subjectListFilePath + " when adding a cue card from " + currentSubject.GetName());
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

            try{AlertBox.display("Error in changing a cue card","Need to select a subject before you can change its cue cards");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return -2;
        }


        int result = currentSubject.ChangeCard(oldQuestion, oldAnswer, newQuestion, newAnswer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//failed to save
                //alert box will be in SaveState
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

            try{AlertBox.display("Error in removing a cue card","Need to select a subject before you can remove one of its cue cards");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return -2;
        }

        int result = currentSubject.RemoveCard(question, answer);
        if (result == 0){
            if(!Save(subjectListFilePath, subjectList)){//failed to save
                //alert box will be in SaveState
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

            try{AlertBox.display("Error in getting next cue card","Need to select a subject before you can get one of its cue cards");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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

            try{AlertBox.display("Error in CueCard", "need to select a subject before you can get a cue card");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

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
            System.out.println("need to select a subject before you can randomize the cue cards");

            try{AlertBox.display("Error in CueCard", "need to select a subject before you can randomize its cue cards");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}
        }
        else {currentSubject.RandomizeCards();}
    }



    /***
     * gets the notePath for currentSubject
     * @return the notePath
     */
    public static String GetNotesFilePath(){
        if (currentSubject == null){
            try{AlertBox.display("Error in getting notes file path","Need to select a subject before you can get its file path to notes");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return "";
        }
        return currentSubject.GetNotesFilePath();
    }



    /***
     * gets the pdfPath for currentSubject
     * @return the pdfPath
     */
    public static String GetPDFFilePath(){
        if (currentSubject == null){
            try{AlertBox.display("Error in getting pdfs file path","Need to select a subject before you can get a file path to its pdfs");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return "";
        }
        return currentSubject.GetPDFFilePath();
    }





    /***
     * gets the imagePath for currentSubject
     * @return the imagePath
     */
    public static String GetImageFilePath(){
        if (currentSubject == null){
            try{AlertBox.display("Error in getting images file path","Need to select a subject before you can get a file path to its images");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return "";
        }
        return currentSubject.GetImageFilePath();
    }




    /***
     * gets a list of all the pdf filenames in currentSubject's pdfs folder without their file extension
     * @return an empty list if no subject was selected or a list of pdf filenames without the file extension
     */
    public static ArrayList<String> GetAllPDFs(){
        if (currentSubject == null){
            try{AlertBox.display("Error in getting all pdfs","Need to select a subject before you can get its pdfs");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return new ArrayList<>();
        }
        else {return currentSubject.GetAllPDFs();}
    }

    /***
     * gets a list of all the pdf filenames in currentSubject's notes folder without their file extension
     * @return an empty list if no subject was selected or a list of pdf filenames without the file extension
     */
    public static ArrayList<String> GetAllNotes(){
        if (currentSubject == null){
            try{AlertBox.display("Error in getting notes","Need to select a subject before you can get its notes");}
            catch(ExceptionInInitializerError error){}//error happens when the front end isn't initialized (like when testing the backend)
            catch(NoClassDefFoundError error){}

            return new ArrayList<>();
        }
        else{return currentSubject.GetAllNotes();}
    }






    /***
     * loads the subject list, updates subjectList, and checks if any files were deleted externally and adjusts accordingly
     */
    public static void LoadSubjects(){
        ArrayList<Subject> missingSubjectList = new ArrayList<>();

        subjectList = SaveState.Load(subjectListFilePath, Subject.class);

        //getting list of subjects missing files
        for (int index = 0; index < subjectList.size(); index++){
            Subject tempSubject = subjectList.get(index);


            if (!(new File(tempSubject.GetSubjectFilePath()).exists())){//subject doesn't have a folder
                missingSubjectList.add(tempSubject);
            }
            else{//making sure it has a notes and pdfs folder
                File subjectDir = new File(tempSubject.GetSubjectFilePath() + "/notes");
                File pdfDir = new File(tempSubject.GetSubjectFilePath() + "/pdfs");
                File imageDir = new File(tempSubject.GetSubjectFilePath() + "/images");

                subjectDir.mkdir();
                pdfDir.mkdir();
                imageDir.mkdir();
            }
        }

        //update subjectList to exclude the missing subjects
        for (int index = 0; index < missingSubjectList.size(); index++){
            if (missingSubjectList.get(index) == currentSubject){currentSubject = null;}

            subjectList.remove(missingSubjectList.get(index));
        }
    }




    /***
     * searches the name of the Subjects, the name and content of notes, pdfs, and cue cards for the searchTerm
     * @param searchTerm the term being searched for
     * @return all the Subjects that have content that match searchTerm and whatever content that matched searchTerm
     */
    public static ArrayList Search(String searchTerm) {

        ArrayList results = new ArrayList();

        //making sure the subjects were loaded
        LoadSubjects();

        //searching the Subjects
        results.add("Subjects");

        int subjectIndex = 0;
        while (subjectIndex < subjectList.size()){
            Subject subject = subjectList.get(subjectIndex);
            ArrayList subjectContents = new ArrayList();

            if (subject.GetName().contains(searchTerm)){//checking the name of the Subject
                subjectContents.add(subject.GetName());
            }


            //checking the cue cards
            ArrayList<ArrayList<String>> cards = subject.GetAllCueCards();
            int cardIndex = 0;

            ArrayList cardResults = new ArrayList();
            cardResults.add("Cue Cards");

            while(cardIndex < cards.size()){
                ArrayList<String> card = cards.get(cardIndex);

                //checking the question and answer of a card
                if (card.get(0).contains(searchTerm) || card.get(1).contains(searchTerm)) {
                    //adding card
                    ArrayList cardContent = new ArrayList();
                    cardContent.add("Question: " + card.get(0));
                    cardContent.add("Answer: " + card.get(1));

                    cardResults.add(cardContent);
                }

                cardIndex += 1;
            }

            //adding cue cards to the subject's matching content
            if (cardResults.size() > 1){
                if (subjectContents.size() == 0){subjectContents.add(subject.GetName());}

                subjectContents.add(cardResults);
            }



            //checking the pdfs folder
            ArrayList<String> pdfsList = subject.GetAllPDFs();
            int pdfIndex = 0;
            ArrayList pdfResults = new ArrayList();
            pdfResults.add("pdfs");

            while (pdfIndex < pdfsList.size()){
                String pdfName = pdfsList.get(pdfIndex) + ".pdf";
                ArrayList pdfMatchingContent = new ArrayList();

                //checking file contents and file name
                ArrayList tempList = SearchPDFContent(subject.GetPDFFilePath() + "/" + pdfName, searchTerm);
                if ((tempList != null) || pdfName.contains(searchTerm)){
                    pdfMatchingContent.add(pdfName);
                    //TODO: uncomment our line if you want to display each line in a pdf that matches the searchTerm
//                    pdfMatchingContent.addAll(tempList);
                }


                if (pdfMatchingContent.size() > 0){pdfResults.add(pdfMatchingContent);}

                pdfIndex += 1;
            }

            //adding pdfs to the subject's matching content
            if (pdfResults.size() > 1){
                if (subjectContents.size() == 0){subjectContents.add(subject.GetName());}

                subjectContents.add(pdfResults);
            }


            //checking the notes folder
            ArrayList<String> notesList = subject.GetAllNotes();
            int notesIndex = 0;

            ArrayList notesResult = new ArrayList();
            notesResult.add("Notes");

            while (notesIndex < notesList.size()){
                String fileName = notesList.get(notesIndex) + ".docx";
                ArrayList matchingContent;

                //checking file contents and file name
                matchingContent = SearchDocxContent(subject.GetNotesFilePath() + "/" + fileName, searchTerm);

                if (matchingContent != null || fileName.contains(searchTerm)){
                    notesResult.add(fileName);
                    //TODO: uncomment our line if you want to display each line in a .docx that matches the searchTerm
//                    notesResult.add(matchingContent);
                }

                notesIndex += 1;
            }


            if (notesResult.size() > 1){
                if (subjectContents.size() == 0){subjectContents.add(subject.GetName());}

                subjectContents.add(notesResult);
            }



            if (subjectContents.size() > 0){results.add(subjectContents);}

            subjectIndex += 1;
        }

        if (results.size() > 1){return results;}


        return null;
    }



    /***
     * searches a pdf and gets each line that contains the searchTerm
     * @param filePath the file path to the pdf
     * @param searchTerm the term being searched for
     * @return each line containing the search term in the pdf
     */
    public static ArrayList SearchPDFContent(String filePath, String searchTerm){
        ArrayList results = new ArrayList();

        try {
            //From Tanner's code
            File file = new File(filePath);
            PDDocument doc = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(doc);


            if (!text.isBlank()) {
                //getting lines containing the search term
                Object[] sentences = text.lines().toArray();

                for (int index = 0; index < sentences.length; index++) {
                    if (sentences[index].toString().contains(searchTerm)){
                        results.add(sentences[index].toString());
                    }
                }
            }


            doc.close();
        }
        catch (IOException error){
            error.printStackTrace();
        }

        if (results.size() > 0){return results;}

        return null;
    }




    /***
     * searches a pdf and gets each line that contains the searchTerm
     * @param filePath the file path to the docx file
     * @param searchTerm the term being searched for
     * @return each line containing the search term in the docx
     */
    public static ArrayList SearchDocxContent(String filePath, String searchTerm){
        ArrayList results = new ArrayList();

        try{
            FileInputStream fileStream = new FileInputStream(filePath);
            XWPFDocument docx = new XWPFDocument();

            List<XWPFParagraph> paragraphList = docx.getParagraphs();

            for (int index = 0; index < paragraphList.size(); index++){
                String paragraph = paragraphList.get(index).getText();
                if (paragraph.contains(searchTerm)){results.add(paragraph);}
            }

            fileStream.close();
        }
        catch(Exception error){
            error.printStackTrace();
        }


        if (results.size() > 0){return results;}

        return null;
    }








    //testing
    public static void main(String[] args){

        boolean pass;
        int numPassed = 0;
        int numTested = 0;
        int listSize;

        LoadSubjects();


        //testing AddSubject and GetAllSubjectNames
        System.out.println("            testing AddSubject");

        System.out.println("testing base case for AddSubject");

        listSize = GetAllSubjectNames().size();
        AddSubject("test subject 0");
        AddSubject("test subject 1");
        AddSubject("test subject 10");
        AddSubject("test subject 2");
        numTested += 1;

        if (GetAllSubjectNames().size() != listSize + 4){
            System.out.println("there is an issue with either AddSubject or GetAllSubjectNames\nresults:");
            System.out.println(GetAllSubjectNames());
        }
        else{
            numPassed += 1;
            System.out.println("passed\n");
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
        SelectSubject(Subject.defaultName);
        DeleteSubjectFolder();


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
        SelectSubject("test subject 2");

        ChangeSubjectName("");
        if (GetName().equals(Subject.defaultName)){
            ChangeSubjectName("test subject 2");//changing its name from the default value so that name is available for the next Subject with a blank name

            SelectSubject("test subject 2");
            ChangeSubjectName("  ");
            if (GetName().equals(Subject.defaultName)){
                numPassed += 1;
                System.out.println("passed\n");
            }
            else{System.out.println("failed: did not change the name from a string with only spaces to default name (new folder)\n");}
        }
        else{System.out.println("failed: when passed an empty string, did not set the name to the default one (new folder)\n");}


        //base case
        System.out.println("testing the base case");
        numTested += 1;

        SelectSubject(Subject.defaultName);
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


        SelectSubject("test subject 2");
        System.out.println(numPassed + " / " + numTested + " test cases passed");


        System.out.println(GetAllSubjectNames());
        System.out.println("current subject: " + GetName());





        String searchingFor = "preservation";
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

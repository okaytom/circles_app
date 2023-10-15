package com.example.demo;

import java.io.File;

/***
 * represents a class at school
 */


public class Subject {

    private static String appName = "Circle_App";
    public String name;


    private String filePath;
    private String notesPath;//TODO

    private String cardPath;//TODO


    public Subject(String name){
        this.name = name;

        //creating folders
        File appDir = new File(appName);
        appDir.mkdir();

        if (appDir.exists()) {
            this.filePath = appName + "/" + name;

            File subjectDir = new File(this.filePath + "/notes");

            if (!subjectDir.mkdirs()){
                if (!subjectDir.exists()){System.out.println("Failed to create the directory for " + name);}
            }
        }
        else{System.out.println("Failed to create the app's directory for storing subjects");}

    }


    /***
     * changes the name of the subject and its file
     * @param newName the new name for the subject
     * @return 0 if succeeds, 1 if error, 2 if a file already exists with the new name
     */
    public int ChangeName(String newName){

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
     * recursively goes through the file, deleting its contents
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



    public void AddCueCard(){
        //TODO
    }

    public void RemoveCueCard(){
        //TODO
    }

    public void ChangeCueCard(){
        //TODO
    }



//testing
//    public static void main(String[] args){
//        Subject cmpt111 = new Subject("cmpt111");
//
//        cmpt111.ChangeName("econ111");
//
//
//    }
}

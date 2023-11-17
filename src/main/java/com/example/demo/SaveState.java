//Created by Tyler Chow
/*
creates a folder for the app and saves and loads files with json format
 */
package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;



/***
 * handles saving and loading lists of objects in .json files
 */
public class SaveState {

    public final static String appName = "Circle App"; //directory for the app's files

    public final static String devFolder = appName + "/dev file";//directory for developers to store files the user doesn't need to interact with

    public static String appFolderAbsolutePath = System.getProperty("user.dir");

    // add transient to attributes if you don't want them to be saved
    //ex:
    /***
     * public transient int duration;
     */

    /***
     * saves a list of objects of the same type in a .json file. Add "transient" to an attribute's declaration
     * if you don't want that attribute to be saved.
     * @param filePath the name of the .json file and where to find it if not in the working directory
     * @param objList the list of objects to be stored
     * @param <type> the type of the objects to be stored
     * @postcond creates/changes the content of filepath
     * @return true if it succeeded, false if it didn't
     */
     static <type> Boolean Save(String filePath, type objList) {
         Gson gson = new GsonBuilder()
                 .setPrettyPrinting()
                 .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
                 .create();



         try {//writing content to file
            FileWriter file = new FileWriter(filePath);
            gson.toJson(objList, file);

            file.flush();
            file.close();

        }
        catch (Exception e) {
            System.out.println("failed to save: " + filePath);
//            e.printStackTrace();
            return false;
        }

        return true;
    }


    /***
     * Loads objects saved in a list in a .json file containing only one type of object
     * @param filePath the name of the .json file, defaults to working directory if not specified
     * @param className the class of the object stored in the file
     * @param <type> the type of the object stored
     * @precond the .json file filepath must exist or it returns an empty list
     * @return a list of objects or an empty list
     */
     static <type> ArrayList<type> Load(String filePath, Class<type> className) {

        ArrayList<type> objList = new ArrayList<>(); //list of desired objects

        ArrayList<type> tempList; //list to store loaded objects of the wrong type



         Gson gson = new GsonBuilder()
                 .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
                 .create();


         try {//loading content of filepath
            FileReader file = new FileReader(filePath);
            tempList = gson.fromJson(file, ArrayList.class);

            //converting the items of the array list to the right type
            if (tempList != null) {
                tempList.forEach(x -> {
                    objList.add(gson.fromJson(gson.toJson(x), className));
                });
            }

            file.close();

        }
        catch (Exception e) {
            System.out.println("failed to load " + className + " or file wasn't created yet");
//            e.printStackTrace();
        }

        return objList;
    }



    /***
     * Loads an object saved in a .json file with one object
     * @param filePath the name of the .json file, defaults to working directory if not specified
     * @param className the class of the object stored in the file
     * @param <type> the type of the object stored
     * @precond the .json file filepath must exist or it returns an empty list
     * @return the object stored in the json file or null on error
     */
    static <type> type LoadObject(String filePath, Class<type> className) {

        type result = null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
                .create();


        try {//loading content of filepath
            FileReader file = new FileReader(filePath);
            result = gson.fromJson(file, className);

            file.close();

        }
        catch (Exception e) {
            System.out.println("failed to load " + className + " or file wasn't created yet");
//            e.printStackTrace();
        }

        return result;
    }


    /***
     * checks if a file exists in a folder
     * @param fileName the name of the file being looked for
     * @param folder the folder being checked for the file
     * @return true if the file exists in the folder, false if it doesn't
     */
    public static boolean FileExists(String fileName, String folder){
        File folderFile = new File(folder);

        String[] files = folderFile.list();

        //searching through the content of notes for fileName
        for (int index = 0; index < files.length; index++){
            if (files[index].equals(fileName)){return true;}
        }

        return false;
    }

}




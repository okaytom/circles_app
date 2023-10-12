package com.example.demo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;



/***
 * handles saving and loading lists of objects
 */
public class SaveState {

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
    public static <type> Boolean Save(String filePath, ArrayList<type> objList) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        try {//writing content to file
            FileWriter file = new FileWriter(filePath);
            gson.toJson(objList, file);

            file.flush();
            file.close();

        }//TODO exception handling
        catch (IOException e) {
            System.out.println("failed to save: " + filePath);
            return false;
        }

        return true;
    }


    /***
     * Loads objects saved in a .json file containing only one type of object
     * @param filePath the name of the .json file, defaults to working directory if not specified
     * @param className the class of the object stored in the file
     * @param <type> the type of the object stored
     * @precond the .json file filepath must exist or it returns an empty list
     * @return a list of objects or an empty list
     */
    public static <type> ArrayList<type> Load(String filePath, Class<type> className) {

        ArrayList<type> objList = new ArrayList<>(); //list of desired objects

        ArrayList<type> tempList; //list to store loaded objects of the wrong type



        Gson gson = new Gson();

        try {//loading content of filepath
            FileReader file = new FileReader(filePath);
            tempList = gson.fromJson(file, ArrayList.class);

            //converting the items of the array list to the right type
            tempList.forEach(x -> {
                objList.add(gson.fromJson(x.toString(), className));
            });

            file.close();

        }//TODO exception handling
        catch (IOException e) {
            System.out.println("failed to load " + className + " or file wasn't created yet");
        }

        return objList;
    }


}




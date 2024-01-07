package com.example.demo;

import java.util.ArrayList;

public interface Searchable {

    //created by Tyler Chow

    public static final String searchErrorMsg = "nothing found";


    /***
     * searches if something matches the searchTerm
     * @param searchTerm the term being searched for
     * @return a list containing the information about objects containing the search term
     */
    static ArrayList Search(String searchTerm) {
        ArrayList results = new ArrayList();
        ArrayList searchResults;


        //searching events and reminders for searchTerm
        searchResults = CalendarController.Search(searchTerm);
        if (searchResults != null){results.add(searchResults);}

        //searching Subjects for searchTerm
        searchResults = NoteTaker.Search(searchTerm);
        if (searchResults != null){results.add(searchResults);}


        if (results.size() == 0){results.add(searchErrorMsg);}

        return results;
    }
}

//created by Tyler Chow
package com.example.demo;

public interface Searchable {


    public static final String searchErrorMsg = "nothing found";

    /***
     * searches if something matches the searchTerm
     * @param searchTerm the term being searched for
     * @return a string containing the information about objects containing the search term
     */
    static String Search(String searchTerm) {
        String results = "";
        boolean foundSomething = false;

        String searchResults;




        //searching events for searchTerm
        searchResults = CalendarController.Search(searchTerm);
        if (!searchResults.equals(searchErrorMsg)){//found something

            if (foundSomething){//for readability between categories
                results = results + "\n\n\n\n";
            }

            results = results + searchResults;

            foundSomething = true;
        }




        //searching Subjects for searchTerm
        searchResults = NoteTaker.Search(searchTerm);
        if (!searchResults.equals(searchErrorMsg)){//found something

            if (foundSomething){//for readability between categories
                results = results + "\n\n\n\n";
            }

            results = results + searchResults;

            foundSomething = true;
        }


        if (foundSomething){return results;}

        return searchErrorMsg;
    }
}

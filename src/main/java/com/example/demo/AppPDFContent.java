//Created by Tyler Chow

package com.example.demo;


/***
 * a portion of the contents of a pdf for a note
 */
public interface AppPDFContent {

    /***
     * adds the formatting of the content
     */
    public abstract void SetStyle();

    /***
     * adds the content to be loaded or saved
     */
    public abstract void AddContent();
}

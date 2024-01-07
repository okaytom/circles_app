package com.example.demo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface NoteContent {


    /***
     * adds the content this class represents to a Word document
     * @param docx the word document being edited
     */
    void SaveContent(XWPFDocument docx);


    //TODO:add methods for displaying the content
}

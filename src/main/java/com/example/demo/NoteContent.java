package com.example.demo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public interface NoteContent {



    /***
     * adds the content this class represents to a Word document
     * @param paragraph the paragraph the content will be added to
     */
    void SaveContent(XWPFParagraph paragraph);


    //TODO:add methods for displaying the content

}

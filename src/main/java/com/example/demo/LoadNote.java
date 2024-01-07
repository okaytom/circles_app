package com.example.demo;
//Created by Tyler Chow

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/***
 * loads the contents of a docx file into a list and can create a docx file using a similar list
 */
public class LoadNote {

    /***
     * turns most of the content of a Word document into a format our app can read (there are some things it cannot read
     * like tables, if it cannot read them, then they might be deleted if you use our app to save)
     * @param filePath the filepath to the Word document to be loaded including the .docx file extension
     * @return the content of a Word document that our app can read
     */
    public static ArrayList<NoteContent> LoadDocx(String filePath){
        ArrayList<NoteContent> results = new ArrayList<>();

        try {
            FileInputStream fileStream = new FileInputStream(filePath);

            XWPFDocument docx = new XWPFDocument(fileStream);
            List<XWPFParagraph> paragraphList = docx.getParagraphs();


            //turning the content of the docx file into content the frontend can load
            for (int paragraphIndex = 0; paragraphIndex < paragraphList.size(); paragraphIndex++){
                XWPFParagraph paragraph = paragraphList.get(paragraphIndex);

                if (paragraph.runsIsEmpty()){//is a blank line
                    //TODO: handle blank lines
                }
                else{//not a blank line
                    List<XWPFRun> runList = paragraph.getRuns();

                    for (int runIndex = 0; runIndex < runList.size(); runIndex++){
                        XWPFRun run = runList.get(runIndex);//content with the same format

                        if (run.getEmbeddedPictures().size() > 0){//run is a list of pictures
                            List<XWPFPicture> pictureList = run.getEmbeddedPictures();

                            for (int pictureIndex = 0; pictureIndex < pictureList.size(); pictureIndex++){
                                //TODO: create picture objects
                            }
                        }
                        else{
                            //TODO: handle text
                        }

                    }

                }

                //TODO: handle new lines (assuming getting the text strips the new line characters)


            }


            docx.close();
            fileStream.close();
        }
        catch (Exception error){error.printStackTrace();}

        return results;
    }


    /***
     * updates/creates a Word document with the content provided
     * @param filePath the filepath to the Word document including the .docx file extension
     * @param content the content in the Word document
     */
    public static void SaveDocx(String filePath, ArrayList<NoteContent> content){
        try{
            FileOutputStream fileStream = new FileOutputStream(new File(filePath));
            XWPFDocument docx = new XWPFDocument();

            //adds each item provided to the Word document
            for (int index = 0; index < content.size(); index++){
                content.get(index).SaveContent(docx);
            }



            docx.write(fileStream);//save the content
            docx.close();
            fileStream.close();
        }
        catch (FileNotFoundException error){
            //TODO: display error message to the user
            //TODO: one cause of this exception is the docx is currently open in Microsoft Word

        }
        catch (Exception error){
            error.printStackTrace();
            //TODO: display error message to the user
        }

    }


}

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
    public static ArrayList<ArrayList<NoteContent>> LoadDocx(String filePath){
        ArrayList<ArrayList<NoteContent>> results = new ArrayList<>();

        try {
            FileInputStream fileStream = new FileInputStream(filePath);

            XWPFDocument docx = new XWPFDocument(fileStream);
            List<XWPFParagraph> paragraphList = docx.getParagraphs();


            //turning the content of the docx file into content the frontend can load
            for (int paragraphIndex = 0; paragraphIndex < paragraphList.size(); paragraphIndex++){
                XWPFParagraph paragraph = paragraphList.get(paragraphIndex);

                ArrayList<NoteContent> paragraphContent = new ArrayList<>();

                if (paragraph.runsIsEmpty()){//is a blank line
                    //TODO: handle blank lines
                }
                else{//not a blank line
                    List<XWPFRun> runList = paragraph.getRuns();

                    for (int runIndex = 0; runIndex < runList.size(); runIndex++){
                        XWPFRun run = runList.get(runIndex);//content with the same format

                        if (run.getEmbeddedPictures().size() > 0){//run is a list of pictures
                            List<XWPFPicture> pictureList = run.getEmbeddedPictures();

                            //adding pictures
                            for (int pictureIndex = 0; pictureIndex < pictureList.size(); pictureIndex++){
                                paragraphContent.add(new NoteImageContent(pictureList.get(pictureIndex)));
                            }
                        }
                        else{//adding text
                            paragraphContent.add(new NoteTextContent(run));
                        }

                    }

                }

                results.add(paragraphContent);
            }


//            docx.close();
            fileStream.close();
        }
        catch (Exception error){error.printStackTrace();}

        return results;
    }


    /***
     * updates/creates a Word document with the content provided
     * @param filePath the filepath to the Word document including the .docx file extension
     * @param paragraphList the content in the Word document
     */
    public static void SaveDocx(String filePath, ArrayList<ArrayList<NoteContent>> paragraphList){
        try{
            FileOutputStream fileStream = new FileOutputStream(new File(filePath));
            XWPFDocument docx = new XWPFDocument();

            //splitting content by paragraphs
            for (int paragraphIndex = 0; paragraphIndex < paragraphList.size(); paragraphIndex++){
                XWPFParagraph paragraph = docx.createParagraph();

                //adds each item provided to the Word document
                for (int runIndex = 0; runIndex < paragraphList.get(paragraphIndex).size(); runIndex++) {
                    try {
                        paragraphList.get(paragraphIndex).get(runIndex).SaveContent(paragraph);
                    }
                    catch (Exception error){error.printStackTrace();}/*just to be sure the entire docx file doesn't fail
                    to save if there was an error saving one element*/
                }
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

    //testing
    public static void main(String[] args) {

        //TODO: remove, these tests require all the temporary files to still be in the project
        //testing LoadDocx()
        ArrayList<ArrayList<NoteContent>> output = LoadDocx("test.docx");


//        output.get(output.size() - 1).add(new NoteImageContent("turtwig statue.jpg", "turtwig statue"));
//        output.get(output.size() - 1).add(new NoteImageContent("Doge.jpg", "Doge"));
        output.get(output.size() - 1).add(new NoteImageContent("testpng.png", "testpng"));


        for (int indexOne = 0; indexOne < output.size(); indexOne++){

            for (int runIndex = 0; runIndex < output.get(indexOne).size(); runIndex++){

                if (output.get(indexOne).get(runIndex) instanceof NoteImageContent){
//                    System.out.println("image: " + (((NoteImageContent) output.get(indexOne).get(runIndex)).imageFileName));
                }
                else if (output.get(indexOne).get(runIndex) instanceof NoteTextContent){
//                    System.out.println("text: " + ((NoteTextContent) output.get(indexOne).get(runIndex)).text);
                }
            }

//            System.out.println();// to represent newlines for paragraphs with no runs (blank lines in word doc)
        }


        //testing SaveDocx - will have to manually check the docx file
        SaveDocx("test.docx", output);

    }
}

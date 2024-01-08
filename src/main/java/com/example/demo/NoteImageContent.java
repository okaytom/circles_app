package com.example.demo;

import org.apache.poi.common.usermodel.PictureType;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class NoteImageContent implements NoteContent{

    public String filePath;
    public String imageFileName;

    XWPFPicture picture;

    /***
     * saving information for adding a picture read from a docx file
     * @param picture
     */
    public NoteImageContent(XWPFPicture picture){
        this.picture = picture;
        this.imageFileName = picture.getPictureData().getFileName();
    }

    /***
     * saving information for adding a picture received from a user
     * @param filePath
     * @param imageFileName
     */
    public NoteImageContent(String filePath, String imageFileName){
        this.filePath = filePath;
        this.imageFileName = imageFileName;
    }


    @Override
    public void SaveContent(XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();

        //if image was from a loaded docx
        if (this.picture != null){
            XWPFPictureData pictureData = this.picture.getPictureData();
            try {
                run.addPicture(new ByteArrayInputStream(pictureData.getData()), pictureData.getPictureType(), pictureData.getFileName(), (int) this.picture.getWidth(), (int) this.picture.getDepth());
            } catch (Exception error){
                error.printStackTrace();
                //TODO: tell user an error occurred
            }
        }
        else{//image was from the user
            try{
                //getting the values to plug into run.addPicture
                InputStream fileStream = new FileInputStream(this.filePath);

                //getting file type
//                String[] tempSplitFilePath = this.filePath.split(".");
//                String fileExtension = tempSplitFilePath[tempSplitFilePath.length - 1];
//                int pictureType;
//                PictureType thing = JPEG;
//                if (fileExtension.equals("png")){pictureType = JPEG;}
//                else if (fileExtension.equals("jpg")){}
//


                fileStream.close();
            }catch (Exception error){
                error.printStackTrace();
                //TODO: tell user an error occurred
            }
        }




    }
}

package com.example.demo;

import org.apache.poi.common.usermodel.PictureType;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.util.Units;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

public class NoteImageContent implements NoteContent{

    public String filePath;
    public String imageFileName;
    private static int maxPixelWidth = 695;//got from putting an image into Word and filling the page with it
    private static int maxPixelHeight = 937;


    XWPFPicture picture;

    /***
     * saving information for adding a picture read from a docx file
     * @param picture the picture loaded from the docx file
     */
    public NoteImageContent(XWPFPicture picture){
        this.picture = picture;
        this.imageFileName = picture.getPictureData().getFileName();
    }

    /***
     * saving information for adding a picture received from a user
     * @param filePath the filepath of the image, including the image filename and file extension
     * @param imageFileName name of the image excluding the file extension
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
                InputStream fileStream = new ByteArrayInputStream(pictureData.getData());

                run.addPicture(fileStream, pictureData.getPictureType(), pictureData.getFileName(), (int) this.picture.getWidth() * Units.EMU_PER_POINT, (int) this.picture.getDepth() * Units.EMU_PER_POINT);
                fileStream.close();
            } catch (Exception error){
                error.printStackTrace();
                //TODO: tell user an error occurred
            }
        }

        else{//image was from the user
            try{
                //getting the values to plug into run.addPicture
                InputStream fileStream = new FileInputStream(new File(this.filePath));

                //getting picture type
                PictureType pictureType = null;
                if (this.filePath.endsWith("png")){pictureType = PictureType.valueOf("PNG");}
                else if (this.filePath.endsWith("jpg")){pictureType = PictureType.valueOf("JPEG");}


                //getting image width and height
                BufferedImage image = ImageIO.read(new File(this.filePath));


                double width = image.getWidth();//in pixels
                double height = image.getHeight();//in pixels


                //scaling the image
                if (height > maxPixelHeight){
                    double ratio = width / height;
                    height = maxPixelHeight;
                    width = height * ratio;
                }

                if (width > maxPixelWidth){
                    double ratio = height / width;
                    width = maxPixelWidth;
                    height = width * ratio;
                }

                //adding image
                if(pictureType != null) {
                    run.addPicture(fileStream, pictureType, this.imageFileName, (int)(Units.EMU_PER_PIXEL * width) , (int)(Units.EMU_PER_PIXEL * height));
                }
                else{
                    //TODO: tell the user that the image file needs to be a png or jpg (can also use this.fileName to tell them which file caused the error)
                }

                fileStream.close();
            }catch (Exception error){
                error.printStackTrace();
                //TODO: tell user an error occurred
            }
        }




    }
}

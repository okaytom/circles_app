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
                //TODO: remove
                System.out.println("width:" + this.picture.getWidth() + " or in EMU:" + this.picture.getWidth() * Units.EMU_PER_POINT);
                System.out.println("depth:" + this.picture.getDepth() + " or in EMU:" + this.picture.getDepth() * Units.EMU_PER_POINT);

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
                ImageInputStream filePictureStream = ImageIO.createImageInputStream(new File(this.filePath));



                //getting picture type
                PictureType pictureType = null;
                if (this.filePath.endsWith("png")){pictureType = PictureType.valueOf("PNG");}
                else if (this.filePath.endsWith("jpg")){pictureType = PictureType.valueOf("JPEG");}


                //getting image width and height
//                BufferedImage image = ImageIO.read(new File(this.filePath));

                //TODO: replace because this didn't work either
                Iterator<ImageReader> readers = ImageIO.getImageReaders(filePictureStream);

                int width = 0;
                int height = 0;

                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(filePictureStream, true);
                    width = reader.getWidth(0);
                    height = reader.getHeight(0);
                }
                //TODO: remove
                System.out.println("\nUser");
                System.out.println("width:" + width);
                System.out.println("depth:" + height);

//                int width = (int)(image.getWidth() / 8.615384615384615);
//                int height = (int)(image.getHeight() / 8.615384615384615);

                //TODO: fix bug where the scale of an image drastically changes if it was an image provided by a user
                if(pictureType != null) {
                    run.addPicture(fileStream, pictureType, this.imageFileName, Units.toEMU(width) , Units.toEMU(height));
                }
                else{
                    //TODO: tell the user that the image file needs to be a png or jpg (can also use this.fileName to tell them which file caused the error)
                }

                filePictureStream.close();
                fileStream.close();
            }catch (Exception error){
                error.printStackTrace();
                //TODO: tell user an error occurred
            }
        }




    }
}

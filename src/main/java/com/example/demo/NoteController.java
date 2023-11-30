package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//Tanner

public class NoteController implements Initializable {
    // TODO let the user choose a font size, maybe in menu?

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu fonntMenu;

    @FXML
    private MenuItem loadBtn, openImage;

    @FXML
    private MenuBar menuBr;

    @FXML
    private MenuItem saveBtn;

    @FXML
    private TextArea textFld;

    @FXML
    private Spinner<Integer> fontSpinner;

    @FXML
    private ColorPicker colorPicker;


    // sets up the spinner for changing font. min value is 12, max is 40, and the increment is 1
    SpinnerValueFactory<Integer> spinValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(12,40, 1);

    // Variable for the PDBox font, defaults to Helvetica
    private PDType1Font pdfFont = PDType1Font.HELVETICA;

    // just a default font size
    private float fontSize = 12;

    private String title;

    private FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG and PNGs", "*.png", "*.PNG", "*.jpg", "*.JPG");

    //The tabsController
    private TabsController tabsController;

    /**
     * This method takes whatever is in the textArea, then saves it to a pdf
     * If the PDF already exists, it will just overwrite what is there
     * @param filePath the path the pdf will be saved to
     * @throws IOException
     */
    private void save(String filePath, String filename) throws IOException {
        PDDocument doc = new PDDocument();
        String txt = textFld.getText();
        txt = txt.replace("\n", "").replace("\r", "");
        // This line currently gets rid of newLine and invalid characters, as PDFBox does not like them and I haven't found a better fix yet
        //txt = txt.replace("\n", "").replace("\r", "");
        System.out.println(txt);
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        try{
            contentStream.beginText();
            // These are currently just filler values for font and layout, I'll do something prettier later
            contentStream.setFont(pdfFont, (float) textFld.getFont().getSize());
            contentStream.newLineAtOffset(5,page.getMediaBox().getHeight() - 25);
            contentStream.showText(txt);
            contentStream.endText();
            contentStream.close();
            doc.setAllSecurityToBeRemoved(true);

            // adding saved note to tabs controller NotesListView
            ListView<String> notes = tabsController.getNotesListView();
            doc.save(filePath + "\\" + filename + ".pdf");
            if(!notes.getItems().contains(filename)){
                tabsController.getNotesListView().getItems().add(filename);
            }
        }
        catch (Exception e){
            AlertBox.display("Could not save", "File contains characters that cannot be saved");
        }
        doc.close();
    }

    /**
     * This is an updated save which allows for pdf output to multiple lines
     * @param filePath
     * @param filename
     * @throws IOException
     */
    private void multiLineSave(String filePath, String filename) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        float pdfFontSize = (float) textFld.getFont().getSize();
        float leading = 1.5f * fontSize;

        PDRectangle mBox = page.getMediaBox();
        float margin = 72;
        float width = mBox.getWidth() - 2*margin;
        float startX = mBox.getLowerLeftX() + margin;
        float startY = mBox.getUpperRightY() - margin;

        String txt = textFld.getText();
        List<String> lines = new ArrayList<String>();
        txt = txt.replace("\n", " ").replace("\r", " ");
        int lastSpace = -1;

        String searchString = txt;

        // This loop handles the wrapping for the text on the pdf
        // width is the maximum width a line of text can be before it needs to write to a new line below
        // if width is exceeded at any point, it goes back to the last space, then makes a substring of the valid characters.
        // Lines is an arrayList of strings where each string is a separate line.
        while(!txt.isEmpty()){
            int spaceIndex = txt.indexOf(' ', lastSpace + 1);
            if(spaceIndex < 0){
                spaceIndex = txt.length();
            }
            String subString = txt.substring(0, spaceIndex);
            float size = pdfFontSize * pdfFont.getStringWidth(subString) / 1000;
            System.out.printf("'%s' - %f of %f\n", subString, size, width);
            if(size > width){
                if(lastSpace < 0){
                    lastSpace = spaceIndex;
                }
                subString = txt.substring(0, lastSpace);
                lines.add(subString);
                txt = txt.substring(lastSpace).trim();
                System.out.printf("'%s' is line\n", subString);
                lastSpace = -1;
            }
            else if(spaceIndex == txt.length()){
                lines.add(txt);
                txt = "";
            }
            else{
                lastSpace = spaceIndex;
            }
        }
        try{
            contentStream.beginText();
            contentStream.setFont(pdfFont, (float) textFld.getFont().getSize());
            contentStream.setNonStrokingColor((float) colorPicker.getValue().getRed(), (float) colorPicker.getValue().getGreen(), (float) colorPicker.getValue().getBlue());
            contentStream.newLineAtOffset(startX,startY);
            for(String line: lines){
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
            }
            contentStream.endText();


            /** Kayden Start**/

            // For pic names in note user uses **name of pic** and the image has to be in image folder of the current subject

            String tempPicName;

            while (searchString.contains("**")) {

                tempPicName = searchString.substring(searchString.indexOf("**") + 2);
                searchString = tempPicName.substring(tempPicName.indexOf("**") +2);
                tempPicName = tempPicName.substring(0, tempPicName.indexOf("**"));

                try {
                    PDImageXObject pdImage = PDImageXObject.createFromFile(NoteTaker.GetImageFilePath() + "\\" 
                            + tempPicName + ".png", doc);

                    float picWidth = pdImage.getWidth();
                    float picHeight = pdImage.getHeight();

                    float scaleFactor = 0;

                    if (picWidth > width) {
                        scaleFactor = width/picWidth;
                        picWidth = width;
                        picHeight *= scaleFactor;
                    }
                    contentStream.drawImage(pdImage, startX, 0, picWidth, picHeight);
                }
                catch (Exception e) {
                    String message = "Image folder does not contain the image: " + tempPicName;
                    AlertBox.display("Image not saved", message);
                }
            }

            /**Kayden end**/

            contentStream.close();
            doc.setAllSecurityToBeRemoved(true);

            ListView<String> notes = tabsController.getNotesListView();
            doc.save(filePath + "\\" + filename + ".pdf");
            if(!notes.getItems().contains(filename)){
                tabsController.getNotesListView().getItems().add(filename);
            }
        }
        catch (Exception e){
            AlertBox.display("Could not save", "Either the PDF is already open, or the file contains characters that cannot be saved");
        }

    }

    /**
     * This method takes whatever is in a pdf, strips the text from it then adds it to the textArea
     * @param filePath the filePath of the pdf we want to load from
     * @throws IOException
     */
    public void load(String filePath, String filename) throws IOException{
        File file = new File(filePath + "\\" + filename + ".pdf");
        if(file.exists()){
            PDDocument doc = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(doc);
            //text = text.replace("\n", " ").replace("\r", " ");
            System.out.println(text);
            doc.close();
            textFld.setText(text);
            title = filename;
        }
        else{
            AlertBox.display("Could not load file", "Selected file no longer exists");
        }

        // TODO get this working with newLines
    }

    /**
     * sets the fontSize for the PDF
     * @param newPDFFontSize new size of font
     */
    private void setPDFFontSize(float newPDFFontSize){
        fontSize = newPDFFontSize;
    }

    /**
     * sets the font for the pdf
     * @param newFont the font we will change to
     */
    private void setPdfFont(PDType1Font newFont){
        pdfFont = newFont;
    }

    /**
     * General method to change the font in the textArea the user is typing in
     * @param newTextFont the font that we will change to
     */
    private void setTextFont(Font newTextFont){
        // Both this and the setTextFontSize use the same logic of saving what is in the textArea, clearing it, then re-adding it with the updated settings
        String tempText = textFld.getText();
        textFld.clear();
        textFld.setFont(newTextFont);
        textFld.setText(tempText);
    }

    /**
     * General method to change the font size in the textArea the user is typing in
     * @param newTextFontSize
     */
    private void setTextFontSize(float newTextFontSize){
        String tempText = textFld.getText();
        textFld.clear();
        textFld.setFont(Font.font (textFld.getFont().getName(), newTextFontSize));
        textFld.setText(tempText);
        setPDFFontSize(newTextFontSize);
    }


    @FXML
    private void fileSaveHit(ActionEvent event) throws IOException {
        if(title == null){
            title = TextBox.display("Pick a title", "Pick a title for your notes");
            ListView<String> notes = tabsController.getNotesListView();
            if(notes.getItems().contains(title)){
                if(!ConfirmBox.display("Overwriting previous noted titled" + title, "Are you sure you want to overwrite the other file with this name?")){
                    title = TextBox.display("Pick a title", "Pick a title for your notes");
                }
            }
        }
        multiLineSave(NoteTaker.GetNotesFilePath(), title);
    }


    @FXML
    void onFileLoad(ActionEvent event) throws IOException {
        if(title == null){
            AlertBox.display("Error in Loading", "Must Save before a load");
        }
        else{
            load(NoteTaker.GetNotesFilePath(), title);
        }
    }

    @FXML
    void onInsertImage(ActionEvent event) throws  IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Import Image");
        Stage stage = new Stage();
        File myFile = fileChooser.showOpenDialog(stage);
        if (myFile != null){
            //checking if the file already exists
            String tempString = NoteTaker.GetImageFilePath();
            String image_filepath =  ".\\" + tempString.replace("/", "\\") + "\\";
            if (SaveState.FileExists(myFile.getName(), NoteTaker.GetPDFFilePath())){
                if(!ConfirmBox.display("Warning","A file with the name " + myFile.getName() + " already exists,\n would you like to overwrite it?")){
                    System.out.println("No image added");
                    return;//user doesn't want to overwrite their existing file
                }
            }

            // copies file from chosen path to our myFiles package, REPLACE_EXISTING overwrites file if same name
            Files.copy(myFile.toPath(), Path.of(image_filepath+myFile.getName()), REPLACE_EXISTING );
            File source = new File(image_filepath+myFile.getName());

            boolean success = source.exists();
            // prints message that file was imported and saved
            System.out.println("Operation success " + success);
            if(success){
                String temp = textFld.getText();
                // removing extension
                String filename = myFile.getName().replace(".png", "");
                filename = filename.replace(".PNG", "");
                filename = filename.replace(".jpg", "");
                filename = filename.replace(".JPG", "");

                temp = temp + "**" + filename + "**";
                textFld.setText(temp);
            }
        }
        else{
            System.out.println("No file imported");
        }
    }

    @FXML
    void setTextFontCourier(ActionEvent event) {
        setTextFont(Font.font ("Courier New", textFld.getFont().getSize()));
        setPdfFont(PDType1Font.COURIER);
    }

    @FXML
    void setTextFontDefault(ActionEvent event) {
        setTextFont(Font.font ("System", textFld.getFont().getSize()));
        setPdfFont(PDType1Font.HELVETICA);
    }

    @FXML
    void setTextFontTNR(ActionEvent event) {
        setTextFont(Font.font ("Times New Roman", textFld.getFont().getSize()));
        setPdfFont(PDType1Font.TIMES_ROMAN);
    }


    @FXML
    void refreshFont(ActionEvent event) {
        setTextFontSize(fontSpinner.getValue());
    }

    // Tommy
    public void getTabsController(TabsController tabsController) {
        this.tabsController = tabsController;
    }

    //Tommy
    public void NewSession(){
        title = null;
        textFld.clear();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSpinner.setValueFactory(spinValFac);
        fontSpinner.setEditable(false);
    }
}
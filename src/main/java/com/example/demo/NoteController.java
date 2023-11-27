package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//Tanner

public class NoteController implements Initializable {
    // TODO let the user choose a font size, maybe in menu?

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu fonntMenu;

    @FXML
    private MenuItem loadBtn;

    @FXML
    private MenuBar menuBr;

    @FXML
    private MenuItem saveBtn;

    @FXML
    private TextArea textFld;

    @FXML
    private Spinner<Integer> fontSpinner;

    // sets up the spinner for changing font. min value is 12, max is 40, and the increment is 1
    SpinnerValueFactory<Integer> spinValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(12,40, 1);

    // Variable for the PDBox font, defaults to Helvetica
    private PDType1Font pdfFont = PDType1Font.HELVETICA;

    // just a default font size
    private float fontSize = 12;

    private String title;

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
        // This line currently gets rid of newLine and invalid characters, as PDFBox does not like them and I haven't found a better fix yet
        txt = txt.replace("\n", "").replace("\r", "");
        System.out.println(txt);
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        try{
            contentStream.beginText();
            // These are currently just filler values for font and layout, I'll do something prettier later
            contentStream.setFont(pdfFont, (float) textFld.getFont().getSize());
            contentStream.newLine();
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
     * This method takes whatever is in a pdf, strips the text from it then adds it to the textArea
     * @param filePath the filePath of the pdf we want to load from
     * @throws IOException
     */
    public void load(String filePath) throws IOException{
        File file = new File(filePath);
        PDDocument doc = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(doc);
        System.out.println(text);
        doc.close();
        textFld.setText(text);

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
        }
        save(NoteTaker.GetNotesFilePath(), title);
    }


    @FXML
    void onFileLoad(ActionEvent event) throws IOException {
        load("filePath");
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

    public void getTabsController(TabsController tabsController) {
        this.tabsController = tabsController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSpinner.setValueFactory(spinValFac);
    }
}
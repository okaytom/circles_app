package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

//Tanner

public class NoteController {
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

//    @FXML
//    private Button tempFontChanger;

    // Font for the textArea, frontEnd Font
    private Font textFont;

    // Variable for the PDBox font, defaults to time new roman
    private PDType1Font pdfFont = PDType1Font.HELVETICA;

    // just a default font size
    private float fontSize = 12;

    // List of fonts we should have: Times Roman, Courier, default (helvetica)

    private void save(String filePath) throws IOException {
        PDDocument doc = new PDDocument();
        String txt = textFld.getText();
        // This line currently gets rid of newLine and invalid characters, as PDFBox does not like them and I haven't found a better fix yet
        txt = txt.replace("\n", "").replace("\r", "");
        System.out.println(txt);
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.beginText();
        // These are currently just filler values for font and layout, I'll do something prettier later
        contentStream.setFont(pdfFont, (float) textFld.getFont().getSize());
        contentStream.newLine();
        contentStream.showText(txt);
        contentStream.endText();
        contentStream.close();
        doc.save(filePath);
        doc.close();
    }

    private void load(String filePath) throws IOException{
        File file = new File(filePath);
        PDDocument doc = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(doc);
        System.out.println(text);
        doc.close();
        // Could prob just do it line by line maybe?
        textFld.setText(text);
    }
//    // NOT A PERMANENT METHOD, I'M, JUST DOING THIS SO I CAN EASILY CHANGE FONT SIZE WITHOUT DOING MENUBAR STUFF
//    @FXML
//    void changeFontTemp(MouseEvent event) {
//        setTextFont(Font.font ("Times New Roman", textFld.getFont().getSize()));
//    }




    private void setPDFFontSize(float newPDFFontSize){
        fontSize = newPDFFontSize;
    }

    private void setPdfFont(PDType1Font newFont){
        pdfFont = newFont;
    }

    private void setTextFont(Font newTextFont){
        // you probably want to save the text, clear the textarea, then reDraw with the old text
        String tempText = textFld.getText();
        textFld.clear();
        textFld.setFont(newTextFont);
        textFld.setText(tempText);
    }

    private void setTextFontSize(float newTextFontSize){
        String tempText = textFld.getText();
        textFld.clear();
        textFld.setFont(Font.font (textFld.getFont().getName(), newTextFontSize));
        textFld.setText(tempText);
    }


    @FXML
    private void fileSaveHit(ActionEvent event) throws IOException {
        save("filePath");
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

}
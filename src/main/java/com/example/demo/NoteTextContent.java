package com.example.demo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

public class NoteTextContent implements NoteContent{

    public String text;
    public String textColour;
    public double fontSize;
    public String fontFamily;


//    public STHighlightColor.Enum highlightColor;//will equal NONE if text isn't highlighted


    public NoteTextContent(XWPFRun run){
        this.text = run.text();
        this.textColour = run.getColor();
        this.fontSize = run.getFontSizeAsDouble();
        this.fontFamily = run.getFontFamily();
//        this.highlightColor = run.getTextHighlightColor();
    }

    public NoteTextContent(String text, String colour, double fontSize, String fontFamily){
        this.text = text;
        this.textColour = colour;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }


    @Override
    public void SaveContent(XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();

        run.setText(this.text);
        run.setColor(this.textColour);
        run.setFontSize(this.fontSize);
        run.setFontFamily(this.fontFamily);
    }
}

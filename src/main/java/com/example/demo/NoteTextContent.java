package com.example.demo;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

public class NoteTextContent implements NoteContent{

    public String text;
    public String textColour;
    public STHighlightColor.Enum highlightColor;//will equal NONE if text isn't highlighted


    public NoteTextContent(XWPFRun run){
        this.text = run.text();
        this.textColour = run.getColor();

        this.highlightColor = run.getTextHighlightColor();


    }

    @Override
    public void SaveContent(XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();

    }
}

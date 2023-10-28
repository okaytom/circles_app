package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class CalendarView {
    Canvas myCanvas;

    GraphicsContext gc;

    public CalendarView(){
        myCanvas = new Canvas(500, 500);
        gc = myCanvas.getGraphicsContext2D();
//        this.getChildren.add(myCanvas);
    }

    public void setupEvents(CalendarController controller){

    }

    public void draw(){

    }

    public void modelChanged(){

    }

}

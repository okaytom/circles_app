package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    /**
     * calendar stage
     */
    Stage calendar_stage;

    /**
     *  Object for holding the current date time
     */
    ZonedDateTime date = ZonedDateTime.now();


    /**
     * Scene for going back home
     */
    Scene home_scene;

    @FXML
    GridPane calendargrid;


    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<>();

    BorderPane display = new BorderPane();

    String[] months_lst = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};

    public void  drawCalendar(){

        ZonedDateTime calenderview = date;

        calendargrid.getStyleClass().add("background");

        Text blank = new Text("           ");
        calendargrid.add(blank, 0, 0);
        for(int i = 0;i < 7; i++){
            calenderview = date.plusDays(i);
            DayOfWeek weekday = calenderview.getDayOfWeek(); // the day of the week i.e monday, tuesday
            int monthday = calenderview.getDayOfMonth(); // 1-31
            Text day = new  Text(weekday.toString().toLowerCase() + " " + monthday);
            calendargrid.add(day, i+1,0);



            if(events.size() != 0){ // if there are events
                for (Events e: events){
                    if(e.getOccur().equals("One-Time")) { // if the event is one time
                        if (e.getDate().toString().substring(0, 10).equals(calenderview.toString().substring(0, 10))) { // if date matches
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Daily")) { // if the event is daily
                        if (calenderview.isAfter(e.getDate())) {
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Weekly")){ // if the event is weekly
                        if(calenderview.isAfter(e.getDate()) && e.getDate().getDayOfWeek().equals(calenderview.getDayOfWeek())){
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }

                        }

                    }


                }
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

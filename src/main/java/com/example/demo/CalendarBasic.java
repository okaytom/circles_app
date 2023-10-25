package com.example.demo;


import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;


public class CalendarBasic {

    /**
     * calendar stage
     */
    Stage calendar_stage;

    /**
     *  Object for holding date time
     */
    ZonedDateTime date = ZonedDateTime.now();


    /**
     * Scene for adding events
     */
    Scene event_scene;

    /**
     * Scene for going back home
     */
    Scene home_scene;



    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<>();

    //IntegerProperty update = new SimpleIntegerProperty(this, "update", 0);

    BorderPane display = new BorderPane();

    String[] months_lst = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};


    public CalendarBasic (Stage window, Scene home){
        calendar_stage = window;
        home_scene = home;

    }



    // Tanner and Tommy
    public void  drawCalendar(){

        ZonedDateTime calenderview = date;

        System.out.println(date.toString().substring(0,10));
        // creating calendar
        GridPane calendar = new GridPane();
        calendar.setVgap(1);
        calendar.setHgap(1);
        calendar.setStyle("-fx-background-color: turquoise");

        Text blank = new Text("           ");
        calendar.add(blank, 0, 0);
        for(int i = 0;i < 7; i++){
            calenderview = date.plusDays(i);
            DayOfWeek weekday = calenderview.getDayOfWeek(); // the day of the week i.e monday, tuesday
            int monthday = calenderview.getDayOfMonth(); // 1-31
            Text day = new  Text(weekday.toString().toLowerCase() + " " + monthday);
            calendar.add(day, i+1,0);



            if(events.size() != 0){ // if there are events
                for (Events e: events){
                    if(e.getOccur().equals("One-Time")) {
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
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
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

                                calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Daily")) {
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
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
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

                                calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Weekly")){
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
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
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

                                calendar.add(event_display, i + 1, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }

                        }

                    }


                }
            }
        }


        Text hour = new Text("12:00 AM    ");
        calendar.add(hour, 0, 1);
        for (int i = 1; i <= 11; i++){
            Text hour_after_12 = new Text(i + ":00 AM    ");
            calendar.add(hour_after_12, 0, i+1);
        }
        hour = new Text("12:00 PM    ");
        calendar.add(hour, 0, 13);
        for (int i = 1; i <= 11; i++){
            Text hour_after_12 = new Text(i + ":00 PM    ");
            calendar.add(hour_after_12, 0, i + 13);
        }


        for(int j = 0; j<=24; j++){
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(100);
            calendar.getRowConstraints().add(r);
        }

        calendar.setGridLinesVisible(true);


        // creating layout for button and labels
        HBox top = new HBox();
        top.setAlignment(Pos.TOP_RIGHT);
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setSpacing(10);

        Button add_event = new Button("Add event");
        add_event.getStyleClass().add("button");
        add_event.setOnAction(e -> AddEvent());

        Button previous_week = new Button("Previous week");
        previous_week.getStyleClass().add("button");
        previous_week.setOnAction(e -> {
            date = date.minusWeeks(1); // subtract one week from the calendar and update display
            drawCalendar();
        });

        Button next_week = new Button("Next week");
        next_week.getStyleClass().add("button");
        next_week.setOnAction(e -> {
            date = date.plusWeeks(1);
            drawCalendar();
        });

        Button remove_event = new Button("Remove Event");
        remove_event.getStyleClass().add("button");
        // TODO remove_event.setOnAction(e -> Remove());


        Label month = new Label(date.getMonth().toString());
        month.setFont(new Font(24));

        top.setStyle("-fx-background-color: turquoise;");
        top.getChildren().addAll(previous_week, remove_event, month, add_event, next_week);
        display.setTop(top);

        display.setBottom(calendar);

        // at the end of the function, update the calendar in the main application
        //update.setValue(update.get() + 1);
    }


    //Tommy
    /**
     * Makes a new scene for adding events into the calendar
     */
    private void AddEvent() {
        //Grid for layout
        GridPane event_layout = new GridPane();
        event_layout.setPadding(new Insets(10, 10, 10, 10));
        event_layout.setHgap(7);
        event_layout.setVgap(7);

        // label on the left, choice on the right
        Label name = new Label("Event");
        GridPane.setConstraints(name, 0, 0);
        TextField subject = new TextField();
        GridPane.setConstraints(subject, 1, 0);

        Label year = new Label("Year");
        GridPane.setConstraints(year, 0, 1);
        ChoiceBox<String> years = new ChoiceBox<>();
        years.getItems().addAll("2023", "2024", "2025", "2026",
                "2027", "2028", "2029", "2030", "2031", "2032",
                "2033", "2034");
        GridPane.setConstraints(years, 1, 1);


        Label month = new Label("Month");
        GridPane.setConstraints(month, 0, 2);
        ChoiceBox<String> months = new ChoiceBox<>();
        months.getItems().addAll(months_lst);
        GridPane.setConstraints(months, 1, 2);

        Label day = new Label("Day");
        GridPane.setConstraints(day, 0, 3);
        TextField chosen_day = new TextField();
        GridPane.setConstraints(chosen_day, 1, 3);

        Label occurence = new Label("Occurence");
        GridPane.setConstraints(occurence, 0, 4);
        ChoiceBox<String> repeating = new ChoiceBox<>();
        repeating.getItems().addAll("One-Time", "Daily", "Weekly");
        // default value
        repeating.setValue("One-Time");
        GridPane.setConstraints(repeating, 1, 4);

        Label stime = new Label("Start Time");
        GridPane.setConstraints(stime, 0, 5);
        TextField starttime = new TextField();
        GridPane.setConstraints(starttime, 1, 5);
        ChoiceBox<String> am_or_pm = new ChoiceBox<>();
        am_or_pm.getItems().addAll("AM", "PM");
        am_or_pm.setValue("AM");
        GridPane.setConstraints(am_or_pm, 2, 5);

        Label etime = new Label("End Time");
        GridPane.setConstraints(etime, 0, 6);
        TextField endtime = new TextField();
        GridPane.setConstraints(endtime, 1, 6);
        ChoiceBox<String> am_or_pm_etime = new ChoiceBox<>();
        am_or_pm_etime.getItems().addAll("AM", "PM");
        am_or_pm_etime.setValue("AM");
        GridPane.setConstraints(am_or_pm_etime, 2, 6);


        Label cat = new Label("Category");
        GridPane.setConstraints(cat, 0, 7);
        ComboBox<String> category = new ComboBox<>(); //set it to combobox so user can put in their own category
        category.getItems().addAll("Work", "School");
        category.setEditable(true);
        category.setValue("School");
        GridPane.setConstraints(category, 1, 7);

        //Go back to calendar
        Button calendar = new Button("Confirm added event");
        calendar.setOnAction(e -> {
            // save AM or PM as an integer where AM = 1 and PM = 0
            boolean start_am = true;
            boolean end_am = true;
            if(am_or_pm.getValue().equals("PM")){
                start_am = false;
            }
            if(am_or_pm_etime.getValue().equals("PM")){
                end_am = false;
            }
            VerifyEventData(years.getValue(), months.getValue(), chosen_day.getText(), subject.getText(),
                    repeating.getValue(), starttime.getText(), endtime.getText(), start_am, end_am,
                    category.getValue(), 0);
        });
        GridPane.setConstraints(calendar, 0, 8);

        Button go_back = new Button("Go back");
        go_back.setOnAction(e -> calendar_stage.setScene(home_scene));
        GridPane.setConstraints(go_back, 0, 9);



        event_layout.getChildren().addAll(name, subject, year, years, months, month,
                day, chosen_day, occurence, repeating, starttime, stime, am_or_pm,
                cat, category, endtime, etime, am_or_pm_etime, calendar, go_back);

        event_scene = new Scene(event_layout, 652, 480);
        calendar_stage.setScene(event_scene);

    }

    // Tommy
    private void VerifyEventData(String year, String month, String d, String subject,
                                 String occur, String starttime, String endtime,
                                 boolean start_am, boolean end_am, String category, int prio
    ){
        Events new_event = new Events();
        int day;

        try{
            // check to see that all text fields were filled
            if (year == null || month == null || d == null || subject == null
            || occur == null || starttime == null || endtime == null || category == null){
                throw new IOException();
            }

            // check to see day entered is an int
            day = Integer.parseInt(d);

            // get month number
            int month_num;
            for (month_num = 1; month_num <= 12; month_num++){
                if (months_lst[month_num-1].equals(month)){
                    break;
                }
            }


            // I could not enable assert for some reason so I had to do it the stupid way
            if(month_num == 9 || month_num == 4 || month_num == 6 || month_num == 11){
                if(day < 1 || day > 30){
                    throw new AssertionError();
                }
            }
            else if(month_num == 2  && Integer.parseInt(year) % 4 == 0 ){
                if(day < 1 || day > 29){
                    throw new AssertionError();
                }
            }
            else if(month_num == 2){
                if(day < 1 || day > 28){
                    throw new AssertionError();
                }
            }
            else{
                if(day < 1 || day > 31){
                    throw new AssertionError();
                }
            }

            // checking start and end times
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d";
            if(!(starttime.matches(regex) && endtime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));
            int end = Integer.parseInt(endtime.replaceFirst(":", ""));


            if(!start_am){
                start = start + 1200;
            }

            if(!end_am){
                end = end + 1200;
            }

            //check to see start time is before end time
            if (start > end){
                throw new ArithmeticException();
            }


            new_event.setDate(Integer.parseInt(year) , month_num , day);
            new_event.setSubject(subject);
            new_event.setOccur(occur);
            new_event.setStarttime(Integer.toString(start));
            new_event.setEndtime(Integer.toString(end));
            new_event.setCategory(category);
            new_event.setPriorityLevel(prio);
            System.out.println("Event date is :" + new_event.getDate());
            System.out.println("Event subject is: " + new_event.getSubject());
            System.out.println("Event occurence is: " + new_event.getOccur());
            System.out.println("Event start time is: " + new_event.getStarttime());
            System.out.println("Event end time is: " + new_event.getEndtime());
            System.out.println("Event category is: " + new_event.getCategory());
            events.add(new_event);



            // Updating calendar
            drawCalendar();

//            if(new_event.getOccur() == "One-Time"){
//                //calendar.add(event_display,4, 10);
//                Integer.parseInt(new_event.getStarttime().substring(0,new_event.getStarttime().length()-1)+1);
//
//                //calendar.addColumn(4, event_display);
//                calendar.addRow(4, event_display);
//                display.setBottom(calendar);
//            }
//            else if (new_event.getOccur() == "Daily"){
//
//            }
//            else{
//
//            }


            // acts as a trigger for the application to update the calendar
            //update.setValue(events.size());

            calendar_stage.setScene(home_scene);
        }
        catch (IOException t){
            AlertBox.display("Empty Textfields", "Fill in all textfields");
        }
        catch (NumberFormatException e){
            AlertBox.display("Error in day", "Day must be an int");
        }
        catch (AssertionError a){
            AlertBox.display("Error in day", "Day is outside range");
        }
        catch (IllegalArgumentException i){
            AlertBox.display("Error in time", "Must be in format 'hour:minutes'");
        }
        catch (ArithmeticException x){
            AlertBox.display("Error in time", "Start time must be before endtime");
        }


    }
}

//        Menu circle_menu = new Menu("Circle");
//        MenuItem close = new MenuItem("Close");
//        close.setOnAction(e-> calendar_stage.close());
//        circle_menu.getItems().add(close);
//
//        Menu calendar_menu = new Menu("calendar");
//        MenuItem addevent = new MenuItem("Add event");
//        addevent.setOnAction(e -> AddEvent());
//        calendar_menu.getItems().add(addevent);
//
//        Menu files_menu = new Menu("Files");
//
//        Menu settings = new Menu("Settings");
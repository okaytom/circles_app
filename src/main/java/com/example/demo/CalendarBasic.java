package com.example.demo;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CalendarBasic {

    /**
     * calendar stage
     */
    Stage calendar_stage;
    /**
     * Scene for calendar view
     */
    Scene event_scene;

    /**
     * Scenes for event view
     */
    Scene calendar_scene;

    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<Events>();

    String[] months_lst = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};


    public CalendarBasic (Stage window){
        calendar_stage = window;
    }


    //Tanner
    private GridPane addcalendar(){
        GridPane calendar = new GridPane();
        calendar.setVgap(1);
        calendar.setHgap(1);
        calendar.setStyle("-fx-background-color: turquoise");


        Text blank = new Text("           ");
        Text sunday = new Text("Sunday     ");
        Text monday = new Text("Monday     ");
        Text tuesday = new Text("Tuesday    ");
        Text wednesday = new Text("Wednesday  ");
        Text thursday = new Text("Thursday   ");
        Text friday = new Text("Friday     ");
        Text saturday = new Text("Saturday   ");

        calendar.add(blank, 0, 0);
        calendar.add(sunday, 1,0);
        calendar.add(monday, 2,0);
        calendar.add(tuesday, 3,0);
        calendar.add(wednesday, 4,0);
        calendar.add(thursday, 5,0);
        calendar.add(friday, 6,0);
        calendar.add(saturday, 7,0);


        int nums = 12;
        for (int i = 1; i <= nums; i++){
            Text hour = new Text(i + ":00 AM    ");
            calendar.add(hour, 0, i);
        }
        for (int i = 1; i <= nums; i++){
            Text hour = new Text(i + ":00 PM    ");
            calendar.add(hour, 0, i + 12);
        }
        for(int j = 0; j<=24; j++){
            RowConstraints r = new RowConstraints();
            calendar.getRowConstraints().add(r);
            r.setPercentHeight(100);
        }

        calendar.setGridLinesVisible(true);

        return calendar;
    }

    // Tommy
    public Scene CalenderScene(){
        BorderPane display = new BorderPane();
        GridPane calendar = addcalendar();
        display.setRight(calendar);

        // creating layout with buttons and labels
        GridPane top = new GridPane();
        Button add_event = new Button("Add event");
        add_event.setOnAction(e -> AddEvent());
        top.getChildren().addAll(add_event);
        display.setTop(top);



        calendar_scene = new Scene(display, 640, 480);

        return calendar_scene;
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
        ChoiceBox<String> category = new ChoiceBox<>();
        category.getItems().addAll("Work", "School", "Other");
        GridPane.setConstraints(category, 1, 7);

        //Go back to calendar
        Button calendar = new Button("Confirm added event");
        calendar.setOnAction(e -> {
            // save AM or PM as an integer where AM = 1 and PM = 0
            boolean start_am = true;
            boolean end_am = true;
            if(am_or_pm.getValue() == "PM"){
                start_am = false;
            }
            if(am_or_pm_etime.getValue() == "PM"){
                end_am = false;
            }
            VerifyEventData(years.getValue(), months.getValue(), chosen_day.getText(), subject.getText(),
                    repeating.getValue(), starttime.getText(), endtime.getText(), start_am, end_am,
                    category.getValue(), 0);
        });
        GridPane.setConstraints(calendar, 0, 8);

        Button go_back = new Button("Go back");
        go_back.setOnAction(e -> calendar_stage.setScene(calendar_scene));
        GridPane.setConstraints(go_back, 0, 9);



        event_layout.getChildren().addAll(name, subject, year, years, months, month,
                day, chosen_day, occurence, repeating, starttime, stime, am_or_pm,
                cat, category, endtime, etime, am_or_pm_etime, calendar, go_back);

        event_scene = new Scene(event_layout, 640, 480);

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
                if (months_lst[month_num-1] == month){
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

            new_event.setDate(day + "-" + month_num + "-" + year);
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
            calendar_stage.setScene(calendar_scene);
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
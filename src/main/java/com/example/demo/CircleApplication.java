package com.example.demo;

/**
 * TOMMY OJO
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.Button;

public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    Stage window;

    /**
     * Button for adding events
     */
    Button add_event;

    /**
     * Scene for Calender view
     */
    Scene event_scene;

    /**
     * Scenes for event view
     */
    Scene calender_scene;

    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<Events>();

    String[] months_lst = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};
    @Override
    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });


        // creating layout with buttons and labels
        VBox layout = new VBox(30);
        add_event = new Button("Add event");
        add_event.setOnAction(e -> AddEvent());
        layout.getChildren().addAll(add_event);

        calender_scene = new Scene(layout, 640, 480);
        window.setScene(calender_scene);


        window.show();


    }

    /**
     * Makes a new scene for adding events into the calender
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

        Label etime = new Label("End Time");
        GridPane.setConstraints(etime, 0, 6);
        TextField endtime = new TextField();
        GridPane.setConstraints(endtime, 1, 6);

        Label cat = new Label("Category");
        GridPane.setConstraints(cat, 0, 7);
        ChoiceBox<String> category = new ChoiceBox<>();
        category.getItems().addAll("Work", "School", "Other");
        GridPane.setConstraints(category, 1, 7);

        //Go back to calender
        Button calender = new Button("Confirm added event");
        calender.setOnAction(e -> {
            VerifyEventData(years.getValue(), months.getValue(), chosen_day.getText(), subject.getText(),
                    repeating.getValue(), starttime.getText(), endtime.getText(), category.getValue(), 0);
        });
        GridPane.setConstraints(calender, 0, 8);

        Button go_back = new Button("Go back");
        go_back.setOnAction(e -> window.setScene(calender_scene));
        GridPane.setConstraints(go_back, 0, 9);



        event_layout.getChildren().addAll(name, subject, year, years, months, month,
                day, chosen_day, occurence, repeating, starttime, stime,
                cat, category, endtime, etime, calender, go_back);

        event_scene = new Scene(event_layout, 640, 480);

        window.setScene(event_scene);

    }

    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            // TODO Add SaveState here
            window.close();
        }
    }

    /**
     * Adds a new event to calender IFF the parameters given are correct
     * @param year year of new event
     * @param month month of new event
     * @param d day of new event
     * @param subject name of new event
     * @param occur occurence of new event
     * @param prio priority of new event
     * @param starttime starting time of event
     * @param endtime ending time of event
     * @param category category of event (work, school, other)
     */
    private void VerifyEventData(String year, String month, String d, String subject,
    String occur, String starttime, String endtime, String category, int prio){
        Events new_event = new Events();
        int day;

        try{
            day = Integer.parseInt(d);


            // TODO assertion is not enabling and idk why
            assert day <= 31;
            assert day >= 1;

            // get month number
            int month_num;
            for (month_num = 1; month_num <= 12; month_num++){
                if (months_lst[month_num-1] == month){
                    break;
                }
            }

            new_event.setDate(Integer.toString(day) + "-" + month_num + "-" + year);
            new_event.setSubject(subject);
            new_event.setOccur(occur);
            new_event.setStarttime(starttime);
            new_event.setEndtime(endtime);
            new_event.setCategory(category);
            new_event.setPriorityLevel(prio);
            System.out.println("Event date is :" + new_event.getDate());
            System.out.println("Event subject is: " + new_event.getSubject());
            System.out.println("Event occurence is: " + new_event.getOccur());
            System.out.println("Event start time is: " + new_event.getStarttime());
            System.out.println("Event end time is: " + new_event.getEndtime());
            System.out.println("Event category is: " + new_event.getCategory());
            events.add(new_event);
            window.setScene(calender_scene);
        }
        catch (NumberFormatException e){
            AlertBox.display("Error in day", "Day must be an int");

        }
        catch (AssertionError a){
            AlertBox.display("Error in day", "Day is outside range");
        }


    }

    public static void main(String[] args) {
        launch();
    }
}
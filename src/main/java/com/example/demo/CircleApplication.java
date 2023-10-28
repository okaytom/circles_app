package com.example.demo;

/**
 * TOMMY OJO
 */

        import javafx.application.Application;
        import javafx.geometry.Insets;
        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.VBox;
        import javafx.scene.paint.Color;
        import javafx.scene.shape.Circle;
        import javafx.scene.text.Font;
        import javafx.stage.Stage;
        import java.io.IOException;


public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    Stage window;


    Scene calendar_scene;

    String filepath = SaveState.devFolder + "/Events.json";

    CalendarBasic calendar_obj;

    Scene setting_scene;

    Scene files_scene;

    Scene search_scene;

    public void start(Stage primarystage) throws IOException {
        // setting main window
        window = primarystage;
        window.setTitle("Circle");
        BorderPane maindisplay = new BorderPane();

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });





        // making calendar scene
        calendar_scene = new Scene(maindisplay, 652, 480);
        calendar_scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        /* calendar object*/
        calendar_obj = new CalendarBasic(window, calendar_scene);
        calendar_obj.events = SaveState.Load(filepath, Events.class);

        //listener for whenever the calendar redraws itself
        calendar_obj.display.bottomProperty().addListener( (v,oldvalue, newvalue) -> {
            maindisplay.setRight(calendar_obj.display);
        });

        // draw the initial calendar
        calendar_obj.drawCalendar();


        // Creating layout for sidebar
        Button circle = new Button("Circle");
        circle.setFont(new Font( 20));
        circle.setPrefWidth(161);
        circle.getStyleClass().add("button");

        Button calendar = new Button("Calendar");
        calendar.setFont(new Font( 20));
        calendar.setPrefWidth(161);
        calendar.getStyleClass().add("button");
        calendar.setOnAction(e -> window.setScene(calendar_scene));


        Button files = new Button("Files");
        files.setFont(new Font( 20));
        files.setPrefWidth(161);
        files.getStyleClass().add("button");
        // TODO action for files

        Button search = new Button("Search");
        search.setFont(new Font( 20));
        search.setPrefWidth(161);
        search.getStyleClass().add("button");
        // TODO action for search

        Button settings = new Button("Settings");
        settings.setFont(new Font( 20));
        settings.setPrefWidth(161);
        settings.getStyleClass().add("button");
        // TODO action for settings


        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: blue;");
        sidebar.setFillWidth(true);

        sidebar.getChildren().addAll(circle, calendar,files, search, settings);
        maindisplay.setLeft(sidebar);



        // WELCOME PAGE
        // making calendar scene
        VBox root = new VBox();
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Label myText = new Label("Study W/ Me");

        myText.setFont(new Font("Elephant", 75));
        myText.setPadding(new Insets(0, 0, 130, 0));
        myText.setTextFill(Color.CORNFLOWERBLUE);

        double radius = 175;
        Button circleButton = new Button("GO");
        circleButton.setFont(new Font("Elephant", 45));
        circleButton.setTextFill(Color.CORNFLOWERBLUE);
        circleButton.setLayoutX(250);
        circleButton.setLayoutY(350);
        circleButton.setShape(new Circle(radius));
        circleButton.getStyleClass().add("button");
        circleButton.setPrefSize(radius, radius);
        circleButton.setOnAction(e -> window.setScene(calendar_scene));

        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("background");

        root.getChildren().addAll(myText, circleButton);
        window.setTitle("Hello World");

        window.setScene(new Scene(root, 652, 480));
        window.show();
    }


    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            SaveState.Save(filepath, calendar_obj.events);
            window.close();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
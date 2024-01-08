package com.example.demo;

// Tyler Chow
import com.example.demo.Model.SaveState;

import java.io.File;
public class Launcher {
    public static void main(String[] args) {

        //creates a directory for the app
        File appDir = new File(SaveState.appName);
        appDir.mkdir();

        File devfile = new File(SaveState.devFolder);
        devfile.mkdir();

        CircleApplication.main(args);
    }
}

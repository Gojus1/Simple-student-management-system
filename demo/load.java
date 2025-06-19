package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Say Hello");
        btn.setOnAction(e -> System.out.println("Hello, JavaFX!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Test JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

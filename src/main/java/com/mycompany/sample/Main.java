package com.mycompany.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.gillius.jfxutils.JFXUtil;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Region contentRootRegion = (Region) loader.load();

        StackPane root = JFXUtil.createScalePane( contentRootRegion, 900, 700, false );
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Charting Example");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

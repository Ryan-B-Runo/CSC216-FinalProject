package com.example.csc216finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {

    private Pane[][] panes = new Pane[26][26];

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        for(int i = 0; i < panes.length; i++) {
            for(int j = 0; j < panes[0].length; j++) {
                Pane box = createColorBox();
                int finalI = i;
                int finalJ = j;
                box.setOnMouseClicked(event -> {
                    if(event.getClickCount() == 2) {//double click to bucket fill
                        try {
                            bucketFill(finalI, finalJ);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                box.setOnDragDetected(event -> {
                    box.startFullDrag();
                });
                box.setOnMouseDragEntered(event -> {
                    if(box.getStyle().equals("-fx-background-color: white; -fx-border-color: black;")) {
                        box.setStyle("-fx-background-color: black;");
                    }else if(box.getStyle().equals("-fx-background-color: black;")) {
                        box.setStyle("-fx-background-color: white; -fx-border-color: black;");
                    }
                });
                gridPane.add(box, j, i);
                panes[i][j] = box;
            }
        }

        Scene scene = new Scene(gridPane);
        stage.setTitle("Bucket Fill");
        stage.setScene(scene);
        stage.show();

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Instructions");
        a.setContentText("Click and drag to make borders.\nDouble click to fill.");
        a.showAndWait();
    }

    private Pane createColorBox() {
        Pane box = new Pane();
        box.setPrefSize(20, 20);
        box.setStyle("-fx-background-color: white; -fx-border-color: black;"); // Initial color and border

        return box;
    }

    private void bucketFill(int x, int y) throws InterruptedException {

        String oldStyle = panes[x][y].getStyle();

        if(oldStyle.equals("-fx-background-color: red; -fx-border-color: black;") || oldStyle.equals("-fx-background-color: black;")){
            return;
        }
        ArrayDeque<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        queue.add(new Pair<>(x, y));

        while(!queue.isEmpty()){
            Pair<Integer, Integer> pair = queue.poll();
            int currentX = pair.getKey();
            int currentY = pair.getValue();
            if(currentX < 0 || currentX >= 26 || currentY < 0 || currentY >=26 || !panes[currentX][currentY].getStyle().equals(oldStyle)){
                continue;
            }else{
                panes[currentX][currentY].setStyle("-fx-background-color: red; -fx-border-color: black;");
                queue.add(new Pair<>(currentX+1, currentY));
                queue.add(new Pair<>(currentX-1, currentY));
                queue.add(new Pair<>(currentX, currentY+1));
                queue.add(new Pair<>(currentX, currentY-1));
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

package com.ita07.autoclicker.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MainController{

    final ToggleGroup group = new ToggleGroup();
    private MouseButton mouseSelected = MouseButton.PRIMARY;
    Robot robot;
    Map<KeyCode, Timeline> activeKeys = new HashMap<>();
    @FXML
    private VBox vbox;
    @FXML
    private RadioButton leftRadioButton;
    @FXML
    private RadioButton rightRadioButton;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField textField;
    @FXML
    public void initialize() {
        robot = new Robot();
        //Add an event filter in the scene
        vbox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                //Validate that the user pressed the required KeyCode
                if (event.getCode().equals(KeyCode.F6)) {
                    //Check if interval is set
                    if (textField.getText().isEmpty()) {
                        //Popup Warning to remind the user to input the interval
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        //Referencing a node from the scene so i can link the warning to the stage in order to use the stage's icon
                        alert.initOwner(rightRadioButton.getScene().getWindow());
                        //Set the rest messages
                        alert.setTitle("Warning");
                        alert.setHeaderText("");
                        alert.setContentText("You must provide an interval!");
                        //Waits for the user to respond(Click Ok or exit button)
                        alert.showAndWait();
                    }
                    //Check if KeyCode has already been pressed
                    else if (activeKeys.containsKey(event.getCode())) {
                        //Get it from the Hashmap
                        activeKeys.get(event.getCode()).stop();
                        //And delete it
                        activeKeys.remove(event.getCode());
                    }else {
                        // Not yet running ==> Start it
                        // Execute mouse click first time
                        robot.mouseClick(mouseSelected);
                        Timeline timeline = new Timeline(new KeyFrame(setDuration(), event2 -> {
                            //Do until same keyCode is Pressed
                            robot.mouseClick(mouseSelected);
                        }
                        ));
                        //Run Cycle Forever
                        timeline.setCycleCount(Animation.INDEFINITE);
                        // Start it
                        timeline.play();
                        // Add KeyCode pressed(F6) and timeline in the HashMap
                        activeKeys.put(event.getCode(), timeline);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //Bind radiobuttons so only one can be pressed
        leftRadioButton.setToggleGroup(group);
        rightRadioButton.setToggleGroup(group);
        leftRadioButton.setSelected(true);

        //Assign Values for the combobox
        comboBox.getItems().addAll("ms", "sec", "min" , "hour");
        //Set a prompt value
        comboBox.setValue("ms");

        //Restrict the user to type anything else other than numbers from 1-9
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            //Return the number pressed
            if (text.matches("[0-9]*")) {
                return change;
            }
            //If something else is entered return nothing
            return null;
        };
        //Apply the filter in the textfield
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    @FXML
    //Handle left radiobutton press and set the value for the mouse
    void leftPressed(ActionEvent event) {
        mouseSelected = MouseButton.PRIMARY;
    }

    @FXML
    //Handle left radiobutton press and set the value for the mouse
    void rightPressed(ActionEvent event) {
        mouseSelected = MouseButton.SECONDARY;
    }

    public Duration setDuration() {
        // Parse the duration between each click
        int interval = Integer.parseInt(textField.getText());
            switch (comboBox.getValue()) {
                case "ms":
                    return Duration.millis(interval);
                case "sec":
                    return Duration.seconds(interval);
                case "min" :
                    return Duration.minutes(interval);
                case "hour" :
                    return Duration.hours(interval);
            }
        return null;
    }

}
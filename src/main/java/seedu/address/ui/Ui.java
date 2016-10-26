package seedu.address.ui;

import javafx.stage.Stage;
//@@author A0113992B-reused
/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

}

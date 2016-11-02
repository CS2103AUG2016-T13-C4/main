package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import com.joestelmach.natty.generated.DateParser.minutes_return;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.address.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.*;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.parser.Parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;


import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// @@author A0113992B-reused
public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    private static final String MESSAGE_NOT_LIST_COMMAND = "Listing today's tasks";
    private static final String MESSAGE_NULL_INPUT = "No user input";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    String previousCommandTest;

    private Logic logic;

    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
            ResultDisplay resultDisplay, Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

    public void configure(ResultDisplay resultDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
        this.logic = logic;
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }


    @FXML
    private void handleCommandInputChanged() throws IllegalValueException {
        //Take a copy of the command text
        previousCommandTest = commandTextField.getText();        
        /* We assume the command is correct. If it is incorrect, the command box will be changed accordingly
         * in the event handling code {@link #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        //taskLister(previousCommandTest);
        mostRecentResult = logic.execute(previousCommandTest);
        resultDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);

    }
    
    private void taskLister(String userInput) throws IllegalValueException {
        MainWindow main = new MainWindow();
        String commandWord;
        String arguments;
        
        if (userInput.length() <= 4) {
            throw new IllegalValueException(MESSAGE_NOT_LIST_COMMAND);
        } else {
            commandWord = userInput.substring(0, 3);
            arguments = userInput.substring(5, userInput.length()-1);
        }
        
        
        if (!commandWord.equals("list")) {
                main.setLabel(1);
        } else if (commandWord.equals("list")) {
            if (arguments.equals("today")) {
                main.setLabel(1);
            } else if (arguments.equals("tomorrow")) {
                main.setLabel(2);
            } else if (arguments.equals("all")) {
                main.setLabel(3);
            }
        }  
    }
    
    
    
    /**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event,"Invalid command: " + previousCommandTest));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandTest);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }
    
    /**
     * Gets input text of the command field
     */    
    public String getCommandText() {
        return commandTextField.getText();
    }

}

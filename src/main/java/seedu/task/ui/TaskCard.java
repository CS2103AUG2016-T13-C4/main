package seedu.task.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import seedu.task.model.task.ReadOnlyTask;
//@@author A0113992B
public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private AnchorPane cardPane;
    @FXML
    private Label task;
    @FXML
    private Label id;
    @FXML
    private Label startdate;
    @FXML
    private Label starttime;
    @FXML
    private Label enddate;
    @FXML
    private Label endtime;
    @FXML
    private Label hashtag;
    
    private ReadOnlyTask taskentry;
    private int displayedIndex;
    
    
    public TaskCard(){

    }

    public static TaskCard load(ReadOnlyTask taskentry, int displayedIndex){
        TaskCard card = new TaskCard();
        card.taskentry = taskentry;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        // Set wrap text and alignment in case of multiple line entries
        task.setWrapText(true);
        task.setTextAlignment(TextAlignment.JUSTIFY);
        startdate.setWrapText(true);
        startdate.setTextAlignment(TextAlignment.JUSTIFY);
        starttime.setWrapText(true);
        starttime.setTextAlignment(TextAlignment.JUSTIFY);
        enddate.setWrapText(true);
        enddate.setTextAlignment(TextAlignment.JUSTIFY);
        endtime.setWrapText(true);
        endtime.setTextAlignment(TextAlignment.JUSTIFY);
        hashtag.setWrapText(true);
        hashtag.setTextAlignment(TextAlignment.JUSTIFY);
         
        task.setText(taskentry.getName().fullName);
        id.setText(displayedIndex + ". ");
        startdate.setText(taskentry.getDateTime().date_value);
        starttime.setText(taskentry.getDateTime().time_value);
        enddate.setText(taskentry.getDueTime().date_value);
        endtime.setText(taskentry.getDueTime().time_value);
        hashtag.setText(taskentry.tagsString());
    }

    public AnchorPane getLayout() {
        return cardPane;
    }
    
 // @@author A0113992B-reused
    @Override
    public void setNode(Node node) {
        cardPane = (AnchorPane)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}

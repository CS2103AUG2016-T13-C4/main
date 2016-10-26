package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import seedu.address.model.task.ReadOnlyTask;

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

    @Override
    public void setNode(Node node) {
        cardPane = (AnchorPane)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}

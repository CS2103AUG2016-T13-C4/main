package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import seedu.address.model.person.ReadOnlyTask;

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
    
    private ReadOnlyTask person;
    private int displayedIndex;
    
    public TaskCard(){

    }

    public static TaskCard load(ReadOnlyTask person, int displayedIndex){
        TaskCard card = new TaskCard();
        card.person = person;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        task.setText(person.getName().fullName);
        id.setText(displayedIndex + ". ");
        startdate.setText(person.getDateTime().date_value);
        starttime.setText(person.getDateTime().time_value);
        enddate.setText(person.getDueTime().date_value);
        endtime.setText(person.getDueTime().time_value);
        hashtag.setText(person.tagsString());
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

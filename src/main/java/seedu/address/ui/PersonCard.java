package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.person.ReadOnlyTask;

public class PersonCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

//    @FXML
//    private HBox cardPane;
//    @FXML
//    private Label name;
//    @FXML
//    private Label id;
//    @FXML
//    private Label phone;
//    @FXML
//    private Label address;
//    @FXML
//    private Label email;
//    @FXML
//    private Label tags;

    //my code
    @FXML
    private HBox cardPane;
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
    // end of my code
    
    private ReadOnlyTask person;
    private int displayedIndex;
    
    public PersonCard(){

    }

    public static PersonCard load(ReadOnlyTask person, int displayedIndex){
        PersonCard card = new PersonCard();
        card.person = person;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
    	String dateMessage = "Date: ", timeMessage = "Time: ";
    	if (person.getDueTime().date_value == "") {
    		dateMessage += person.getDateTime().date_value;
    		timeMessage += person.getDateTime().time_value;
        } else {
        	dateMessage += person.getDateTime().date_value + " to " + person.getDueTime().date_value;
    		timeMessage += person.getDateTime().time_value + " to " + person.getDueTime().time_value;
        }
        task.setText(person.getName().fullName);
        id.setText(displayedIndex + ". ");
        startdate.setText(dateMessage);
        starttime.setText(timeMessage);
        //email.setText(person.getAddress().value);
        //tags.setText(person.tagsString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}

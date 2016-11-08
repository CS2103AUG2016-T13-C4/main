package seedu.task.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;

public class TaskCardTest {
    
    private AnchorPane cardPane;
    private AnchorPane replacement;
    
    UiPartLoader loader = new UiPartLoader();
    
    
    @SuppressWarnings("static-access")
    @Test
    public void test() throws IllegalValueException {
        TaskName name;
        DateTime dateTime;
        DueDateTime dueDateTime;
        UniqueTagList tags;
        boolean isDone; 
        
        Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        
        name = new TaskName("buy grocery");
        dateTime  = new DateTime("nov 4 3pm");
        dueDateTime = new DueDateTime("nov 4 5pm");
        tags = new UniqueTagList(t1,t2,t3);
        isDone = true;
        
        // test constructor
        TaskCard card = new TaskCard();
        TaskCard another = new TaskCard();
        
//       // test load function        
//        Task task1 = new Task(name, dateTime, dueDateTime, tags, isDone);
//        assertEquals(card.load(task1, 1), another.load(task1, 1));
        
        // test getFXMLPath function
        String path = "TaskListCard.fxml";
        assertEquals(card.getFxmlPath(), path);
        
        // test getLayout function
        assertEquals(cardPane, card.getLayout());
        
        // test setNode function
        card.setNode(replacement);
        assertEquals(cardPane, replacement);
        
    }
}

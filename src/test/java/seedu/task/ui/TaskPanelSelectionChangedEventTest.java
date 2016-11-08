package seedu.task.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.task.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;

public class TaskPanelSelectionChangedEventTest {    
    TaskName name;
    DateTime dateTime;
    DueDateTime dueDateTime;
    UniqueTagList tags;
    boolean isDone;   

    @Test
    public void test() throws IllegalValueException {
        Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        
        name = new TaskName("buy grocery");
        dateTime  = new DateTime("nov 4 3pm");
        dueDateTime = new DueDateTime("nov 4 5pm");
        tags = new UniqueTagList(t1,t2,t3);
        isDone = true;
        Task task1 = new Task(name, dateTime, dueDateTime, tags, isDone);
        
        // test constructor 
        TaskPanelSelectionChangedEvent event = new TaskPanelSelectionChangedEvent(task1);
        
        // test toString
        assertEquals(event.toString(), "TaskPanelSelectionChangedEvent");
        
        // test getNewSelection method
        assertEquals(event.getNewSelection(), task1);
        
    }
}

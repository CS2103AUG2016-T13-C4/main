// @@author A0113992B
package seedu.task.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;

public class TaskTest {
    
    @Test
    public void test() throws IllegalValueException {      
        Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        
        TaskName name = new TaskName("buy grocery");
        DateTime dateTime  = new DateTime("nov 4 3pm");
        DueDateTime dueDateTime = new DueDateTime("nov 4 5pm");
        UniqueTagList tags = new UniqueTagList(t1,t2,t3);
        boolean isDone = true;
        
        // test constructor
        Task task1 = new Task(name, dateTime, dueDateTime, tags, isDone);
        
        // test getters
        assertEquals(task1.getName(), name);
        assertEquals(task1.getDateTime(), dateTime);
        assertEquals(task1.getDueTime(), dueDateTime);
        assertEquals(task1.getTags(), tags);
        
        // test setUndone function
        boolean test = false;
        task1.setUndoneTask();
        assertEquals(task1.isDoneTask(), test);
        
        // test setDone function
        test = true;
        task1.setDoneTask();
        assertEquals(task1.isDoneTask(), test);
        
        // test toString
        assertEquals(task1.toString(), "buy grocery Start: 04 Nov 2016 (15:00 Hrs) "
                     + "End: 04 Nov 2016 (17:00 Hrs) Tags: [impt][urgent][school]");
       
    }
}

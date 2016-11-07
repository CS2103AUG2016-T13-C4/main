// @@author A0113992B
package seedu.task.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.AddCommand;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;

public class AddCommandTest {
    
    @Test
    public void test() throws IllegalValueException {
        Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(t1, t2, t3));
        
        TaskName name = new TaskName("buy grocery");
        DateTime dateTime  = new DateTime("nov 4 3pm");
        DateTime dateTime2  = new DateTime();
        DueDateTime dueDateTime = new DueDateTime("nov 4 5pm");
        DueDateTime dueDateTime2 = new DueDateTime();
        UniqueTagList utags = new UniqueTagList(tags);
        boolean isDone = false;
        
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date1 = new Date(stamp.getTime());
        Date date2 = new Date(stamp.getTime() + 1000);
        List<Date> datelist = Arrays.asList (date1, date2);
        
        Task task1 = new Task(name, dateTime2, dueDateTime, utags, isDone);
        Task task2 = new Task(name, dateTime, dueDateTime, utags, isDone);
        Task task3 = new Task(name, dateTime2, dueDateTime2, utags, isDone);
        
        // test createNormalTask function 
        Task test_task1 = AddCommand.createNormalTask("buy grocery", "nov 4 5pm", tags);    
        assertEquals(task1.toString(), test_task1.toString());
        
        // test createEvents function
        Task test_task2 = AddCommand.createEvent("buy grocery", "nov 4 3pm to nov 4 5pm", tags, datelist);
        assertEquals(task2.toString(), test_task2.toString());
        
        // test createFloating function
        Task test_task3 = AddCommand.createFloatingTask("buy grocery", tags);
        assertEquals(task3.toString(), test_task3.toString());
        
        // test checkDateCount function
        Task check_normal = AddCommand.checkDateCount("buy grocery", "nov 4 5pm", tags); 
        assertEquals(task1.toString(), check_normal.toString());
        
        Task check_event = AddCommand.checkDateCount("buy grocery", "nov 4 3pm to nov 4 5pm", tags); 
        assertEquals(task2.toString(), check_event.toString());
  
        
    }
    
}

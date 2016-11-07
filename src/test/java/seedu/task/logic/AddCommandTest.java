// @@author A0113992B
package seedu.task.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.AddCommand;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;

public class AddCommandTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();

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
    
    // @@author A0135763B
    @Test
    public void testTripleDateCheckDateCount() throws IllegalValueException {
    	Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(t1, t2, t3));
    	
        exception.expect(IllegalValueException.class);
    	AddCommand.checkDateCount("buy grocery", "nov 4 3pm to nov 4 5pm and nov 4 6pm", tags); 
    }
    
    @Test
    public void testNoDateCheckDateCount() throws IllegalValueException {
    	Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        final Set<Tag> tags = new HashSet<Tag>(Arrays.asList(t1, t2, t3));
    	
        exception.expect(IllegalValueException.class);
    	AddCommand.checkDateCount("buy grocery", "", tags); 
    }
    
    @Test
    public void testValidChronoOrder() {
    	List<Date> datelist = new ArrayList<Date>();
    	Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date1 = new Date(stamp.getTime());
        Date date2 = new Date(stamp.getTime() + 1000);
        
        // 0 dates
        assertFalse(AddCommand.validateChronoOrder(datelist));
        
        // 1 date
        datelist.add(date1);
        assertFalse(AddCommand.validateChronoOrder(datelist));
        
        // 2 dates Chronological order
        datelist.add(date2);
        assertTrue(AddCommand.validateChronoOrder(datelist));
        
        // 3 dates
        datelist.add(date1);
        assertFalse(AddCommand.validateChronoOrder(datelist));
        
        //2 dates non-chrono
        datelist.clear();
        datelist.add(date2);
        datelist.add(date1);
        assertFalse(AddCommand.validateChronoOrder(datelist));
    }
    
    @Test
    public void testRetrieveNoDate() throws IllegalValueException {
    	// test empty date string
    	exception.expect(IllegalValueException.class);
    	AddCommand.retrieveDate("");
    }
    
    
    @Test
    public void testRetrieveDate() throws IllegalValueException {    	
    	// test single date string
    	assertTrue(AddCommand.retrieveDate("November 12 2015").size() == 1);
    	assertTrue(AddCommand.retrieveDate("November 12 1100").size() == 1);
    	assertTrue(AddCommand.retrieveDate("November 12 5pm").size() == 1);
    	assertTrue(AddCommand.retrieveDate("November 12 1:23PM").size() == 1);
    	assertTrue(AddCommand.retrieveDate("5pm").size() == 1);
    	assertTrue(AddCommand.retrieveDate("today").size() == 1);
    	assertTrue(AddCommand.retrieveDate("today 6pm").size() == 1);
    	assertTrue(AddCommand.retrieveDate("today 23:11hrs").size() == 1);
    	assertTrue(AddCommand.retrieveDate("tomorrow").size() == 1);
    	assertTrue(AddCommand.retrieveDate("tomorrow 2300Hrs").size() == 1);
    	
    	// test double date string
    	assertTrue(AddCommand.retrieveDate("today to tomorrow").size() == 2);
    	assertTrue(AddCommand.retrieveDate("tomorrow to today").size() == 2);
    	assertTrue(AddCommand.retrieveDate("november 12 to november 13").size() == 2);
    	assertTrue(AddCommand.retrieveDate("november 13 to november 12").size() == 2);
    	assertTrue(AddCommand.retrieveDate("5pm to 6pm").size() == 2);
    	assertTrue(AddCommand.retrieveDate("6pm to 2pm").size() == 2);
    }
    
    
}

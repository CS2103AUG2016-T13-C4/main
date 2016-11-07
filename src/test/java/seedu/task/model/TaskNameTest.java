package seedu.task.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// @@author A0113992B
import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.task.TaskName;


public class TaskNameTest {
    
    @Test
    public void test() throws IllegalValueException  {
        
        // test constructor
        TaskName t1 = new TaskName("weirwiurhw");
        TaskName t2 = new TaskName("buy grocery");
        TaskName t3 = null;    
        
        // test toString
        assertEquals(t1.toString(), "weirwiurhw");
        assertEquals(t2.toString(), "buy grocery");
        
        // test isValid function
        assertFalse(t3.isValidName(""));
        assertFalse(t3.isValidName("buy grocery t/"));
        assertFalse(t3.isValidName("buy *%$$ grocery"));
    }
}

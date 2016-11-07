//  @@author A0113992B
package seedu.task.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.task.DueDateTime;

public class DueDateTimeTest {
    
    @Test
    public void test() throws IllegalValueException {
        // assertEquals();
        // test constructor
        DueDateTime d1 = new DueDateTime("4 Nov 3pm"); 
        DueDateTime d2 = new DueDateTime("4 Nov 21:56pm"); 
        DueDateTime d3 = new DueDateTime("4 Nov 12:00am"); 
        DueDateTime d4 = new DueDateTime("4 Nov 12:00pm"); 
        DueDateTime d5 = new DueDateTime("30 feb 3pm"); 
        DueDateTime d6 = new DueDateTime("4 Nov 3pm"); // for testing equals function
        DueDateTime d7 = new DueDateTime(); // for testing equals function
        DueDateTime d8 = new DueDateTime(); // for testing equals function
        
        // test toString
        assertEquals(d1.toString(), "04 Nov 2016 (15:00 Hrs)");
        assertEquals(d2.toString(), "04 Nov 2016 (21:56 Hrs)");
        assertEquals(d3.toString(), "04 Nov 2016 (00:00 Hrs)");
        assertEquals(d4.toString(), "04 Nov 2016 (12:00 Hrs)");
        assertEquals(d5.toString(), "01 Mar 2016 (15:00 Hrs)");
        
        // test equals function
        assertFalse(d1.equals(d6));
        assertFalse(d1.equals(d2));
        assertTrue(d7.equals(d8));
        

    }
}

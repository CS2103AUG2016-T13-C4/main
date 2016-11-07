// @@author A0113992B
package seedu.task.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.task.DateTime;

public class DateTimeTest {
    
    @Test
    public void test() throws IllegalValueException {
        // assertEquals();
        // test constructor
        DateTime d1 = new DateTime("4 Nov 3pm"); 
        DateTime d2 = new DateTime("4 Nov 21:56pm"); 
        DateTime d3 = new DateTime("4 Nov 12:00am"); 
        DateTime d4 = new DateTime("4 Nov 12:00pm"); 
        DateTime d5 = new DateTime("30 feb 3pm"); 
        DateTime d6 = new DateTime("4 Nov 3pm"); // for testing equals function
        DateTime d7 = new DateTime(); // for testing equals function
        DateTime d8 = new DateTime(); // for testing equals function
        
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
        
        // test isValid function
        assertFalse(d7.isValidDate("sudeihd"));

    }
}

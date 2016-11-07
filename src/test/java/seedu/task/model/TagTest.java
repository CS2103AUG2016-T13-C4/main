package seedu.task.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;

public class TagTest {
    
    @Test
    public void test() throws IllegalValueException {
        // test constructor
        Tag t1  = new Tag("impt");
        Tag t2  = new Tag("WIEURHWIEUHR");
        Tag t3  = null;
        
        // test toString
        assertEquals(t1.toString(), "[impt]");
        assertEquals(t2.toString(), "[WIEURHWIEUHR]");
        
        // test isValid
        assertFalse(t3.isValidTagName("t%%#$@"));
    }
}

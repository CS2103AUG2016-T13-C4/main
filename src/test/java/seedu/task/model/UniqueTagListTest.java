package seedu.task.model;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;

public class UniqueTagListTest {
    
    @Test
    public void test() throws IllegalValueException {
        
        Tag t1 = new Tag("impt");
        Tag t2 = new Tag("urgent");
        Tag t3 = new Tag("school");
        Tag t4 = new Tag("school");
        Tag t5 = new Tag("different");
        
        Collection<Tag> c1 = new ArrayList<Tag>(Arrays.asList(t1, t2, t3, t5));
        Collection<Tag> c2 = new ArrayList<Tag>(Arrays.asList(t1, t2, t3, t5));
        
        Set<Tag> s1 = new HashSet<Tag>(Arrays.asList(t1, t2, t3));
        Set<Tag> s2 = new HashSet<Tag>(Arrays.asList(t1, t2, t4, t5));
        Set<Tag> s3 = new HashSet<Tag>(Arrays.asList(t5));
        Set<Tag> s4 = new HashSet<Tag>(Arrays.asList(t1, t2, t3, t4, t5));
        
        UniqueTagList l1 = new UniqueTagList(t1, t2, t3);
        UniqueTagList lc1 = new UniqueTagList(c1);  // t1, t2, t3, t5
        UniqueTagList lc2 = new UniqueTagList(c1);  // t1, t2, t3, t5
        UniqueTagList ls1 = new UniqueTagList(s1); // t1, t2, t3
        UniqueTagList ls2 = new UniqueTagList(s2); // t1, t2, t4, t5
        UniqueTagList ls3 = new UniqueTagList(s3); // t5
        UniqueTagList ls4 = new UniqueTagList(s4); // t1, t2, t3, t4, t5
        UniqueTagList lcopy = new UniqueTagList(l1);
        
        
        // test copy constructor
        assertEquals(l1, lcopy);
        
        // test set tags function
        l1.setTags(ls3); // sets l1 tags to that of ls3
        assertEquals(l1, ls3);
        
        // test mergeFrom function
        ls1.mergeFrom(ls2); // sets allow duplicate tags
        assertEquals(ls1, ls4);
        
        lc1.mergeFrom(ls2); // collection doesn't allow duplicate tags
        assertEquals(lc1, lc2);
        
        // test add function
        lcopy.add(t5); 
        assertEquals(lcopy, lc2);
        
    }
}

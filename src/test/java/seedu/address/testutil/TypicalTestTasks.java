package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.SuperbToDo;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask walk, dinner;

    public TypicalTestTasks() {
        try {
        	walk =  new TaskBuilder().withName("Walk in the park").withDate("18 Oct 2016").build();
        	dinner =  new TaskBuilder().withName("Dinner with family").withDate("18 Oct 2016 8pm").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(SuperbToDo ab) {

        try {
        	ab.addTask(new Task(walk));
        	ab.addTask(new Task(dinner));
        } catch (UniqueTaskList.DuplicatePersonException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{walk, dinner};
    }

    public SuperbToDo getTypicalSuperbToDo(){
        SuperbToDo ab = new SuperbToDo();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}

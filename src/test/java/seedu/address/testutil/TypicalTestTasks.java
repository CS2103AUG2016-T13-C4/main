package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.*;

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

    public static void loadAddressBookWithSampleData(AddressBook ab) {

        try {
        	ab.addPerson(new Task(walk));
        	ab.addPerson(new Task(dinner));
        } catch (UniqueTaskList.DuplicatePersonException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{walk, dinner};
    }

    public AddressBook getTypicalSuperbToDo(){
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}

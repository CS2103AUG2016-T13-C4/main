package seedu.task.testutil;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.SuperbTodo;
import seedu.task.model.task.*;


/**
 *
 */
public class TypicalTestTasks {

    public static TestTask walk, dinner;

    public TypicalTestTasks() {
        try {
        	walk =  new TaskBuilder().withName("Walk in the park").withDueTime("18 Oct 2016").build();
        	dinner =  new TaskBuilder().withName("Dinner with family").withDueTime("18 Oct 2016 8pm").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadSuperbTodoWithSampleData(SuperbTodo ab) {

        try {
        	ab.addTask(new Task(walk));
        	ab.addTask(new Task(dinner));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{walk, dinner};
    }

    public SuperbTodo getTypicalSuperbToDo(){
        SuperbTodo ab = new SuperbTodo();
        loadSuperbTodoWithSampleData(ab);
        return ab;
    }
}

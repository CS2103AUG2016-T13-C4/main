package seedu.task.testutil;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.SuperbTodo;
import seedu.task.model.TaskTest;
import seedu.task.model.task.*;


/**
 *
 */
public class TypicalTaskTests {

    public static TaskTest walk, dinner;

    public TypicalTaskTests() {
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
        	ab.addTask(new Task((ReadOnlyTask) walk));
        	ab.addTask(new Task((ReadOnlyTask) dinner));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TaskTest[] getTypicalTasks() {
        return new TaskTest[]{walk, dinner};
    }

    public SuperbTodo getTypicalSuperbToDo(){
        SuperbTodo ab = new SuperbTodo();
        loadSuperbTodoWithSampleData(ab);
        return ab;
    }
}

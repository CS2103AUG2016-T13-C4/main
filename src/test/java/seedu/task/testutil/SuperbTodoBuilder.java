package seedu.task.testutil;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.SuperbTodo;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;



/**
 * A utility class to help with building  objects.
 * Example usage: <br>
 *     {@code SuperbTodo ab = new SuperbTodoBuilder().withTask("buy grocery", "do assignment").withTag("impt").build();}
 */
public class SuperbTodoBuilder {

    private SuperbTodo SuperbTodo;

    public SuperbTodoBuilder(SuperbTodo SuperbTodo){
        this.SuperbTodo = SuperbTodo;
    }

    public SuperbTodoBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        SuperbTodo.addTask(task);
        return this;
    }

    public SuperbTodoBuilder withTag(String tagName) throws IllegalValueException {
        SuperbTodo.addTag(new Tag(tagName));
        return this;
    }

    public SuperbTodo build(){
        return SuperbTodo;
    }
}

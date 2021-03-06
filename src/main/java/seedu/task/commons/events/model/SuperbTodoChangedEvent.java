package seedu.task.commons.events.model;

import seedu.task.commons.events.BaseEvent;
import seedu.task.model.ReadOnlySuperbTodo;

/** Indicates the SuperbTodo in the model has changed*/
public class SuperbTodoChangedEvent extends BaseEvent {

    public final ReadOnlySuperbTodo data;

    public SuperbTodoChangedEvent(ReadOnlySuperbTodo data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}

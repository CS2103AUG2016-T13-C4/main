package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlySuperbTodo;

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

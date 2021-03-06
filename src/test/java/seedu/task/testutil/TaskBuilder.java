package seedu.task.testutil;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.TaskTest;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.*;

/**
 *
 */
public class TaskBuilder {

    private TaskTest task;

    public TaskBuilder() {
        this.task = new TaskTest();
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new TaskName(name));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withDateTime(String dateTime) throws IllegalValueException {
        this.task.setDateTime(new DateTime(dateTime));
        return this;
    }

    public TaskBuilder withDueTime(String dueDateTime) throws IllegalValueException {
        this.task.setDueTime(new DueDateTime(dueDateTime)); 
        return this;
    }

    public TaskTest build() {
        return this.task;
    }

}

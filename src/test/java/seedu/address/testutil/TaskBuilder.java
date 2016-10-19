package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.person.*;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
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

    public TaskBuilder withAddress(String address) throws IllegalValueException {
        this.task.setAddress(new Address(address));
        return this;
    }

    public TaskBuilder withDate(String dateTime) throws IllegalValueException {
        this.task.setPhone(new DateTime(dateTime));
        return this;
    }

    public TaskBuilder withDue(String dueDateTime) throws IllegalValueException {
        this.task.setEmail(new DueDateTime(dueDateTime));
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}

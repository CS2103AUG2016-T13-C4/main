package seedu.address.model.task;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.util.Objects;

//@@author A0135763B-reused
/**
 * Represents a Task in SuperbToDo.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private TaskName name;
    private DateTime dueDate;
    private DueDateTime dueTime;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(TaskName name, DateTime dueDate, DueDateTime dueTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, dueDate, dueTime, tags);
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDateTime(), source.getDueTime(), source.getTags());
    }
    
    @Override
    public TaskName getName() {
        return name;
    }

    @Override
    public DateTime getDateTime() {
        return dueDate;
    }

    @Override
    public DueDateTime getDueTime() {
        return dueTime;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, dueDate, dueTime, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
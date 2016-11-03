package seedu.address.testutil;

import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private TaskName name;
    private DateTime dateTime;
    private DueDateTime dueDateTime;    
    private UniqueTagList tags;

    public TestTask() {
        tags = new UniqueTagList();
    }

    public void setName(TaskName name) {
        this.name = name;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDueTime(DueDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    @Override
    public TaskName getName() {
        return name;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public DueDateTime getDueTime() {
        return dueDateTime;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName().fullName + " ");
        sb.append(this.getDateTime().date_value + " ");
        sb.append(this.getDateTime().time_value + " ");
        sb.append(this.getDueTime().date_value + " ");
        sb.append(this.getDueTime().time_value + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }

	@Override
	public boolean isDoneTask() {
		// TODO Auto-generated method stub
		return false;
	}


}

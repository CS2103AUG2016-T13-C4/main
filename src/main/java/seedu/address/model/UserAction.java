package seedu.address.model;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;

import java.util.Objects;

//@@author A0135763B-reused
/**
 * Represents a UserAction in SuperbToDo.
 * Guarantees: details are present and not null, field values are validated.
 */
public class UserAction {

    private String commandWord;
    private Task backUpTask;
    private int index;
    
    
    /**
     * Constructor to create a user action
     */
    public UserAction(String command, int position, Task toSave) {
        assert !CollectionUtil.isAnyNull(command, position, toSave);
        this.commandWord = command;
        this.index = position;
        this.backUpTask = toSave;
    }
    
    /**
     * Overloaded constructor
     */
    public UserAction(String command, Task toSave) {
        assert !CollectionUtil.isAnyNull(command, toSave);
        this.commandWord = command;
        this.index = -1;
        this.backUpTask = toSave;
    }
    
    /**
     * Overloaded constructor
     */
    public UserAction(String command, int position, ReadOnlyTask toSave) {
        assert !CollectionUtil.isAnyNull(command, toSave);
        this.commandWord = command;
        this.index = position;
        this.backUpTask = (Task) toSave;
    }


	public String getCommandWord() {
		return commandWord;
	}


	public Task getBackUpTask() {
		return backUpTask;
	}
	
	public ReadOnlyTask getReadOnlyBackUpTask() {
		return backUpTask;
	}


	public int getIndex() {
		return index;
	}
    
    
    
}

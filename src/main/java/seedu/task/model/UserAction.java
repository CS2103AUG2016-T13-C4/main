package seedu.task.model;

import seedu.task.commons.util.CollectionUtil;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;

//@@author A0135763B
/**
 * Represents a UserAction in SuperbToDo.
 * 
 * Serves as a recorder to store the Action user performed
 */
public class UserAction {

    private String commandWord;
    private Task backUpTask;
    private Task backUpEditTask;
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
    public UserAction(String command, int position, ReadOnlyTask toSave, Task toEdit) {
        assert !CollectionUtil.isAnyNull(command, toSave);
        this.commandWord = command;
        this.index = position;
        this.backUpTask = (Task) toSave;
        this.backUpEditTask = toEdit;
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

	public Task getBackUpEditTask() {
		return backUpEditTask;
	}
    
    
    
}

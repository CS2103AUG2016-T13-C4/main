package seedu.task.logic.commands;

import java.util.List;

import seedu.task.commons.core.Messages;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.LogicManager;
import seedu.task.model.UserAction;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0133945B
/**
 * Mark done the task identified using it's last displayed index from SuperbTodo.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ":Mark done the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_Done_Task_SUCCESS = "Done Task to: %1$s";

    public final int targetIndex;
    public final boolean undo, redo;
    
    private ReadOnlyTask taskToDone;
    private List<ReadOnlyTask> lastShownList;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public DoneCommand(int targetIndex, boolean undo, boolean redo) throws IllegalValueException {
    	this.targetIndex = targetIndex;
    	this.undo = undo;
    	this.redo = redo;
    }


	@Override
    public CommandResult execute() {
    	assert model != null;
    	lastShownList = (undo||redo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        taskToDone = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);

        try {
            model.doneTask(taskToDone);
            saveAction();
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        String formatOutput = String.format(MESSAGE_Done_Task_SUCCESS, taskToDone);        
        return new CommandResult(formatOutput);
    }
	//@@author A0135763B
	/**
     * Overloaded function for the purpose of undo Command
     * 
     * Allows undo command to overwrite the success and failure messages

     */
    public CommandResult execute(String Message, String Error) {
    	assert model != null;
    	lastShownList = (undo||redo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Error);
        }
        taskToDone = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);

        try {
            model.doneTask(taskToDone);
            saveAction();
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        	return new CommandResult(Error);
        }
      
        return new CommandResult(Message);
    }
    
    /**
     * function to save user's action into the UndoStorageManager
     * 
     * Determine if an action should be saved and if so, save it as an UserAction Obejct
     */
	private void saveAction() {
		if (!undo) {
		    LogicManager.actionRecorder.recordAction(
		    		new UserAction(DoneCommand.COMMAND_WORD, 
		    				UniqueTaskList.getInternalList().indexOf(taskToDone), 
		    				taskToDone)
		    );
		}
	}

}

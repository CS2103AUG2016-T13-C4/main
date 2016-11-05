package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.LogicManager;
import seedu.address.model.UserAction;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.storage.UndoManagerStorage;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0135763B-reused
/**
 * Deletes a task identified using it's last displayed index from SuperbTodo.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "remove";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Remove the task identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Removed Task: %1$s";

    public final int targetIndex;
    public final boolean undo;
    private ReadOnlyTask taskToDelete;
    private List<ReadOnlyTask> lastShownList;

    
    public DeleteCommand(int targetIndex, boolean undo) {
        this.targetIndex = targetIndex;
        this.undo = undo;
    }


    @Override
    public CommandResult execute() {
    	
    	lastShownList = (undo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();
    	
    	if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    	
    	taskToDelete = (undo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);
    	
        try {
        	int getPosition =  UniqueTaskList.getInternalList().indexOf(taskToDelete);
            model.deleteTask(taskToDelete);
            LogicManager.actionRecorder.recordAction(new UserAction(DeleteCommand.COMMAND_WORD, getPosition, taskToDelete));
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        //@@author A0135763B-reused
        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

}

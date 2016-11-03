package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.LogicManager;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.storage.UndoManagerStorage;

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
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public DoneCommand(int targetIndex) throws IllegalValueException {
    	this.targetIndex = targetIndex;
    }


	@Override
    public CommandResult execute() {
    	assert model != null;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToDone = lastShownList.get(targetIndex - 1);

        try {
            model.doneTask(taskToDone);
            LogicManager.theOne.recorder("done", UniqueTaskList.getInternalList().indexOf(taskToDone), (Task)taskToDone);
            System.out.println("done recorded");
            LogicManager.theOne.undoUpdate(LogicManager.theOne);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        String formatOutput = String.format(MESSAGE_Done_Task_SUCCESS, taskToDone);        
        return new CommandResult(formatOutput);
    }

}

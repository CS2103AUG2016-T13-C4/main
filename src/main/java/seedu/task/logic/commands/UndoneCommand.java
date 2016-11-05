package seedu.task.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.LogicManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.task.storage.UndoManagerStorage;

//@@author A0133945B
/**
 * Mark undone the task identified using it's last displayed index from SuperbTodo.
 */
public class UndoneCommand extends Command {

    public static final String COMMAND_WORD = "undone";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ":Mark undone the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_Done_Task_SUCCESS = "unDone Task to: %1$s";

    public final int targetIndex;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public UndoneCommand(int targetIndex) throws IllegalValueException {
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

        ReadOnlyTask taskToUndone = lastShownList.get(targetIndex - 1);

        try {
            model.undoneTask(taskToUndone);
//            LogicManager.theOne.recorder("undone", UniqueTaskList.getInternalList().indexOf(taskToUndone), (Task)taskToUndone);
//            System.out.println("undone recorded");
//            LogicManager.theOne.undoUpdate(LogicManager.theOne);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        String formatOutput = String.format(MESSAGE_Done_Task_SUCCESS, taskToUndone);        
        return new CommandResult(formatOutput);
    }

}

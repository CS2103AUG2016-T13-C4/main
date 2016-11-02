package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.DateTime;
import seedu.address.model.task.DueDateTime;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.storage.UndoManagerStorage;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskName;
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
    ReadOnlyTask taskToDelete;
    UndoManagerStorage undoM = new UndoManagerStorage();

    
    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;  
    }


    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        } else {
            taskToDelete = lastShownList.get(targetIndex - 1);
        }

       try {
            model.deleteTask(taskToDelete);
            undoM.recorder("remove", (Task)taskToDelete);
            System.out.println("remove recorded");
            undoM.deleteUpdate(undoM);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
               
        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));

    }
    
    
}

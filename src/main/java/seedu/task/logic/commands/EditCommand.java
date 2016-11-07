package seedu.task.logic.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.task.model.UserAction;
import seedu.task.commons.core.Messages;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.LogicManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0135763B
/**
 * Edit a task identified using it's last displayed index from SuperbTodo.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edit the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 by 28 October 2016 on 3pm t/Important";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Task to: %1$s";

    public final int targetIndex;
    private final Task toEdit;
    public final boolean undo;
    public final boolean redo;
    
    private ReadOnlyTask taskToEdit;
    private List<ReadOnlyTask> lastShownList;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String dateTimeParam, Set<String> tags, boolean undo, boolean redo)
            throws IllegalValueException {
    	this.targetIndex = targetIndex;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toEdit = AddCommand.handleAddType(name, dateTimeParam, tagSet);
        this.undo = undo;
        this.redo = redo;
    }
    
    /**
     * Overload constructor for already available Edit task
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(Task task, int position, boolean undo, boolean redo) throws IllegalValueException {
    	this.toEdit = task;
    	this.targetIndex = position;
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
        taskToEdit = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);

        try {
            model.editTask(taskToEdit, toEdit);
            saveAction();
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        // @@author A0135763B
        String formatOutput = String.format(MESSAGE_EDIT_PERSON_SUCCESS, toEdit);        
        return new CommandResult(formatOutput);
    }

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
        taskToEdit = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);

        try {
            model.editTask(taskToEdit, toEdit);
            saveAction();
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        	return new CommandResult(Error);
        }

        // @@author A0135763B       
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
					new UserAction(EditCommand.COMMAND_WORD, 
							UniqueTaskList.getInternalList().indexOf(toEdit), 
							taskToEdit,
							toEdit)
			);
		}
	}

}

package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.LogicManager;
import seedu.address.model.UserAction;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.storage.UndoManagerStorage;

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
    
    private ReadOnlyTask taskToEdit;
    private List<ReadOnlyTask> lastShownList;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String dateTimeParam, Set<String> tags, boolean undo)
            throws IllegalValueException {
    	this.targetIndex = targetIndex;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toEdit = AddCommand.handleAddType(name, dateTimeParam, tagSet);
        this.undo = undo;
    }
    
    /**
     * Overload constructor for already available Edit task
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(Task task, int position, boolean undo) throws IllegalValueException {
    	this.toEdit = task;
    	this.targetIndex = position;
    	this.undo = undo;
    }
    

    @Override
    public CommandResult execute() {
    	assert model != null;
    	
    	lastShownList = (undo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        taskToEdit = (undo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);

        try {
            model.editTask(taskToEdit, toEdit);
            LogicManager.actionRecorder.recordAction(new UserAction(EditCommand.COMMAND_WORD, UniqueTaskList.getInternalList().indexOf(toEdit), taskToEdit));
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        // @@author A0135763B
        String formatOutput = String.format(MESSAGE_EDIT_PERSON_SUCCESS, toEdit);        
        return new CommandResult(formatOutput);
    }

}

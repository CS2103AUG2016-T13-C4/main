package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

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
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String dateTimeParam, Set<String> tags)
            throws IllegalValueException {
    	this.targetIndex = targetIndex;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toEdit = AddCommand.handleAddType(name, dateTimeParam, tagSet);
    }
    

    @Override
    public CommandResult execute() {
    	assert model != null;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask personToEdit = lastShownList.get(targetIndex - 1);

        try {
            model.editTask(personToEdit, toEdit);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
        
        String formatOutput = String.format(MESSAGE_EDIT_PERSON_SUCCESS, toEdit);
        
        return new CommandResult(formatOutput);
    }

}

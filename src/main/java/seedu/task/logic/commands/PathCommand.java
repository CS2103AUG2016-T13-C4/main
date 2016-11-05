package seedu.task.logic.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.task.MainApp;
import seedu.task.commons.core.Config;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.CommandResult;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.task.storage.SuperbTodoIO;
import seedu.task.storage.UndoManagerStorage;

//@@author A0133945B
/**
 * Change the save path of the storage
 */
public class PathCommand extends Command {

    public static final String COMMAND_WORD = "path";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ":Change the save path of the storage.\n"
            + "Parameters: Newpath (must be a valid path directory)\n"
            + "Example: " + COMMAND_WORD + " data";

    public static final String MESSAGE_Path_Task_SUCCESS = "Change path: %1$s";
    

    private final String newpath;

    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalV alueException if any of the raw values are invalid
     */
    public PathCommand(String newpath) throws IllegalValueException {
    	this.newpath = newpath;
    }


	@Override
    public CommandResult execute() {
    	assert model != null;
        model.changePath(newpath);
        String formatOutput = String.format(MESSAGE_Path_Task_SUCCESS, newpath);        
        return new CommandResult(formatOutput);
    }



}

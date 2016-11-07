package seedu.task.logic.commands;

import seedu.task.model.UserAction;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.LogicManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@@author A0135763B
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Adds a task to the SuperbToDo.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to SuperbTodo. "
            + "Format: add <task description> at/by <time> on <date> [t/TAG]\n"
            + "Example: " + COMMAND_WORD
            + " Finish homework by 23:59 on 11 Oct t/school t/important";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_ERROR_DATE = "SuperbToDo is unable to identify event period. Please specify only 2 dates as period.";
    public static final String MESSAGE_ERROR_PERIOD = "SuperbToDo is unable your specified period. Please check if you have entered a valid date and time.";
    public static final String MESSAGE_ERROR_CHRONO = "SuperbToDo detected a chronological error. " + 
    													"Please check if both start and end period are chronologically correct";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the SuperbToDo";

    private final static boolean Undone = false;
    private final Task toAdd;
    private final int index;
    private final boolean undo;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String dateTimeParam, Set<String> tags, boolean undo)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toAdd = handleAddType(name, dateTimeParam, tagSet);
        this.index = -1;
        this.undo = undo;
    }
    
    //@@author A0135763B
    /**
     * Overloaded constructor to add command if task is already available
     * 
     * Used in undo command
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(Task task, int position, boolean undo) throws IllegalValueException {
        this.toAdd = task;
        this.index = position;
        this.undo = undo;
    }
    
    /**
     * Function to create a task object.
     * Takes in raw values and determine the Task type: Floating, event or normal task
     *
     * This function is also used by edit to create a Task object
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public static Task handleAddType(String name, String dateTimeParam, final Set<Tag> tagSet) throws IllegalValueException {
		if (dateTimeParam.equals("")) {
        	// Floating task
        	return createFloatingTask(name, tagSet);
        } else {
			return checkDateCount(name, dateTimeParam, tagSet);
        }
	}
    
    /**
     * Function to create a task object. depending on the number of date periods found
     * Used to determine if a task is timed or an event.
     *
     * Return: Task object 
     */
	public static Task checkDateCount(String name, String dateTimeParam, final Set<Tag> tagSet)
			throws IllegalValueException {
		List<Date> dateList = retrieveDate(dateTimeParam);
		
		if (dateList.size() > 2 || dateList.size() == 0) {
			throw new IllegalValueException(MESSAGE_ERROR_DATE);
		} else if (dateList.size() == 1) {
			return createNormalTask(name, dateTimeParam, tagSet);
		} else {
			return createEvent(name, dateTimeParam, tagSet, dateList);
		}
	}
	
	/**
     * Validation function to check if user have given 2 valid periods in terms of chronological validity
     * Takes in list of 2 dates.
     *
     * Return: boolean representing if valid or not

     */
	public static boolean validateChronoOrder(List<Date> dateList) {
		if (dateList.size() != 0 && dateList.size() <= 2) {
			return (dateList.get(0).compareTo(dateList.get(1)) < 0);
		} else {
			return false;
		}
	}
	
	/**
     * Create a task representing an event.
     * Takes in raw values and return a task object.
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
	public static Task createEvent(String name, String dateTimeParam, final Set<Tag> tagSet, List<Date> dateList)
			throws IllegalValueException {
		if (validateChronoOrder(dateList)) {
			return new Task(
			        new TaskName(name),
			        new DateTime(dateTimeParam),
			        new DueDateTime(dateTimeParam),
			        new UniqueTagList(tagSet),
			        Undone
			);
		} else {
			throw new IllegalValueException(MESSAGE_ERROR_CHRONO);
		}
	}
	
	/**
     * Create a task representing a standard task with just a end date time.
     * Takes in raw values and return a task object.
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
	public static Task createNormalTask(String name, String dateTimeParam, final Set<Tag> tagSet)
			throws IllegalValueException {
		return new Task(
				new TaskName(name),
		        new DateTime(),
		        new DueDateTime(dateTimeParam),
		        new UniqueTagList(tagSet),
		        Undone

		);
	}
	
	/**
     * Create a task representing a floating task with on date time.
     * Takes in raw values and return a task object.
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
	public static Task createFloatingTask(String name, final Set<Tag> tagSet) throws IllegalValueException {
		return new Task(
				 new TaskName(name),
		         new DateTime(),
		         new DueDateTime(),
		         new UniqueTagList(tagSet),
		         Undone
		);
	}
	
	/**
     * Function to parse a String representing a time period into natty parser for handling
     *
     * returns: A list of dates (If a date is found) or empty list (If unable to detect a date value)
     */
	private static List<Date> retrieveDate(String period)  throws IllegalValueException {
		assert period != null;
        period = period.trim();
        Parser parser = new Parser();
    	List<DateGroup> dateParser = parser.parse(period);
    	if (period.equals(dateParser.get(0).getText())) {
    		return dateParser.get(0).getDates();
    	} else {
    		throw new IllegalValueException(MESSAGE_ERROR_PERIOD);
    	}
	}
    
    //@@author A0135763B-reused
	@Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd, index);
            saveAction();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
    }
	
	/**
     * Overloaded function for the purpose of undo Command
     * 
     * Allows undo command to overwrite the success and failure messages
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public CommandResult execute(String Message, String Error) {
        assert model != null;
        try {
            model.addTask(toAdd, index);
            saveAction();
            return new CommandResult(Message);
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(Error);
        }
    }

	private void saveAction() {
		if (!undo) {
			LogicManager.actionRecorder.recordAction(
					new UserAction(AddCommand.COMMAND_WORD, 
							UniqueTaskList.getInternalList().indexOf(toAdd), 
							toAdd)
			);
		}
	}

}

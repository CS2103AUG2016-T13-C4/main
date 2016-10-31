package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

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

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String dateTimeParam, Set<String> tags)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toAdd = handleAddType(name, dateTimeParam, tagSet);
    }
    
    //@@author A0135763B
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

	private static Task checkDateCount(String name, String dateTimeParam, final Set<Tag> tagSet)
			throws IllegalValueException {
		List<Date> dateList = retrieveDate(dateTimeParam);
		
		if (dateList.size() > 2 || dateList.size() == 0) {
			throw new IllegalValueException(MESSAGE_ERROR_DATE);
		} else if (dateList.size() == 1) {
			// Normal task
			return createNormalTask(name, dateTimeParam, tagSet);
		} else {
			// Event
			return validateChronoOrder(name, dateTimeParam, tagSet, dateList);
		}
	}

	private static Task validateChronoOrder(String name, String dateTimeParam, final Set<Tag> tagSet,
			List<Date> dateList) throws IllegalValueException {
		if (dateList.get(0).compareTo(dateList.get(1)) < 0) {
			return createEvent(name, dateTimeParam, tagSet);
		} else {
			throw new IllegalValueException(MESSAGE_ERROR_CHRONO);
		}
	}

	private static Task createEvent(String name, String dateTimeParam, final Set<Tag> tagSet)
			throws IllegalValueException {
		return new Task(
		        new TaskName(name),
		        new DateTime(dateTimeParam),
		        new DueDateTime(dateTimeParam),
		        new UniqueTagList(tagSet)
		);
	}

	private static Task createNormalTask(String name, String dateTimeParam, final Set<Tag> tagSet)
			throws IllegalValueException {
		return new Task(
				new TaskName(name),
		        new DateTime(),
		        new DueDateTime(dateTimeParam),
		        new UniqueTagList(tagSet)
		);
	}

	private static Task createFloatingTask(String name, final Set<Tag> tagSet) throws IllegalValueException {
		return new Task(
				 new TaskName(name),
		         new DateTime(),
		         new DueDateTime(),
		         new UniqueTagList(tagSet)
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
            model.addTask(toAdd);
          //@@author A0113992B
            /* Doesnt work yet
            commandRecorder.addRecorder("add", toAdd, toAdd.getName(), toAdd.getDateTime(), toAdd.getDueTime(), 
                    toAdd.getTags());
            undoCommand.add(commandRecorder);
            */
          //@@author A0135763B
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

    }

}

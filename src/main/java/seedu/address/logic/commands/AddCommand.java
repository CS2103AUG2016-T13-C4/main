package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to SuperbTodo. "
    		//add <task description> at/by <time> on <date>
            + "Format: <task description> at/by <time> on <date> [t/TAG]\n"
            + "Example: " + COMMAND_WORD
            + " Finish homework by 23:59 on 11 Oct t/school t/important";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_ERROR_DATE = "SuperbToDo is unable to identify event period. Please specify only 2 dates as period.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This task already exists in the address book";

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

	private Task handleAddType(String name, String dateTimeParam, final Set<Tag> tagSet) throws IllegalValueException {
		if (dateTimeParam.equals("")) {
        	// floating task
        	 return new Task(
        			 new TaskName(name),
                     new DateTime(),
                     new DueDateTime(),
                     new Address(),
                     new UniqueTagList(tagSet)
            );
        } else {
        	List dateList = retrieveDate(dateTimeParam);
            
            if (dateList.size() > 2 || dateList.size() == 0) {
            	throw new IllegalValueException(MESSAGE_ERROR_DATE);
            } else if (dateList.size() == 1) {
            	return new Task(
            			new TaskName(name),
    	                new DateTime(),
    	                new DueDateTime(dateTimeParam),
    	                new Address(),
    	                new UniqueTagList(tagSet)
    	        );
            } else {
            	return new Task(
    	                new TaskName(name),
    	                new DateTime(dateList.get(0).toString()),
    	                new DueDateTime(dateList.get(1).toString()),
    	                new Address(),
    	                new UniqueTagList(tagSet)
    	        );
            }
        }
	}

    private List retrieveDate(String period) {
		assert period != null;
        period = period.trim();
        Parser parser = new Parser();
    	List<DateGroup> dateParser = parser.parse(period);
    	return dateParser.get(0).getDates();
	}
    
    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addPerson(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniquePersonList.DuplicatePersonException e) {
            return new CommandResult(MESSAGE_DUPLICATE_PERSON);
        }

    }

}

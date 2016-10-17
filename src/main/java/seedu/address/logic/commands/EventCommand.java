package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import com.joestelmach.natty.*;

/**
 * Adds a person to the address book.
 */
public class EventCommand extends Command {

    public static final String COMMAND_WORD = "event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to SuperbTodo. "
    		//add <task description> at/by <time> on <date>
            + "Format: <task description> from <time and date> to <time and date> [t/TAG]\n"
            + "Example: " + COMMAND_WORD
            + " Jane's birthday party from 21 Dec 2016 3pm to 4pm t/important";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_ERROR_DATE = "SuperbToDo is unable to identify event period. Please specify only 2 dates as period.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This task already exists in the address book";

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EventCommand(String name, String period, Set<String> tags)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        List dateList = retrieveDate(period);
        
        if (dateList.size() > 2 || dateList.size() == 0) {
        	throw new IllegalValueException(MESSAGE_ERROR_DATE);
        }
        
        this.toAdd = new Task(
                new TaskName(name),
                new DateTime(dateList.get(0).toString()),
                new DueDateTime(dateList.get(1).toString()),
                new Address(),
                new UniqueTagList(tagSet)
        );
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

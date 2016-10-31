package seedu.address.logic.commands;


import java.util.Date;
import java.util.List;

//@@author A0135763B
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;


import seedu.address.commons.exceptions.IllegalValueException;

//@@author A0135763B-reused
/**
 * Lists all tasks in SuperbTodo to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    
    public static final String MESSAGE_UNEXPECTED = "SuperbTodo have encountered an unexpected parser error!";
    public static final String MESSAGE_CRITERIA_FAIL = "SuperbTodo cannot understand your listing criteria, try another criteria.";
    
    public static final String LIST_TYPE_ALL = "ALL";
    public static final String LIST_TYPE_TIMED = "TIMED";
    public static final String LIST_TYPE_UNTIMED = "UNTIMED";
    public static final String LIST_TYPE_EVENT = "EVENT";
    public static final String LIST_TYPE_TODAY = "TODAY";
    public static final String LIST_TYPE_TOMORROW = "TOMORROW";
    
    public static final String NATTY_PERIOD_TODAY = "0000hrs to 2359hrs";
    public static final String NATTY_PERIOD_TOMORROW = "tomorrow 0000hrs to 2359hrs";

    public static final int MAP_LIST_TYPE_ALL = 0;
    public static final int MAP_LIST_TYPE_TIMED = 1;
    public static final int MAP_LIST_TYPE_UNTIMED = 2;
    public static final int MAP_LIST_TYPE_EVENT = 3;
    public static final int MAP_LIST_TYPE_TODAY = 4;
    public static final int MAP_LIST_TYPE_TOMORROW = 5;
    public static final int MAP_LIST_TYPE_DATE = 6;
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: List (all/timed/untimed/overdue/today/tomorrow/#keyword/<date>)\n"
            + "Example: " + COMMAND_WORD + " all";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    public static final String MESSAGE_FAIL = "Unable to list tasks";

    private final int listType;
    private Date start, end;
    
    public ListCommand(String args) throws IllegalValueException {
    	Parser parser = new Parser();
    	List<DateGroup> dateParser;
    	
    	args = args.toUpperCase().trim();
    	
    	if (args.equals(LIST_TYPE_ALL)) {
    		this.listType = MAP_LIST_TYPE_ALL;
    	} else if (args.equals(LIST_TYPE_TIMED)) {
    		this.listType = MAP_LIST_TYPE_TIMED;
    	} else if (args.equals(LIST_TYPE_UNTIMED)) {
    		this.listType = MAP_LIST_TYPE_UNTIMED;
    	} else if (args.equals(LIST_TYPE_EVENT)) {
    		this.listType = MAP_LIST_TYPE_EVENT;
    	} else if (args.equals(LIST_TYPE_TODAY)) {
    		this.listType = MAP_LIST_TYPE_TODAY;
    		dateParser = parser.parse(NATTY_PERIOD_TODAY);
        	if (NATTY_PERIOD_TODAY.equals(dateParser.get(0).getText())) {
        		this.start = dateParser.get(0).getDates().get(0);
        		this.end = dateParser.get(0).getDates().get(1);
        	} else {
        		throw new IllegalValueException(MESSAGE_UNEXPECTED);
        	}
    	} else if (args.equals(LIST_TYPE_TOMORROW)) {
    		this.listType = MAP_LIST_TYPE_TOMORROW;
    		dateParser = parser.parse(NATTY_PERIOD_TOMORROW);
        	if (NATTY_PERIOD_TOMORROW.equals(dateParser.get(0).getText())) {
        		this.start = dateParser.get(0).getDates().get(0);
        		this.end = dateParser.get(0).getDates().get(1);
        	} else {
        		throw new IllegalValueException(MESSAGE_UNEXPECTED);
        	}
    	} else {
    		this.listType = MAP_LIST_TYPE_DATE;
    		dateParser = parser.parse(args);
        	if (dateParser.size() > 0 && dateParser.get(0).getDates().size() > 0 && dateParser.get(0).getDates().size() < 3) {
        		this.start = dateParser.get(0).getDates().get(0);
        		this.end = (dateParser.get(0).getDates().size() < 2) ? null : dateParser.get(0).getDates().get(1);
        	} else {
        		throw new IllegalValueException(MESSAGE_CRITERIA_FAIL);
        	}
    	}
 
    }

    @Override
    public CommandResult execute() {
    	if (listType == MAP_LIST_TYPE_ALL) {
    		model.updateFilteredListToShowAll();
    	} else if (listType == MAP_LIST_TYPE_TIMED) {
    		model.updateFilteredListToShowByType(LIST_TYPE_TIMED);
    	} else if (listType == MAP_LIST_TYPE_UNTIMED) {
    		model.updateFilteredListToShowByType(LIST_TYPE_UNTIMED);
    	} else if (listType == MAP_LIST_TYPE_EVENT) {
    		model.updateFilteredListToShowByType(LIST_TYPE_EVENT);
    	} else if (listType == MAP_LIST_TYPE_TODAY || listType == MAP_LIST_TYPE_TOMORROW) {
    		model.updateFilteredListToShowByTime(this.start, this.end, 0);
    	} else if (listType == MAP_LIST_TYPE_DATE) {
    		model.updateFilteredListToShowByTime(this.start, this.end, 1);
    	} else {
    		return new CommandResult(MESSAGE_FAIL);
    	}
    	
    	return new CommandResult(MESSAGE_SUCCESS);
    }
}

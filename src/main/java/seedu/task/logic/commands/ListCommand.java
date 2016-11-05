package seedu.task.logic.commands;


import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//@@author A0135763B
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import seedu.task.commons.exceptions.IllegalValueException;

//@@author A0135763B-reused
/**
 * Lists all tasks in SuperbTodo to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    
    public static final String LIST_TYPE_ALL = "ALL";
    public static final String LIST_TYPE_TIMED = "TIMED";
    public static final String LIST_TYPE_UNTIMED = "UNTIMED";
    public static final String LIST_TYPE_EVENT = "EVENT";
    public static final String LIST_TYPE_TODAY = "TODAY";
    public static final String LIST_TYPE_TOMORROW = "TOMORROW";
    public static final String LIST_TYPE_OVERDUE = "OVERDUE";
    public static final String LIST_TYPE_DONE = "DONE";
    public static final String LIST_TYPE_UNDONE = "UNDONE";
    
    public static final String NATTY_PERIOD_TODAY = "0000hrs to 2359hrs";
    public static final String NATTY_PERIOD_TOMORROW = "tomorrow 0000hrs to 2359hrs";

    public static final int MAP_LIST_TYPE_ALL = 0;
    public static final int MAP_LIST_TYPE_TIMED = 1;
    public static final int MAP_LIST_TYPE_UNTIMED = 2;
    public static final int MAP_LIST_TYPE_EVENT = 3;
    public static final int MAP_LIST_TYPE_TODAY = 4;
    public static final int MAP_LIST_TYPE_TOMORROW = 5;
    public static final int MAP_LIST_TYPE_DATE = 6;
    public static final int MAP_LIST_TYPE_OVERDUE = 7;
    public static final int MAP_LIST_TYPE_DONE = 8;
    public static final int MAP_LIST_TYPE_UNDONE = 9;
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: List (all/timed/untimed/overdue/today/tomorrow/#keyword/<date>)\n"
            + "Example: " + COMMAND_WORD + " all";

    public static final String MESSAGE_SUCCESS_ALL = "Listed all tasks and events";
    public static final String MESSAGE_SUCCESS_TIMED = "Listed all timed task";
    public static final String MESSAGE_SUCCESS_UNTIMED = "Listed all untimed task";
    public static final String MESSAGE_SUCCESS_EVENT = "Listed all events";
    public static final String MESSAGE_SUCCESS_TODAY = "Listed all task for today!";
    public static final String MESSAGE_SUCCESS_TOMORROW = "Listed all task for tomorrow!";
    public static final String MESSAGE_SUCCESS_OVERDUE = "Listed tasks that are overdue!";
    public static final String MESSAGE_SUCCESS_DONE = "Listed tasks that are completed";
    public static final String MESSAGE_SUCCESS_UNDONE = "Listed tasks that are uncompleted";
    public static final String MESSAGE_FAIL = "Unable to list tasks";
    public static final String MESSAGE_UNEXPECTED = "SuperbTodo have encountered an unexpected parser error!";
    public static final String MESSAGE_CRITERIA_FAIL = "SuperbTodo cannot understand your listing criteria, try another criteria.";

    private int listType;
    private Date start, end;
    
    /**
     * Function to request SuperbTodo to list related tasks.
     * Takes in raw value and determine if the String value is valid.
     * 
     * If the String is valid, prepare it.
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public ListCommand(String args) throws IllegalValueException {
    	args = args.toUpperCase().trim();
    	
    	listTypeMapping(args);
    }
    
    /**
     * Helper function of List Command.
     * Takes a raw string and decide if the string is a valid filter for mapping.
     *
     * Purpose: To perform mapping of string arg to a filter type of the list. The mapping will then be used to determine
     *          the execution type.
     *
     * If none of the preset filter type matches the String, the function will default the string as a date period.
     * If the date parser is unable to detect a date period from the default string, this function will throw an exception.
     * 
     * @throws IllegalValueException if default filter is unable to identify a mapping
     */
	private void listTypeMapping(String args) throws IllegalValueException {
		Parser parser = new Parser();
    	
    	if (args.equals(LIST_TYPE_ALL)) {
    		this.listType = MAP_LIST_TYPE_ALL;
    	} else if (args.equals(LIST_TYPE_TIMED)) {
    		this.listType = MAP_LIST_TYPE_TIMED;
    	} else if (args.equals(LIST_TYPE_UNTIMED)) {
    		this.listType = MAP_LIST_TYPE_UNTIMED;
    	} else if (args.equals(LIST_TYPE_DONE)) {
    		this.listType = MAP_LIST_TYPE_DONE;
    	} else if (args.equals(LIST_TYPE_UNDONE)) {
    		this.listType = MAP_LIST_TYPE_UNDONE;
    	} else if (args.equals(LIST_TYPE_EVENT)) {
    		this.listType = MAP_LIST_TYPE_EVENT;
    	} else if (args.equals(LIST_TYPE_TODAY)) {
    		this.listType = MAP_LIST_TYPE_TODAY;
    		defineDateToday(parser);
    	} else if (args.equals(LIST_TYPE_TOMORROW)) {
    		this.listType = MAP_LIST_TYPE_TOMORROW;
    		defineDateTomorrow(parser);
    	} else if (args.equals(LIST_TYPE_OVERDUE)) {
    		this.listType = MAP_LIST_TYPE_OVERDUE;
    		defineDateOverdue();
    	} else {
    		this.listType = MAP_LIST_TYPE_DATE;
    		defineDateDefault(args, parser);
    	}
	}
	
	/**
     * Preparation function to prepare and set the parameters for time = overdue filter
	 *
     */
	private void defineDateOverdue() {
		this.start = new Date();
		this.end = new Date();
	}
	
	/**
     * Preparation function to prepare and set the parameters for time = today filter
	 *
	 * @throws IllegalValueException if there is an unexpected error of the date parser
     */
	private void defineDateToday(Parser parser) throws IllegalValueException {
		List<DateGroup> dateParser;
		dateParser = parser.parse(NATTY_PERIOD_TODAY);
		if (NATTY_PERIOD_TODAY.equals(dateParser.get(0).getText())) {
			this.start = dateParser.get(0).getDates().get(0);
			this.end = dateParser.get(0).getDates().get(1);
		} else {
			throw new IllegalValueException(MESSAGE_UNEXPECTED);
		}
	}
	
	/**
     * Preparation function to prepare and set the parameters for time = tomorrow filter
	 *
	 * @throws IllegalValueException if there is an unexpected error of the date parser
     */
	private void defineDateTomorrow(Parser parser) throws IllegalValueException {
		List<DateGroup> dateParser;
		dateParser = parser.parse(NATTY_PERIOD_TOMORROW);
		if (NATTY_PERIOD_TOMORROW.equals(dateParser.get(0).getText())) {
			this.start = dateParser.get(0).getDates().get(0);
			this.end = dateParser.get(0).getDates().get(1);
		} else {
			throw new IllegalValueException(MESSAGE_UNEXPECTED);
		}
	}
	
	/**
     * Default preparation function to prepare and set the relevant parameters.
     * 
     * Current Default: Treat the string arg as a date period.
     * Parses the string into the date parser and check for number of date periods found
     * 
     * If date period count is 0 or > 2, consider the arg as invalid
     * If date period count is 1, consider it as a specific date and convert it into a full day duration
     * If date period count is 2, consider it as a time period filter.
	 *
	 * @throws IllegalValueException if there is an unexpected error of the date parser or
	 *         if the default filter fails to recognise the string arg.
     */
	private void defineDateDefault(String args, Parser parser) throws IllegalValueException {
		List<DateGroup> dateParser;
		dateParser = parser.parse(args);
		if (dateParser.size() > 0 && dateParser.get(0).getDates().size() > 0 && dateParser.get(0).getDates().size() < 3) {
			if (dateParser.get(0).getDates().size() == 1) {
				Date[] prepareDate = convertDatetoFullRange(dateParser.get(0).getDates().get(0));
				this.start = prepareDate[0];
				this.end = prepareDate[1];
			} else if (dateParser.get(0).getDates().size() == 2) {
				this.start = dateParser.get(0).getDates().get(0);
				this.end = dateParser.get(0).getDates().get(1);
			} else {
				throw new IllegalValueException(MESSAGE_UNEXPECTED);
			}
		} else {
			throw new IllegalValueException(MESSAGE_CRITERIA_FAIL);
		}
	}
    
	/**
     * Helper function for defineDateDefault to convert a date into a full day's duration
     * 
     */
    private Date[] convertDatetoFullRange(Date d) {
        Calendar c = new GregorianCalendar();
        Date[] output = new Date[2];
        
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        output[0] = c.getTime();
        
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        output[1] = c.getTime();

        return output;
    }
   
    @Override
    public CommandResult execute() {
    	String userMessage = "";
    	
    	if (listType == MAP_LIST_TYPE_ALL) {
    		userMessage = MESSAGE_SUCCESS_ALL;
    		model.updateFilteredListToShowAll();
    	} else if (listType == MAP_LIST_TYPE_TIMED) {
    		userMessage = MESSAGE_SUCCESS_TIMED;
    		model.updateFilteredListToShowByType(LIST_TYPE_TIMED);
    	} else if (listType == MAP_LIST_TYPE_DONE) {
    		userMessage = MESSAGE_SUCCESS_DONE;
    		model.updateFilteredListToShowByType(LIST_TYPE_DONE);
    	} else if (listType == MAP_LIST_TYPE_UNDONE) {
    		userMessage = MESSAGE_SUCCESS_UNDONE;
    		model.updateFilteredListToShowByType(LIST_TYPE_UNDONE);
    	} else if (listType == MAP_LIST_TYPE_UNTIMED) {
    		userMessage = MESSAGE_SUCCESS_UNTIMED;
    		model.updateFilteredListToShowByType(LIST_TYPE_UNTIMED);
    	} else if (listType == MAP_LIST_TYPE_EVENT) {
    		userMessage = MESSAGE_SUCCESS_EVENT;
    		model.updateFilteredListToShowByType(LIST_TYPE_EVENT);
    	} else if (listType == MAP_LIST_TYPE_TODAY || listType == MAP_LIST_TYPE_TOMORROW) {
    		userMessage = (listType == MAP_LIST_TYPE_TODAY) ? MESSAGE_SUCCESS_TODAY : MESSAGE_SUCCESS_TOMORROW;
    		model.updateFilteredListToShowByTime(this.start, this.end, MAP_LIST_TYPE_TODAY);
    	} else if (listType == MAP_LIST_TYPE_DATE) {
    		userMessage = MESSAGE_SUCCESS_ALL;
    		model.updateFilteredListToShowByTime(this.start, this.end, MAP_LIST_TYPE_DATE);
    	} else if (listType == MAP_LIST_TYPE_OVERDUE) {
    		userMessage = MESSAGE_SUCCESS_OVERDUE;
    		model.updateFilteredListToShowByTime(this.start, this.end, MAP_LIST_TYPE_OVERDUE);
    	} else {
    		return new CommandResult(MESSAGE_FAIL);
    	}
    	
    	return new CommandResult(userMessage);
    }

	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}
}

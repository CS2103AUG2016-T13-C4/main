# A0135763Breused
###### /java/seedu/task/logic/commands/AddCommand.java
``` java
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
    
    /**
     * function to save user's action into the UndoStorageManager
     * 
     * Determine if an action should be saved and if so, save it as an UserAction Obejct
     */
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
```
###### /java/seedu/task/logic/commands/ClearCommand.java
``` java
/**
 * Clears SuperbTodo.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All tasks have been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(SuperbTodo.getEmptySuperbTodo());
        return new CommandResult(MESSAGE_SUCCESS);
    }


	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}
}
```
###### /java/seedu/task/logic/commands/Command.java
``` java
/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
```
###### /java/seedu/task/logic/commands/Command.java
``` java
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();
    
    public abstract CommandResult execute(String feedbackSuccess, String feedbackUnsucess);

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }

}
```
###### /java/seedu/task/logic/commands/CommandResult.java
``` java
/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    public final String feedbackToUser;

    public CommandResult(String feedbackToUser) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
    }

}
```
###### /java/seedu/task/logic/commands/DeleteCommand.java
``` java
/**
 * Deletes a task identified using it's last displayed index from SuperbTodo.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "remove";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Remove the task identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Removed Task: %1$s";

    public final int targetIndex;
    public final boolean undo, redo;
    private ReadOnlyTask taskToDelete;
    private List<ReadOnlyTask> lastShownList;

    
    public DeleteCommand(int targetIndex, boolean undo, boolean redo) {
        this.targetIndex = targetIndex;
        this.undo = undo;
        this.redo = redo;
    }


    @Override
    public CommandResult execute() {
    	
    	lastShownList = (undo||redo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();
    	
    	if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    	
    	taskToDelete = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);
    	
        try {
        	int getPosition =  UniqueTaskList.getInternalList().indexOf(taskToDelete);
            model.deleteTask(taskToDelete);
            saveAction(getPosition);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

```
###### /java/seedu/task/logic/commands/DeleteCommand.java
``` java
        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }
    
    /**
     * Overloaded function for the purpose of undo Command
     * 
     * Allows undo command to overwrite the success and failure messages
	 * 
	 * @return CommandResult
     */
    public CommandResult execute(String Message, String Error) {
    	
    	lastShownList = (undo||redo) ? model.getSuperbTodo().getTaskList() : model.getFilteredTaskList();
    	
    	if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Error);
        }
    	
    	taskToDelete = (undo||redo) ? lastShownList.get(targetIndex) : lastShownList.get(targetIndex - 1);
    	
        try {
        	int getPosition =  UniqueTaskList.getInternalList().indexOf(taskToDelete);
            model.deleteTask(taskToDelete);
            saveAction(getPosition);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        	return new CommandResult(Error);
        }

```
###### /java/seedu/task/logic/commands/DeleteCommand.java
``` java
        return new CommandResult(Message);
    }
    
    /**
     * function to save user's action into the UndoStorageManager
     * 
     * Determine if an action should be saved and if so, save it as an UserAction Obejct
     */
	private void saveAction(int getPosition) {
		if (!undo) {
			LogicManager.actionRecorder.recordAction(
					new UserAction(DeleteCommand.COMMAND_WORD, 
							getPosition, 
							taskToDelete)
			);
		}
	}

}
```
###### /java/seedu/task/logic/commands/ExitCommand.java
``` java
/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting SuperbTodo as requested ...";

    public ExitCommand() {}

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ExitAppRequestEvent());
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}

}
```
###### /java/seedu/task/logic/commands/HelpCommand.java
``` java
/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    public HelpCommand() {}

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowHelpRequestEvent());
        return new CommandResult(SHOWING_HELP_MESSAGE);
    }

	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}
}
```
###### /java/seedu/task/logic/commands/IncorrectCommand.java
``` java
/**
 * Represents an incorrect command. Upon execution, produces some feedback to the user.
 */
public class IncorrectCommand extends Command {

    public final String feedbackToUser;

    public IncorrectCommand(String feedbackToUser){
        this.feedbackToUser = feedbackToUser;
    }

    @Override
    public CommandResult execute() {
        indicateAttemptToExecuteIncorrectCommand();
        return new CommandResult(feedbackToUser);
    }

	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}

}

```
###### /java/seedu/task/logic/commands/ListCommand.java
``` java
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
```
###### /java/seedu/task/logic/parser/Parser.java
``` java
/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    
    private static final Pattern KEYWORDS_ARGS_SPLIT =  Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
    
    private static final Pattern TASK_PATH_ARGS_FORMAT = Pattern.compile("(?<path>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("((?<name>[^/]+)"
            		+ "\\s+(at|by|on|every|from)\\s+(?<dateTime>[^/]+)" 
            		+ "|(?<floating>[^/]+))"
            		+ "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of tags

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        } 
        
        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);
            
        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return prepareList(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();
            
        case DoneCommand.COMMAND_WORD:
        	return prepareDone(arguments);  
        	
        case UndoneCommand.COMMAND_WORD:
        	return prepareUndone(arguments);
        	
        case PathCommand.COMMAND_WORD:
        	return preparePath(arguments);        	
        	
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
```
###### /java/seedu/task/logic/parser/Parser.java
``` java
        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
    	final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        
        try {
        	return checkDoubleDateTimeParamAndAdd(matcher);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    /**
     * Parses arguments in the context of the undo command.
     * 
     * This command prepares a previous command and parse it to the router function
     * to decipher the command to route to.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUndo(){
    	assert LogicManager.getUndoManager() != null;
    	
    	UndoManagerStorage UndoStorage = LogicManager.getUndoManager();
    	Stack<UserAction> undoStack = UndoStorage.getUndoStack();
    	Stack<UserAction> redoStack = UndoStorage.getRedoStack();
    	
    	if (!undoStack.isEmpty()) {
            UserAction prevAction = undoStack.pop();
            redoStack.push(prevAction);
            
            return new UndoCommand(prevAction);
        } 
    	return new IncorrectCommand(MESSAGE_UNDO_LIMIT);
    }
    
    /**
     * Parses arguments in the context of the redo command.
     * 
     * This command prepares a previously undone command and parse it to a router function
     * to decipher the command to route to
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareRedo(){
    	assert LogicManager.getUndoManager() != null;
    	
    	UndoManagerStorage UndoStorage = LogicManager.getUndoManager();
    	Stack<UserAction> redoStack = UndoStorage.getRedoStack();
    	
    	if (!redoStack.isEmpty()) {
            UserAction prevAction = redoStack.pop();
            
            return new RedoCommand(prevAction);
        } 
    	return new IncorrectCommand(RedoCommand.FEEDBACK_UNSUCCESSFUL_REDO);
    }
    
    /**
     * Helper function for an add command (Pre-validation).
     * This function serves the purpose of checking the string twice if the date and time object are separated
     * E.g: by 3pm on Sunday
     *
     * Any subsequent time period string will be considered as part of task name
     *
     * @param regex matcher object
     * @return the prepared command
     */
	private Command checkDoubleDateTimeParamAndAdd(final Matcher matcher) throws IllegalValueException {
		String getDateTime, getName, secondDateTime;
		getName = matcher.group("name");
		getDateTime = matcher.group("dateTime");
		
		if (getName == null) {
			getName = matcher.group("floating");
			
			return new AddCommand(
					getName,
					"",
		            getTagsFromArgs(matcher.group("tagArguments")),
		            false
		    );
		} else {
			if (DateTime.isValidDate(getDateTime)) {
				// check if secondary datetime parameter exist
				Matcher secondMatch = TASK_DATA_ARGS_FORMAT.matcher(getName.trim());
				if (secondMatch.matches()) {
					secondDateTime = secondMatch.group("dateTime");
					if (secondDateTime != null && DateTime.isValidDate(secondDateTime)) {
						getName = secondMatch.group("name");
						getDateTime = secondDateTime + " " + getDateTime;
					}
		        }
				return new AddCommand(
		    			getName,
		    			getDateTime,
		                getTagsFromArgs(matcher.group("tagArguments")),
		                false
		        );
			} else if (getDateTime != null && getName != null && !getName.trim().equals("") && !getDateTime.trim().equals("")){
				// treat as floating task
				return new AddCommand(
						matcher.group(),
						"",
			            getTagsFromArgs(matcher.group("tagArguments")),
			            false
			    );
			} else {
				return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
			}
		}
	}
	
	/**
     * Helper function for an edit command.
     * This function serves the purpose of checking the string twice if the date and time object are separated
     * E.g: by 3pm on Sunday
     *
     * Any subsequent time period string will be considered as part of task name
     *
     * @param regex matcher object, target index to modify
     * @return the prepared command
     */
	private Command checkDoubleDateTimeParamAndEdit(final Matcher matcher, int target) throws IllegalValueException {
		String getDateTime, getName, secondDateTime;
		getName = matcher.group("name");
		getDateTime = matcher.group("dateTime");
		
		if (getName == null) {
			getName = matcher.group("floating");
			
			return new EditCommand(
					target,
					getName,
					"",
		            getTagsFromArgs(matcher.group("tagArguments")),
		            false,
		            false
		    );
		} else {
			if (DateTime.isValidDate(getDateTime)) {
				// check if secondary datetime parameter exist
				Matcher secondMatch = TASK_DATA_ARGS_FORMAT.matcher(getName.trim());
				if (secondMatch.matches()) {
					secondDateTime = secondMatch.group("dateTime");
					if (secondDateTime != null && DateTime.isValidDate(secondDateTime)) {
						getName = secondMatch.group("name");
						getDateTime = secondDateTime + " " + getDateTime;
					}
		        }
				return new EditCommand(
						target,
		    			getName,
		    			getDateTime,
		                getTagsFromArgs(matcher.group("tagArguments")),
		                false,
		                false
		        );
			} else if (getDateTime != null && getName != null && !getName.trim().equals("") && !getDateTime.trim().equals("")){
				// treat as floating task
				return new AddCommand(
						matcher.group(),
						"",
			            getTagsFromArgs(matcher.group("tagArguments")),
			            false
			    );
			} else {
				return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
			}
		}
	}

    /**
     * Extracts the new tasks's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get(), false, false);
    }
    
    /**
     * Parses arguments in the context of the edit task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
    	Optional<Integer> index = parseIndex(args.trim().substring(0, 1));
        if (!index.isPresent()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

    	final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim().substring(1).trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        try {
        	return checkDoubleDateTimeParamAndEdit(matcher, index.get());
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }

    }
```
###### /java/seedu/task/logic/parser/Parser.java
``` java
    /**
     * Parses arguments in the context of the list task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareList(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListCommand.MESSAGE_USAGE));
        }
        try {
        	return new ListCommand(args.trim());
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

}
```
###### /java/seedu/task/model/ModelManager.java
``` java
    @Override
    public void updateFilteredListToShowByType(String arg) {
    	updateFilteredTaskList(new PredicateExpression(new TypeQualifier(arg)));
    }
    
    @Override
    public void updateFilteredListToShowByTime(Date start, Date end, int type) {
    	updateFilteredTaskList(new PredicateExpression(new TimeQualifier(start, end, type)));
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
    	viewCondition = expression;
    	if (expression == null) {
    		filteredTasks.setPredicate(null);
    	} else {
    		filteredTasks.setPredicate(expression::satisfies);
    	}
    }
    
    private void refreshFilteredTaskList() {
    	updateFilteredTaskList(viewCondition);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    private class TypeQualifier implements Qualifier {
        private String type;

        TypeQualifier(String arg) {
            this.type = arg.toUpperCase();
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            if (type.equals(ListCommand.LIST_TYPE_UNTIMED)) {
            	return task.getDateTime().value == null && task.getDueTime().value == null;
            } else if (type.equals(ListCommand.LIST_TYPE_TIMED)) {
            	return task.getDateTime().value == null && task.getDueTime().value != null;
            } else if (type.equals(ListCommand.LIST_TYPE_EVENT)){
            	return task.getDateTime().value != null && task.getDueTime().value != null;
            } else if (type.equals(ListCommand.LIST_TYPE_DONE)){
            	return task.isDoneTask();
            } else if (type.equals(ListCommand.LIST_TYPE_UNDONE)){
            	return !task.isDoneTask();
            }
            return false;
        }

        @Override
        public String toString() {
            return "type=" + String.join(", ", this.type);
        }
    }
    
    private class TimeQualifier implements Qualifier {
        private Date startDate, endDate;
        private int searchType;

        TimeQualifier(Date start, Date end, int type) {
            this.startDate = start;
            this.endDate = end;
            this.searchType = type;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
        	Date compareDate = task.getDueTime().value;
        	if (compareDate != null) {
	            if (searchType == ListCommand.MAP_LIST_TYPE_TODAY || 
	            		searchType == ListCommand.MAP_LIST_TYPE_TOMORROW || 
	            		searchType == ListCommand.MAP_LIST_TYPE_DATE) {
	            	return startDate.before(compareDate) && endDate.after(compareDate);
	            } else if (searchType == ListCommand.MAP_LIST_TYPE_OVERDUE) {
	            	return endDate.after(compareDate) && !task.isDoneTask();
	            }
        	}
            return false;
        }

        @Override
        public String toString() {
            return "start time=" + String.join(", ", startDate.toString()) + "end time=" + String.join(", ", endDate.toString());
        }
    }

}
```
###### /java/seedu/task/model/task/ReadOnlyTask.java
``` java
/**
 * A read-only immutable interface for a Task in SuperbToDo. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyTask {

	TaskName getName();

	DateTime getDateTime();

	DueDateTime getDueTime();

	boolean isDoneTask();

	/**
	 * The returned TagList is a deep copy of the internal TagList, changes on
	 * the returned list will not affect the task's internal tags.
	 */
	UniqueTagList getTags();

	/**
	 * Returns true if both have the same state. (interfaces cannot override
	 * .equals)
	 */
	default boolean isSameStateAs(ReadOnlyTask other) {
		boolean checkSameDateTime = (other.getDateTime() != null) ? other.getDateTime().equals(this.getDateTime()) : false;
		boolean checkSameDueDateTime = (other.getDueTime() != null) ? other.getDueTime().equals(this.getDueTime()) : false;
		
		
		return other == this // short circuit if same object
				|| (other != null // this is first to avoid NPE below
						&& other.getName().equals(this.getName()) // state
																	// checks
																	// here
																	// onwards
						&& checkSameDateTime
						&& checkSameDueDateTime);
	}

	/**
	 * Formats the task as text, showing all task details.
	 * 
	 * This function is updated to only show non-empty details as information
	 */
	default String getAsText() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getName());

		if (!getDateTime().date_value.equals("")) {
			builder.append(" Start: ").append(getDateTime());
		}

		if (!getDueTime().date_value.equals("")) {
			builder.append(" End: ").append(getDueTime());
		}

		if (getTags().iterator().hasNext()) {
			builder.append(" Tags: ");
			getTags().forEach(builder::append);
		}
		return builder.toString();
	}

	/**
	 * Returns a string representation of this Task's tags
	 */
	default String tagsString() {
		final StringBuffer buffer = new StringBuffer();
		final String separator = ", ";
		getTags().forEach(tag -> buffer.append(tag).append(separator));
		if (buffer.length() == 0) {
			return "";
		} else {
			return buffer.substring(0, buffer.length() - separator.length());
		}
	}

}
```
###### /java/seedu/task/model/task/Task.java
``` java
/**
 * Represents a Task in SuperbToDo.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private TaskName name;
    private DateTime dueDate;
    private DueDateTime dueTime;
    private UniqueTagList tags;
    private boolean isDone;
    
    /**
     * Constructor with isDone.
     */
    public Task(TaskName name, DateTime dueDate, DueDateTime dueTime, UniqueTagList tags, Boolean isDone) {
        assert !CollectionUtil.isAnyNull(name, dueDate, dueTime, tags);
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        this.isDone = isDone;
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDateTime(), source.getDueTime(), source.getTags(), source.isDoneTask());
    }
    
    @Override
    public TaskName getName() {
        return name;
    }

    @Override
    public DateTime getDateTime() {
        return dueDate;
    }

    @Override
    public DueDateTime getDueTime() {
        return dueTime;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }
    //add Done 
    @Override
    public boolean isDoneTask() {
        return isDone;
    }
    
    public void setDoneTask() {
    	this.isDone = true;
    }
    
    public void setUndoneTask() {
    	this.isDone = false;
    }
    

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, dueDate, dueTime, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
```
###### /java/seedu/task/model/task/TaskName.java
``` java
/**
 * Represents a Task's name in the address book. Guarantees: immutable; is valid
 * as declared in {@link #isValidName(String)}
 */
public class TaskName {

	public static final String MESSAGE_NAME_CONSTRAINTS = "Task names should be spaces or alphanumeric characters";
	public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

	public final String fullName;

	/**
	 * Validates given name.
	 *
	 * @throws IllegalValueException
	 *             if given name string is invalid.
	 */
	public TaskName(String name) throws IllegalValueException {
		assert name != null;
		name = name.trim();
		if (!isValidName(name)) {
			throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
		}
		this.fullName = name;
	}

	/**
	 * Returns true if a given string is a valid task name.
	 */
	public static boolean isValidName(String test) {
		return test.matches(NAME_VALIDATION_REGEX);
	}

	@Override
	public String toString() {
		return fullName;
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof TaskName // instanceof handles nulls
						&& this.fullName.equals(((TaskName) other).fullName)); // state
																				// check
	}

	@Override
	public int hashCode() {
		return fullName.hashCode();
	}

}
```

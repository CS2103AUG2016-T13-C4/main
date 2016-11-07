# A0135763Breused
###### /src/main/java/seedu/address/logic/commands/AddCommand.java
``` java
    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
```
###### /src/main/java/seedu/address/logic/commands/ClearCommand.java
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
}
```
###### /src/main/java/seedu/address/logic/commands/Command.java
``` java
/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
```
###### /src/main/java/seedu/address/logic/commands/Command.java
``` java
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForPersonListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

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
###### /src/main/java/seedu/address/logic/commands/CommandResult.java
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
###### /src/main/java/seedu/address/logic/commands/DeleteCommand.java
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

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;       
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask personToDelete = lastShownList.get(targetIndex - 1);

        try {
            model.deleteTask(personToDelete);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

```
###### /src/main/java/seedu/address/logic/commands/DeleteCommand.java
``` java
        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, personToDelete));

    }

}
```
###### /src/main/java/seedu/address/logic/commands/ExitCommand.java
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

}
```
###### /src/main/java/seedu/address/logic/commands/HelpCommand.java
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
}
```
###### /src/main/java/seedu/address/logic/commands/IncorrectCommand.java
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

}

```
###### /src/main/java/seedu/address/logic/commands/ListCommand.java
``` java
/**
 * Lists all tasks in SuperbTodo to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    public ListCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAll();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /src/main/java/seedu/address/logic/parser/Parser.java
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
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
```
###### /src/main/java/seedu/address/logic/parser/Parser.java
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
     * Helper function for an add command.
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
		            getTagsFromArgs(matcher.group("tagArguments"))
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
		                getTagsFromArgs(matcher.group("tagArguments"))
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
		            getTagsFromArgs(matcher.group("tagArguments"))
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
		                getTagsFromArgs(matcher.group("tagArguments"))
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

        return new DeleteCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the edit task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
    	Optional<Integer> index = parseIndex(args.trim().substring(0, 1));
        if(!index.isPresent()){
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

    /**
     * CURRENTLY UNUSED
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

}
```
###### /src/main/java/seedu/address/model/task/ReadOnlyTask.java
``` java
/**
 * A read-only immutable interface for a Task in SuperbToDo.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    TaskName getName();
    DateTime getDateTime();
    DueDateTime getDueTime();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getDateTime().equals(this.getDateTime())
                && other.getDueTime().equals(this.getDueTime()));
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
            builder.append(" Start: ")
                   .append(getDateTime());
        }
        
        if (!getDueTime().date_value.equals("")) {
            builder.append(" Due: ")
                   .append(getDueTime());
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
###### /src/main/java/seedu/address/model/task/Task.java
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

    /**
     * Every field must be present and not null.
     */
    public Task(TaskName name, DateTime dueDate, DueDateTime dueTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, dueDate, dueTime, tags);
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDateTime(), source.getDueTime(), source.getTags());
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
###### /src/main/java/seedu/address/model/task/TaskName.java
``` java
/**
 * Represents a Task's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class TaskName {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Task names should be spaces or alphanumeric characters";
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String fullName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
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
                && this.fullName.equals(((TaskName) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
```
###### /src/main/java/seedu/address/storage/XmlAdaptedTask.java
``` java
/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String duedate;
    @XmlElement(required = true)
    private String time;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        name = source.getName().fullName;
        date = source.getDateTime().date_value;
        duedate = source.getDueTime().date_value;
        time = source.getDateTime().time_value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        final TaskName name = new TaskName(this.name);
        final DateTime date = new DateTime(this.date);
        final DueDateTime duedate = new DueDateTime(this.duedate);
        final UniqueTagList tags = new UniqueTagList(personTags);
        return new Task(name, date, duedate, tags);
    }
}
```

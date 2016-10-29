package seedu.address.logic.parser;

import seedu.address.logic.commands.*;

import seedu.address.model.task.DateTime;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.exceptions.IllegalValueException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

//@@author A0135763B-reused
/**
 * Parses user input.
 */
public class Parser {
    //@@author A0113992B
    private String commandWord, taskInfo = "", editTaskInfo = "";
    private int taskIndex;
    
    //@@author A0135763B-reused
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
        // @@author A0113992B
        this.commandWord = commandWord;
        
        //@@author A0135763B-reused
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
        // @@author A0113992B    
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();
//        case RedoCommand.COMMAND_WORD:
//            return new RedoCommand();
        //@@author A0135763B-reused
        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    
    // @@author A0113992B
    public String getInputCommandWord() {
        return commandWord;
    }
    
    //@@author A0135763B-reused
    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    @SuppressWarnings("null")
    private Command prepareAdd(String args){
    	final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        
        try {
            taskIndex = (Integer) null;
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

        editTaskInfo = args;
        
        final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim().substring(1).trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        try {
            taskIndex = index.get();
            return checkDoubleDateTimeParamAndEdit(matcher, index.get());
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
        
    }
    
    /**
     * returns edit task information for undo and redo purposes
     * @return
     */
    public String getEditTaskInfo() {
        return editTaskInfo;
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
		    )
			        ;
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
        taskIndex = index.get();
        
        return prepareTaskToDeleteInfo(index.get());
    }
    
    /** 
     * prepares to-be deleted task information for undo and redo purposes
     * 
     * @param index
     * @return
     */
    private Command prepareTaskToDeleteInfo(int index) {
        DeleteCommand taskToDelete = new DeleteCommand(index);
        taskInfo = taskToDelete.getTaskInfo();
        return taskToDelete;
    }
    
    /**
     * returns to-be deleted task information for undo and redo purposes
     * @return
     */
    public String getTaskToDeleteInfo() {
        return taskInfo;
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
    
    /**
     * Returns taskIndex of correct command entered
     */
    public int getTaskIndex() {
        return taskIndex;
    }

}
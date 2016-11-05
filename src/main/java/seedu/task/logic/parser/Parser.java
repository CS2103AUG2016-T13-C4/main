package seedu.task.logic.parser;

import seedu.task.logic.commands.PathCommand;
import seedu.task.model.UserAction;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.*;
import seedu.task.model.task.DateTime;
import seedu.task.storage.UndoManagerStorage;

import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0135763B-reused
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
        // @@author A0113992B    
        case UndoCommand.COMMAND_WORD:
        	return prepareUndo();
            //return new UndoCommand();
//        case RedoCommand.COMMAND_WORD:
//            return new RedoCommand();
        //@@author A0135763B-reused
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
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUndo(){
    	UndoManagerStorage UndoStorage = LogicManager.getUndoManager();
    	Stack<UserAction> undoStack = UndoStorage.getUndoStack();
    	Stack<UserAction> redoStack = UndoStorage.getRedoStack();
    	
    	if (!undoStack.isEmpty()) {
            UserAction prevAction = undoStack.pop();
            redoStack.push(prevAction);
            String commandWord = prevAction.getCommandWord();
            
            switch(commandWord) {
	            case EditCommand.COMMAND_WORD:
	                //assert prevCommand.getName().toString()!= null;
	            	try {
	            		return new EditCommand(prevAction.getBackUpTask(), prevAction.getIndex(), true);
	                } catch (IllegalValueException ive) {
	                    return new IncorrectCommand(ive.getMessage());
	                }
	            case AddCommand.COMMAND_WORD:
	                //assert prevCommand.getName().toString()!= null;
	                return new DeleteCommand(prevAction.getIndex(), true);
	            case DeleteCommand.COMMAND_WORD:
	                //assert prevCommand.getName().toString() != null;
	            	try {
	            		return new AddCommand(prevAction.getBackUpTask(), prevAction.getIndex());
	                } catch (IllegalValueException ive) {
	                    return new IncorrectCommand(ive.getMessage());
	                }
	            	//undoRemoveCommand(prevCommand);
	            case UndoneCommand.COMMAND_WORD:
	                //assert prevCommand.getName().toString() != null;
	                //return undoUndoneCommand(prevCommand);
	            case DoneCommand.COMMAND_WORD:
	                //assert prevCommand.getName().toString() != null;
	                //return undoDoneCommand(prevCommand);
	            default:
	                return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
            }
        }
		return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
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
			} else if (getDateTime != null && getName != null && !getName.trim().equals("") && !getDateTime.trim().equals("")){
				// treat as floating task
				return new AddCommand(
						matcher.group(),
						"",
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
				return new EditCommand(
						target,
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

        return new DeleteCommand(index.get(), false);
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
    //@@author A0133945B
    
    /**
     * Parses arguments in the context of the done task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDone(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }
        
        try {
			return new DoneCommand(index.get());
		} catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
		}
        
        
    }
    
    /**
     * Parses arguments in the context of the undone task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUndone(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoneCommand.MESSAGE_USAGE));
        }
        
        try {
			return new UndoneCommand(index.get());
		} catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
		} 
    }
    
    /**
     * Parses arguments in the context of the path task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command preparePath(String args) {
        Optional<String> path = parsePath(args);
        if(!path.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PathCommand.MESSAGE_USAGE));
        }
        
        try {
        	String temp = path.get();
        	int strLength = temp.length();
        	if (temp.charAt(strLength - 1) == '/') {
        		temp += "taskbook.txt";
        	} else {
        		temp += "/tastbook.txt";
        	}
			return new PathCommand(temp);
		} catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
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
     * Returns parsed path string
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<String> parsePath(String command) {
        final Matcher matcher = TASK_PATH_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String path = matcher.group("path");
        if(path.isEmpty()){
            return Optional.empty();
        }
        
        return Optional.of(path);

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
        final Set<String> keywordSet = splitSearchParam(args);
        return new FindCommand(keywordSet);
    }

    /**
     * Custom split function to split a string by spaces. String within quotation marks will be considered as a single string
     *
     * @param string representing the search conditions
     * @return the set of split strings
     */
	private Set<String> splitSearchParam(String args) {
		Set<String> returnlist = new HashSet<String>();
        Matcher m = KEYWORDS_ARGS_SPLIT.matcher(args.trim());
        while (m.find()) {
        	returnlist.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.
        }
        
        return returnlist;
	}
    
    // @@author A0135763B-reused
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
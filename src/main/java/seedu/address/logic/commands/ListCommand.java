package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Set;

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

    public static final int MAP_LIST_TYPE_ALL = 0;
    public static final int MAP_LIST_TYPE_TIMED = 1;
    public static final int MAP_LIST_TYPE_UNTIMED = 2;
    public static final int MAP_LIST_TYPE_EVENT = 3;
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: List (all/timed/untimed/#keyword/overdue/today/tomorrow/<date>)\n"
            + "Example: " + COMMAND_WORD + " all";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    private final int listType;
    
    public ListCommand(String args) {
    	args = args.toUpperCase().trim();
    	
    	if (args.equals(LIST_TYPE_ALL)) {
    		this.listType = MAP_LIST_TYPE_ALL;
    	} else if (args.equals(LIST_TYPE_TIMED)) {
    		this.listType = MAP_LIST_TYPE_TIMED;
    	} else if (args.equals(LIST_TYPE_UNTIMED)) {
    		this.listType = MAP_LIST_TYPE_UNTIMED;
    	} else if (args.equals(LIST_TYPE_EVENT)) {
    		this.listType = MAP_LIST_TYPE_EVENT;
    	} else {
    		this.listType = 4;
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
    	}
    	
    	return new CommandResult(MESSAGE_SUCCESS);
    }
}

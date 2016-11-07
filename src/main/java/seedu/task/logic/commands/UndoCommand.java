//@@author A0135763B
package seedu.task.logic.commands;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.LogicManager;
import seedu.task.model.UserAction;
import seedu.task.model.task.*;
import seedu.task.storage.UndoManagerStorage;

import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Stack;
import java.util.Vector;


public class UndoCommand extends Command {
      public static final String COMMAND_WORD = "undo";

      public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo a task to SuperbTodo. "
            //undo a command just carried out
            + "Format: Undo\n"
            + "Example: " + COMMAND_WORD;
      
    // Exception messages
    private static final String MESSAGE_EXCEPTION_REMOVE = "Nothing to be removed";
    private static final String MESSAGE_EXCEPTION_ADD = "Nothing to be added";
    private static final String MESSAGE_EXCEPTION_EDIT = "Nothing to be edit";
    private static final String MESSAGE_EXCEPTION_DONE = "Nothing to be done";
    private static final String MESSAGE_EXCEPTION_UNDONE = "Nothing to undone";

    // list of commands
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_UNDONE = "undone";


    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_EDIT_UNDO = "undo edit command";
    private static final String FEEDBACK_SUCCESSFUL_REMOVE_UNDO = "undo remove command";
    private static final String FEEDBACK_SUCCESSFUL_ADD_UNDO = "undo add command";
    private static final String FEEDBACK_SUCCESSFUL_UNDONE_UNDO = "undo undone command";
    private static final String FEEDBACK_SUCCESSFUL_DONE_UNDO = "undo done command";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "redo last command";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "undo failed";
    
    private final Command toUndo;
    private final String commandType;
    
    /**
     * Undo the previous command of the user.
     * 
     * @return void
     */
    public UndoCommand (UserAction action) {
    	this.toUndo = routeCommand(action);
    	this.commandType = action.getCommandWord();
    }

    // @@author A0135763B
    /**
     * Helper function to route the User Action into appropriate undo commands
     * 
     * @return command
     */
    public Command routeCommand(UserAction action) {
    	String commandWord = action.getCommandWord();
    	
    	switch(commandWord) {
	        case EditCommand.COMMAND_WORD:
	        	return undoEdit(action);
	        case AddCommand.COMMAND_WORD:
	        	return undoAdd(action);
	        case DeleteCommand.COMMAND_WORD:
	        	return undoDelete(action);
	        case UndoneCommand.COMMAND_WORD:
	        	return undoUndone(action);
	        case DoneCommand.COMMAND_WORD:
	        	return undoDone(action);
	        default:
	            return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	    }
    }
    
    /**
     * Router function to undo a done command
     * 
     * @return Command
     */
	private Command undoDone(UserAction action) {
		assert action.getIndex() != -1;
		try {
			return new UndoneCommand(action.getIndex(), true, false);
		} catch (IllegalValueException ive) {
		    return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
		}
	}
	
	/**
     * Router function to undo an undone command
     * 
     * @return Command
     */
	private Command undoUndone(UserAction action) {
		assert action.getIndex() != -1;
		try {
			return new DoneCommand(action.getIndex(), true, false);
		} catch (IllegalValueException ive) {
		    return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
		}
	}
	
	/**
     * Router function to undo a delete command
     * 
     * @return Command
     */
	private Command undoDelete(UserAction action) {
		assert action.getIndex() != -1;
		try {
			return new AddCommand(action.getBackUpTask(), action.getIndex(), true);
		} catch (IllegalValueException ive) {
		    return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
		}
	}
	
	/**
     * Router function to undo an add command
     * 
     * @return Command
     */
	private Command undoAdd(UserAction action) {
		assert action.getIndex() != -1;
		return new DeleteCommand(action.getIndex(), true, false);
	}
	
	/**
     * Router function to undo an edit command
     * 
     * @return Command
     */
	private Command undoEdit(UserAction action) {
		assert action.getBackUpTask() != null;
		
		try {
			return new EditCommand(action.getBackUpTask(), action.getIndex(), true, false);
		} catch (IllegalValueException ive) {
		    return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
		}
	}
	
	// @@author A0113992B
	@Override
	public CommandResult execute() {
		toUndo.setData(model);
		switch (commandType) {
	        case COMMAND_ADD:
	        	return toUndo.execute(FEEDBACK_SUCCESSFUL_ADD_UNDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_REMOVE:
	        	return toUndo.execute(FEEDBACK_SUCCESSFUL_REMOVE_UNDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_EDIT:
	        	return toUndo.execute(FEEDBACK_SUCCESSFUL_EDIT_UNDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_DONE:
	        	return toUndo.execute(FEEDBACK_SUCCESSFUL_DONE_UNDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_UNDONE:
	        	return toUndo.execute(FEEDBACK_SUCCESSFUL_UNDONE_UNDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        default:
	        	return toUndo.execute();
		}
	}
	
	// @@author A0135763B
	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}

  
}


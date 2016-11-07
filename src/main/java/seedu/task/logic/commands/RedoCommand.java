package seedu.task.logic.commands;
//@@author A0135763B

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.CommandRecorder;
import seedu.task.model.UserAction;

public class RedoCommand extends Command{
    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo a task to SuperbTodo. "
          //undo a command just carried out
          + "Format: Redo\n"
          + "Example: " + COMMAND_WORD;

    // Exception messages
    private static final String MESSAGE_EXCEPTION_REMOVE = "Nothing to be removed";
    private static final String MESSAGE_EXCEPTION_ADD = "Nothing to be added";
    private static final String MESSAGE_EXCEPTION_EDIT = "Nothing to be edit";

    // list of commands
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "Remove";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_UNDONE = "undone";


    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";
    public static final String FEEDBACK_UNSUCCESSFUL_REDO = "You have reached the last redo";
    
    private final Command toRedo;
    private final String commandType;
    
    /**
     * Undo the previous command carried out by user.
     * 
     * @return void
     */
    public RedoCommand (UserAction action) {
    	this.toRedo = routeCommand(action);
    	this.commandType = action.getCommandWord();
    }
    
    public Command routeCommand(UserAction action) {
    	String commandWord = action.getCommandWord();
    	
    	switch(commandWord) {
	        case EditCommand.COMMAND_WORD:
	        	assert action.getBackUpTask() != null;
	        	
	        	try {
	        		return new EditCommand(action.getBackUpEditTask(), action.getIndex(), false, true);
	            } catch (IllegalValueException ive) {
	                return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	            }
	        case AddCommand.COMMAND_WORD:
	        	assert action.getIndex() != -1;
	        	try {
	        		return new AddCommand(action.getBackUpTask(), action.getIndex(), false);
	            } catch (IllegalValueException ive) {
	                return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	            }
	        case DeleteCommand.COMMAND_WORD:
	        	assert action.getIndex() != -1;
	        	return new DeleteCommand(action.getIndex(), false, true);
	        case UndoneCommand.COMMAND_WORD:
	        	assert action.getIndex() != -1;
	        	try {
	        		return new UndoneCommand(action.getIndex(), false, true);
	            } catch (IllegalValueException ive) {
	                return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	            }
	        case DoneCommand.COMMAND_WORD:
	        	assert action.getIndex() != -1;
	        	try {
	        		return new DoneCommand(action.getIndex(), false, true);
	            } catch (IllegalValueException ive) {
	                return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	            }
	        default:
	            return new IncorrectCommand(FEEDBACK_UNSUCCESSFUL_UNDO);
	    }
    }

	@Override
	public CommandResult execute() {
		toRedo.setData(model);
		switch (commandType) {
	        case COMMAND_ADD:
	        	return toRedo.execute(FEEDBACK_SUCCESSFUL_REDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_REMOVE:
	        	return toRedo.execute(FEEDBACK_SUCCESSFUL_REDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_EDIT:
	        	return toRedo.execute(FEEDBACK_SUCCESSFUL_REDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_DONE:
	        	return toRedo.execute(FEEDBACK_SUCCESSFUL_REDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        case COMMAND_UNDONE:
	        	return toRedo.execute(FEEDBACK_SUCCESSFUL_REDO, FEEDBACK_UNSUCCESSFUL_UNDO);
	        default:
	        	return toRedo.execute();
		}
	}

	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}

    
}

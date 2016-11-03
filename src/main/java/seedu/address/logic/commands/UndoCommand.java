// @@author A0113992B
package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.*;
import seedu.address.storage.UndoManagerStorage;
import seedu.address.logic.LogicManager;


import java.util.Stack;
import java.util.Vector;


// @@author A0113992B
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


    

    private LogicManager logicM = new LogicManager(model, null);
    private UndoManagerStorage undoM = LogicManager.theOne;
    private String commandWord;
    
    private Stack<Task> undoStack;
    private Stack<Task> redoStack;
    private Vector<UndoManagerStorage> storedTasksUndone;
    private Vector<UndoManagerStorage> storedTasksDone;
    
    public UndoCommand () {
        this.undoStack = undoM.getUndoStack();
        this.redoStack = undoM.getRedoStack();
        this.storedTasksUndone = undoM.getStoredTasksUndone();
        this.storedTasksDone = undoM.getStoredTasksDone();       
    }
    
    
    /**
     * Undo the previous command carried out by user.
     * 
     * @return void
     */
    @Override
    public CommandResult execute() {
        assert undoStack != null;
        if (!undoStack.isEmpty()) {
            Task prevCommand = undoStack.pop();
            redoStack.push(prevCommand);
            commandWord = storedTasksUndone.lastElement().getCommadnWord();
            
            switch(commandWord) {
            case COMMAND_EDIT:
                assert prevCommand.getName().toString()!= null;
                return undoEditCommand(prevCommand);
            case COMMAND_ADD:
                assert prevCommand.getName().toString()!= null;
                return undoAddCommand(prevCommand);
            case COMMAND_REMOVE:
                assert prevCommand.getName().toString() != null;
                return undoRemoveCommand(prevCommand);
            case COMMAND_UNDONE:
                assert prevCommand.getName().toString() != null;
                return undoUndoneCommand(prevCommand);
            case COMMAND_DONE:
                assert prevCommand.getName().toString() != null;
                return undoDoneCommand(prevCommand);
            }
        }
        return new CommandResult(FEEDBACK_UNSUCCESSFUL_UNDO);       
        
    }

    
    public CommandResult undoEditCommand(Task prevCommand) {
        storedTasksUndone.remove(undoM);       
        String index = String.format("%1$d", undoM.getIndex());       
        String output = prevCommand.getName().toString() + " " + prevCommand.getDateTime().toString() 
                        + " " + prevCommand.getDueTime().toString() + " "
                        + prevCommand.getTags().toString() + " ";
        logicM.execute("remove " + index);
        logicM.execute("add "+ output);
      
        return new CommandResult(FEEDBACK_SUCCESSFUL_EDIT_UNDO);
    }

    /**
     * Undo add command
     * @param prevCommand
     * @return
     */
    public CommandResult undoAddCommand(Task prevCommand) { 
        storedTasksUndone.remove(undoM);
        storedTasksDone.add(undoM);
        String index = String.format("%1$d", undoM.getIndex());
        logicM.execute("remove " + index);
       
        return new CommandResult(FEEDBACK_SUCCESSFUL_ADD_UNDO);
    }
    
    
    /**
     * Undo Remove command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult undoRemoveCommand(Task prevCommand) {
        String output = prevCommand.getName().toString() + " " + prevCommand.getDateTime().toString() 
                        + " " + prevCommand.getDueTime().toString() + " "
                        + prevCommand.getTags().toString() + " ";
        storedTasksUndone.add(undoM);
        logicM.execute("add " + output);
        return new CommandResult(FEEDBACK_SUCCESSFUL_REMOVE_UNDO);
    }

        
    /** Undo undone command
     * 
     * @param previousAction
     *            user's input event
     * @return successful feedback message
     */
    public CommandResult undoUndoneCommand(Task prevCommand) {
        int index = UniqueTaskList.getInternalList().indexOf(prevCommand);
        String num = String.format("%1$d", index);
        logicM.execute("done " + num);
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDONE_UNDO);
    }
    
    /**
     * Undo done command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult undoDoneCommand(Task prevCommand) {
        storedTasksUndone.add(undoM);            
        String output = prevCommand.getName().toString() + " " + prevCommand.getDateTime().toString() 
                        + " " + prevCommand.getDueTime().toString() + " "
                        + prevCommand.getTags().toString() + " ";
        logicM.execute("add "+ output);
        return new CommandResult(FEEDBACK_SUCCESSFUL_DONE_UNDO);
    }

  
}


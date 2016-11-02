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
//    private static final String MESSAGE_EXCEPTION_DONE = "Nothing to be done";
//    private static final String MESSAGE_EXCEPTION_UNDONE = "Nothing to undone";

    // list of commands
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_EDIT = "edit";
//    private static final String COMMAND_DONE = "done";
//    private static final String COMMAND_UNDONE = "undone";


    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";

    

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
            }
//            case COMMAND_UNDONE:
//                assert prevAction.getlistTypePrev() != null
//                && prevAction.getTask() != null;
//                return undoUndoneCommand(prevAction);
//            case COMMAND_DONE:
//                assert prevAction.getlistTypePrev() != null
//                && prevAction.getTask() != null;
//                return undoDoneCommand(prevAction);
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
      
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
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
       
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
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
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }

        
 //     * Undo undone command
//     * 
//     * @param previousAction
//     *            user's input event
//     * @return successful feedback message
//     */
//    public CommandResult undoUndoneCommand(CommandRecorder previousAction) {
//        if (previousAction.getlistTypePrev().equals(LIST_UNDONE)) {
//            storedTasksDone.add(previousAction.gettaskPrev());
//            storedTasksUndone.remove(previousAction.gettaskPrev());
//        } else {
//            throw new IllegalArgumentException(MESSAGE_EXCEPTION_UNDONE);
//        }
//        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
//    }
//    
//    /**
//     * Undo done command
//     * 
//     * @param prevAction
//     *            user's input CommandRecorder
//     * @return successful feedback message
//     */
//    public CommandResult undoDoneCommand(CommandRecorder prevAction) {
//        if (prevAction.getlistTypePrev().equals(LIST_DONE)) {
//            storedTasksDone.remove(prevAction.gettaskPrev());
//            storedTasksUndone.add(prevAction.gettaskPrev());
//        } else {
//            throw new IllegalArgumentException(MESSAGE_EXCEPTION_DONE);
//        }
//        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
//    }

   
//    /**
//     * Remove the Singleton instance for unit testing purposes
//     */
//    public void clearStateForTesting() {
//        selectedCommand = null;
//    }
}


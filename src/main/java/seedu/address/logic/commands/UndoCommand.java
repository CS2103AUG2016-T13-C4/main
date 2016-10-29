// @@author A0113992B
package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;
import seedu.address.storage.UndoManagerStorage;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandRecorder;

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
    private static final String COMMAND_REMOVE = "Remove";
    private static final String COMMAND_EDIT = "edit";
//    private static final String COMMAND_DONE = "done";
//    private static final String COMMAND_UNDONE = "undone";


    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";

    
    private Command command;
    private String commandText;
    private LogicManager logicMng;
    private UndoManagerStorage undoManager;
    private Stack<UndoManagerStorage> undoStack; // to track all input tasks 
    private Stack<UndoManagerStorage> redoStack; // to track redo tasks
    private static Vector<UndoManagerStorage> storedTasksUndone; // to store undo tasks
    private static Vector<UndoManagerStorage> storedTasksDone; // to store redo tasks
    
    public UndoCommand () {        
        this.command = undoManager.getCommand();
        this.commandText = undoManager.getCommandText();
        this.undoStack = undoManager.getUndoStack();
        this.redoStack = undoManager.getRedoStack();
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
            UndoManagerStorage prevCommand = undoStack.pop();
            redoStack.push(prevCommand);
            
            switch(prevCommand.getCommandWord()) {
            case COMMAND_EDIT:
                assert prevCommand.getCommandText() != null;
                return undoEditCommand(prevCommand);
            case COMMAND_ADD:
                assert prevCommand.getCommandText() != null;
                return undoAddCommand(prevCommand);
            case COMMAND_REMOVE:
                assert prevCommand.getCommandText() != null;
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

    
    public CommandResult undoEditCommand(UndoManagerStorage prevCommand) {
        if (prevCommand.getTaskIndex() != (Integer)null) {
            String output = String.format("$1%d", prevCommand.getTaskIndex());
            String taskInfo = prevCommand.getEditTaskInfo();
            
            logicMng.execute("remove " + output);
            logicMng.execute(taskInfo); 
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_EDIT);
        }
        
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }

    /**
     * Undo add command
     * @param prevCommand
     * @return
     */
    public CommandResult undoAddCommand(UndoManagerStorage prevCommand) { 
        String output = String.format("$1%d", prevCommand.getTaskIndex()); 
        
        if (prevCommand.getTaskIndex() != (Integer)null) {
            storedTasksUndone.add(prevCommand); // records down in undo vector
            logicMng.execute("remove "+ output); // remove the task entered
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_REMOVE);
        }
       
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    
    
    /**
     * Undo Remove command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult undoRemoveCommand(UndoManagerStorage prevCommand) {
        String taskInfo = prevCommand.getTaskInfo();
        if (prevCommand.getTaskIndex() != (Integer)null) {
            storedTasksUndone.add(prevCommand);
            logicMng.execute("add " +taskInfo);
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_ADD);
        }

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
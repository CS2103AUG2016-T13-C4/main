package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandRecorder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;


public class UndoCommand extends Command {
      public static final String COMMAND_WORD = "undo";

      public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo a task to SuperbTodo. "
            //undo a command just carried out
            + "Format: Undo\n"
            + "Example: " + COMMAND_WORD;
      
    LogicManager logicM;
    Parser parser;
    
    // Exception messages
    private static final String MESSAGE_EXCEPTION_REMOVE = "Nothing to be removed";
    private static final String MESSAGE_EXCEPTION_DONE = "Nothing to be done";
    private static final String MESSAGE_EXCEPTION_UNDONE = "Nothing to undone";

    // list of commands
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "Remove";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_UNDONE = "undone";

    
    // list of list names
    private static final String LIST_DONE = "finished tasks";
    private static final String LIST_UNDONE = "unfinished tasks";
    private static final String LIST_REMOVE = "removed tasks";

    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";

    private static UndoCommand selectedCommand;
    public static Stack<CommandRecorder> undoStack;
    public static Stack<CommandRecorder> redoStack;
    public static Vector<ReadOnlyTask> storedTasksUndone;
    public static Vector<ReadOnlyTask> storedTasksDone;

    
    private UndoCommand (Vector<ReadOnlyTask> storedTasksUndone, Vector<ReadOnlyTask> storedTasksDone) {
        undoStack = new Stack<CommandRecorder>();
        redoStack = new Stack<CommandRecorder>();
        this.storedTasksUndone = storedTasksUndone;
        this.storedTasksDone = storedTasksDone;
    }
    
    
    
    
    public UndoCommand() {
        
    }

    // Getters for RedoCommand use
    public static Stack<CommandRecorder> getundoStack() {
        return undoStack;
    }
    
    public static Stack<CommandRecorder> getredoStack() {
        return redoStack;
    }
    
    public static Vector<ReadOnlyTask> getstoredTasksUndone() {
        return storedTasksUndone;
    }
    
    public static Vector<ReadOnlyTask> getstoredTasksDone() {
        return storedTasksDone;
    }

    /**
     * Add user's input CommandRecorder into a stack for undo/redo purposes.
     * 
     * @param userInputAction
     *            user's input CommandRecorder
     */
    public void add(CommandRecorder userInputAction) {
        undoStack.push(userInputAction);
        redoStack.clear();
        storedTasksUndone.add(userInputAction.gettaskPrev());
    }

    /**
     * Singleton constructor for the UndoCommand.
     * 
     * @param storedTasksUndone
     *            list of tasks sorted by undone
     * @param storedTasksDone
     *            list of tasks sorted by done
     * @return UndoCommand the UndoCommand object
     */
//    public static UndoCommand getInstance(Vector<ReadOnlyTask> storedTasksUndone,Vector<ReadOnlyTask> storedTasksDone) {
//        if (selectedCommand == null) {
//            selectedCommand = new UndoCommand(storedTasksUndone, storedTasksDone);
//        }
//        return selectedCommand;
//    }

    /**
     * Undo the previous command carried out by user.
     * 
     * @return void
     */
    
    @Override
    public CommandResult execute() {

        assert undoStack != null;
        
        // parser parses the commands for execution
        //modelmanager saves the data
        //commandrecorder records down what are the content of the commands
        //call all the established commands to do it
        if (!undoStack.isEmpty()) {
            CommandRecorder prevAction = undoStack.pop();
            redoStack.push(prevAction);

            switch (prevAction.getCommand()) {
//            case COMMAND_EDIT:
//                assert prevAction.gettaskAfter() != null
//                && prevAction.gettaskPrev() != null;
//                return undoEditCommand(prevAction);
            case COMMAND_ADD:
                assert prevAction.gettaskPrev() != null;
                try {
                    return undoAddCommand(prevAction);
                } catch (TaskNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            case COMMAND_REMOVE:
                assert prevAction.gettaskPrev() != null
                && prevAction.getlistTypePrev() != null;
                return undoRemoveCommand(prevAction);
            case COMMAND_UNDONE:
                assert prevAction.getlistTypePrev() != null
                && prevAction.gettaskPrev() != null;
                return undoUndoneCommand(prevAction);
            case COMMAND_DONE:
                assert prevAction.getlistTypePrev() != null
                && prevAction.gettaskPrev() != null;
                return undoDoneCommand(prevAction);
            }
        }
        return new CommandResult(FEEDBACK_UNSUCCESSFUL_UNDO);
     }
    
    /**
     * Undo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     * @throws TaskNotFoundException 
     */
    public CommandResult undoAddCommand(CommandRecorder prevAction) throws TaskNotFoundException {
        storedTasksUndone.remove(storedTasksUndone.lastElement());
        logicM.execute(prevAction.getCommand() + " " + prevAction.getindexPrev());       
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    
    /**
     * Redo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult redoAddCommand(CommandRecorder nextAction) {
        storedTasksUndone.add(nextAction.gettaskPrev());
        logicM.execute(nextAction.getCommand() + " " + nextAction.getlistTypePrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }



    /**
     * Undo undone command
     * 
     * @param previousAction
     *            user's input event
     * @return successful feedback message
     */
    public CommandResult undoUndoneCommand(CommandRecorder previousAction) {
        if (previousAction.getlistTypePrev().equals(LIST_UNDONE)) {
            storedTasksDone.add(previousAction.gettaskPrev());
            storedTasksUndone.remove(previousAction.gettaskPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_UNDONE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    
    /**
     * Undo done command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult undoDoneCommand(CommandRecorder prevAction) {
        if (prevAction.getlistTypePrev().equals(LIST_DONE)) {
            storedTasksDone.remove(prevAction.gettaskPrev());
            storedTasksUndone.add(prevAction.gettaskPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_DONE);
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
    public CommandResult undoRemoveCommand(CommandRecorder prevAction) {
        if (prevAction.getlistTypePrev().equals(LIST_REMOVE)) {
            redoAddCommand(prevAction);
            storedTasksUndone.add(prevAction.getindexPrev(),prevAction.gettaskPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_REMOVE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }



    /**
     * Undo Edit command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    public CommandResult undoEditCommand(CommandRecorder prevAction) {
        storedTasksUndone.remove(prevAction.gettaskAfter());
        storedTasksUndone.add(prevAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    


//    /**
//     * Remove the Singleton instance for unit testing purposes
//     */
//    public void clearStateForTesting() {
//        selectedCommand = null;
//    }
}
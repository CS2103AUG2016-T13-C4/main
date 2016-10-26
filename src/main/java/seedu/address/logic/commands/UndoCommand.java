package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;
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
    private static final String FEEDBACK_UNSUCCESSFUL_REDO = "You have reached the last redo";

    private static UndoCommand selectedCommand;
    private static Stack<CommandRecorder> undoStack;

    private static Stack<CommandRecorder> redoStack;
    private static Vector<Task> storedTasksUndone;

    private static Vector<Task> storedTasksDone;

    private UndoCommand (Vector<Task> storedTasksUndone, Vector<Task> storedTasksDone) {
        undoStack = new Stack<CommandRecorder>();
        redoStack = new Stack<CommandRecorder>();
        this.storedTasksUndone = storedTasksUndone;
        this.storedTasksDone = storedTasksDone;
    }
    
    
    // Getters
    
    public static Stack<CommandRecorder> getundoStack() {
        return undoStack;
    }
    
    public static Stack<CommandRecorder> getredoStack() {
        return redoStack;
    }
    
    public static Vector<Task> getstoredTasksUndone() {
        return storedTasksUndone;
    }
    
    public static Vector<Task> getstoredTasksDone() {
        return storedTasksDone;
    }

    /**
     * Add user's input CommandRecorder into a stack for undo/redo purposes.
     * 
     * @param CommandRecorderAction
     *            user's input CommandRecorder
     */
    public void add(CommandRecorder CommandRecorderAction) {
        undoStack.push(CommandRecorderAction);
        redoStack.clear();
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
    public static UndoCommand getInstance(Vector<Task> storedTasksUndone,Vector<Task> storedTasksDone) {
        if (selectedCommand == null) {
            selectedCommand = new UndoCommand(storedTasksUndone, storedTasksDone);
        }
        return selectedCommand;
    }

    /**
     * Undo the previous command carried out by user.
     * 
     * @return void
     */
    
    @Override
    public CommandResult execute() {
        if (!undoStack.isEmpty()) {
            CommandRecorder prevAction = undoStack.pop();
            redoStack.push(prevAction);

            switch (prevAction.getCommand()) {
            case COMMAND_EDIT:
                assert prevAction.gettaskAfter() != null
                && prevAction.gettaskPrev() != null;
                return undoEditCommand(prevAction);
            case COMMAND_ADD:
                assert prevAction.gettaskPrev() != null;
                return undoAddCommand(prevAction);
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
     * Undo undone command
     * 
     * @param previousAction
     *            user's input event
     * @return successful feedback message
     */
    private CommandResult undoUndoneCommand(CommandRecorder previousAction) {
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
    private CommandResult undoDoneCommand(CommandRecorder prevAction) {
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
    private CommandResult undoRemoveCommand(CommandRecorder prevAction) {
        if (prevAction.getlistTypePrev().equals(LIST_REMOVE)) {
            redoAddCommand(prevAction);
            storedTasksUndone.add(prevAction.getindexPrev(),prevAction.gettaskPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_REMOVE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }

    /**
     * Undo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult undoAddCommand(CommandRecorder prevAction) {
        storedTasksUndone.remove(prevAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }

    /**
     * Undo Edit command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult undoEditCommand(CommandRecorder prevAction) {
        storedTasksUndone.remove(prevAction.gettaskAfter());
        storedTasksUndone.add(prevAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    
    /**
     * Redo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoAddCommand(CommandRecorder nextAction) {
        storedTasksUndone.add(nextAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

    /**
     * Remove the Singleton instance for unit testing purposes
     */
    protected void clearStateForTesting() {
        selectedCommand = null;
    }
}
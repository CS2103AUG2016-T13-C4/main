package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
<<<<<<< HEAD
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;
=======
import seedu.address.model.person.*;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.logic.LogicManager;
>>>>>>> UndoCommandUpdateV2
import seedu.address.logic.commands.CommandRecorder;

import java.util.Stack;
import java.util.Vector;



public class UndoCommand extends Command {
      public static final String COMMAND_WORD = "undo";

      public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo a task to SuperbTodo. "
            //undo a command just carried out
            + "Format: Undo\n"
            + "Example: " + COMMAND_WORD;
      
    LogicManager logicM;
    TaskName name = new TaskName("buy grocery");
    DateTime dateTime = new DateTime("3pm today");
    DueDateTime dueDateTime = new DueDateTime("5pm tmr");
    UniqueTagList tags;
    Address address = new Address("address");
    Task taskInit = new Task(name, dateTime, dueDateTime, address, tags);
    
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

    
//    // list of list names
//    private static final String LIST_DONE = "finished tasks";
//    private static final String LIST_UNDONE = "unfinished tasks";
//    private static final String LIST_REMOVE = "removed tasks";

    // list of feedbacks
    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";

    public static Stack<CommandRecorder> undoStack;
    public static Stack<CommandRecorder> redoStack;
    public static Vector<Task> storedTasksUndone;
    public static Vector<Task> storedTasksDone;

    
//    private UndoCommand (Vector<ReadOnlyTask> storedTasksUndone, Vector<ReadOnlyTask> storedTasksDone) {
//        undoStack = new Stack<CommandRecorder>();
//        redoStack = new Stack<CommandRecorder>();
//        this.storedTasksUndone = storedTasksUndone;
//        this.storedTasksDone = storedTasksDone;
//    }
    
    public UndoCommand() {
        undoStack = new Stack<CommandRecorder>();
        redoStack = new Stack<CommandRecorder>();
        this.storedTasksUndone.add(taskInit);
    }

    // Getters for RedoCommand use
    public static Stack<CommandRecorder> getUndoStack() {
        return undoStack;
    }
    
    public static Stack<CommandRecorder> getRedoStack() {
        return redoStack;
    }
    
    public static Vector<Task> getStoredTasksUndone() {
        return storedTasksUndone;
    }
    
    public static Vector<Task> getStoredTasksDone() {
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
        storedTasksUndone.add(userInputAction.getTask());
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
            CommandRecorder prevAction = undoStack.pop();
            redoStack.push(prevAction);

            switch (prevAction.getCommand()) {
            case COMMAND_EDIT:
                assert prevAction.getTask() != null;
                return undoEditCommand(prevAction);
            case COMMAND_ADD:
                assert prevAction.getTask() != null;
                try {
                    return undoAddCommand(prevAction);
                } catch (TaskNotFoundException e) {
                    e.printStackTrace();
                }               
            case COMMAND_REMOVE:
                assert prevAction.getTask() != null;
                return undoRemoveCommand(prevAction);
                
//            case COMMAND_UNDONE:
//                assert prevAction.getlistTypePrev() != null
//                && prevAction.getTask() != null;
//                return undoUndoneCommand(prevAction);
//            case COMMAND_DONE:
//                assert prevAction.getlistTypePrev() != null
//                && prevAction.getTask() != null;
//                return undoDoneCommand(prevAction);
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
        if (prevAction.getIndex() != null) {
            storedTasksUndone.remove(storedTasksUndone.lastElement());
            logicM.execute("remove" + " " + prevAction.getIndex()); 
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
    public CommandResult undoRemoveCommand(CommandRecorder prevAction) {
        if (prevAction.getNamePrev() != null) {           
            storedTasksUndone.add(prevAction.getTask());
            redoAddCommand(prevAction);
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_ADD);
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
        if (prevAction.getIndex() != null) {
            ReadOnlyTask forEdit = prevAction.getPersonToEdit(); // last task list before edit command
            storedTasksUndone.remove(prevAction.getTask());
            logicM.execute("edit" + " " + prevAction.getIndex() + " " + prevAction.getNamePrev()
            + " " + prevAction.getDateTimePrev() + " " + prevAction.getDueDateTimePrev()
            + " " + prevAction.getTagsPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_EDIT);
        }
        
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
        if (nextAction.getIndex() != null) { 
            storedTasksUndone.add(nextAction.getTask());
            logicM.execute("add" + " " + nextAction.getNamePrev()
            + " " + nextAction.getDateTimePrev() + " " + nextAction.getDueDateTimePrev() 
            + " " + nextAction.getTagsPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_ADD);
        }
        
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

//    /**
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
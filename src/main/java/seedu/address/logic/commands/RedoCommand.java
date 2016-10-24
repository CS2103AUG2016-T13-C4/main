package seedu.address.logic.commands;

import seedu.address.logic.commands.CommandRecorder;

public class RedoCommand extends Command{
    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo a task to SuperbTodo. "
          //undo a command just carried out
          + "Format: Redo\n"
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

    /**
     * Undo the previous command carried out by user.
     * 
     * @return void
     */
    
    @Override
    public CommandResult execute() {
        if (!UndoCommand.getredoStack().isEmpty()) {
            CommandRecorder nextAction = UndoCommand.getredoStack().pop();
            UndoCommand.getundoStack().push(nextAction);

            switch (nextAction.getCommand()) {
            case COMMAND_EDIT:
                assert nextAction.gettaskAfter() != null
                && nextAction.gettaskPrev() != null;
                return redoEditCommand(nextAction);
            case COMMAND_ADD:
                assert nextAction.gettaskPrev() != null;
                return redoAddCommand(nextAction);
            case COMMAND_REMOVE:
                assert nextAction.gettaskPrev() != null;
                return redoRemoveCommand(nextAction);
            case COMMAND_DONE:
                assert nextAction.gettaskPrev() != null
                && nextAction.getlistTypePrev() != null;
                return redoDoneCommand(nextAction);
            case COMMAND_UNDONE:
                assert nextAction.gettaskPrev() != null
                && nextAction.getlistTypePrev() != null;
                return redoUndoneCommand(nextAction);
            }
        }

        return new CommandResult(FEEDBACK_UNSUCCESSFUL_REDO);
    }
    

    /**
     * Redo undone command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoUndoneCommand(CommandRecorder nextAction) {
        if (nextAction.getlistTypePrev().equals(LIST_DONE)) {
            UndoCommand.getstoredTasksUndone().add(nextAction.gettaskPrev());
            UndoCommand.getstoredTasksDone().remove(nextAction.gettaskPrev());
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_UNDONE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

    /**
     * Redo done command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoDoneCommand(CommandRecorder nextAction) {
        if (nextAction.getlistTypePrev().equals(LIST_UNDONE)) {
            UndoCommand.getstoredTasksUndone().remove(nextAction.gettaskPrev());
            UndoCommand.getstoredTasksDone().add(nextAction.gettaskPrev());
            undoAddCommand(nextAction);
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_DONE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

    /**
     * Redo Remove command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoRemoveCommand(CommandRecorder nextAction) {
        if (nextAction.getlistTypePrev().equals(LIST_DONE)) {
            undoAddCommand(nextAction);
        } else {
            throw new IllegalArgumentException(MESSAGE_EXCEPTION_REMOVE);
        }
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

    /**
     * Redo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoAddCommand(CommandRecorder nextAction) {
        UndoCommand.getstoredTasksUndone().add(nextAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }

    /**
     * Redo Edit command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult redoEditCommand(CommandRecorder nextAction) {
        UndoCommand.getstoredTasksUndone().add(nextAction.gettaskAfter());
        UndoCommand.getstoredTasksUndone().remove(nextAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
    }
    

    /**
     * Undo Add command
     * 
     * @param prevAction
     *            user's input CommandRecorder
     * @return successful feedback message
     */
    private CommandResult undoAddCommand(CommandRecorder prevAction) {
        UndoCommand.getstoredTasksUndone().remove(prevAction.gettaskPrev());
        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
    }
    
}

//// @@author A0113992B
//package seedu.address.logic.commands;
//
//import seedu.address.logic.commands.CommandRecorder;
//
//public class RedoCommand extends Command{
//    public static final String COMMAND_WORD = "redo";
//
//    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo a task to SuperbTodo. "
//          //undo a command just carried out
//          + "Format: Redo\n"
//          + "Example: " + COMMAND_WORD;
//
//    // Exception messages
//    private static final String MESSAGE_EXCEPTION_REMOVE = "Nothing to be removed";
//    private static final String MESSAGE_EXCEPTION_ADD = "Nothing to be added";
//    private static final String MESSAGE_EXCEPTION_EDIT = "Nothing to be edit";
//
//    // list of commands
//    private static final String COMMAND_ADD = "add";
//    private static final String COMMAND_REMOVE = "Remove";
//    private static final String COMMAND_EDIT = "edit";
//
//    // list of feedbacks
//    private static final String FEEDBACK_SUCCESSFUL_UNDO = "Undoing action";
//    private static final String FEEDBACK_SUCCESSFUL_REDO = "Redoing action";
//    private static final String FEEDBACK_UNSUCCESSFUL_UNDO = "You have reached the last undo";
//    private static final String FEEDBACK_UNSUCCESSFUL_REDO = "You have reached the last redo";
//
//    /**
//     * Undo the previous command carried out by user.
//     * 
//     * @return void
//     */
//    
//    @Override
//    public CommandResult execute() {
//        if (!UndoCommand.getRedoStack().isEmpty()) {
//            CommandRecorder nextAction = UndoCommand.getRedoStack().pop();
//            UndoCommand.getUndoStack().push(nextAction);
//
//            switch (nextAction.getCommand()) {
//            case COMMAND_EDIT:
//                assert nextAction.getTask() != null;
//                return redoEditCommand(nextAction);
//            case COMMAND_ADD:
//                assert nextAction.getTask() != null;
//                return redoAddCommand(nextAction);                            
//            case COMMAND_REMOVE:
//                assert nextAction.getTask() != null;
//                return redoRemoveCommand(nextAction);
//            }
//        }    
//        return new CommandResult(FEEDBACK_UNSUCCESSFUL_REDO);
//    }
//    
//
//    /**
//     * Redo Remove command
//     * 
//     * @param prevAction
//     *            user's input CommandRecorder
//     * @return successful feedback message
//     */
//    private CommandResult redoRemoveCommand(CommandRecorder nextAction) {
//        if (nextAction.getIndex() != null) {
//            undoAddCommand(nextAction);
//        } else {
//            throw new IllegalArgumentException(MESSAGE_EXCEPTION_REMOVE);
//        }
//        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
//    }
//
//    /**
//     * Redo Add command
//     * 
//     * @param prevAction
//     *            user's input CommandRecorder
//     * @return successful feedback message
//     */
//    private CommandResult redoAddCommand(CommandRecorder nextAction) {
//        UndoCommand.getStoredTasksUndone().add(nextAction.getTask());
//        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
//    }
//
//    /**
//     * Redo Edit command
//     * 
//     * @param prevAction
//     *            user's input CommandRecorder
//     * @return successful feedback message
//     */
//    private CommandResult redoEditCommand(CommandRecorder nextAction) {
//        UndoCommand.getStoredTasksUndone().add(nextAction.getTask());
//        return new CommandResult(FEEDBACK_SUCCESSFUL_REDO);
//    }
//    
//
//    /**
//     * Undo Add command
//     * 
//     * @param prevAction
//     *            user's input CommandRecorder
//     * @return successful feedback message
//     */
//    private CommandResult undoAddCommand(CommandRecorder prevAction) {
//        UndoCommand.getStoredTasksUndone().remove(prevAction.getTask());
//        return new CommandResult(FEEDBACK_SUCCESSFUL_UNDO);
//    }
//    
//}

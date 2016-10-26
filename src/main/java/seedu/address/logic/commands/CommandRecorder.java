package seedu.address.logic.commands;

import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.Task;

/**
 * This class records down the command carried out 
 * and the task created. 
 * To be used together with UndoCommand.
 *
 */

public class CommandRecorder {

 // A list of available commands
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_UNDONE = "undone";

    // Exception messages
    private static final String EXCEPTION_EDIT = "This constructor is for edit command";
    private static final String EXCEPTION_REMOVE = "This constructor is for remove command";
    private static final String EXCEPTION_ADD = "This constructor is for add command";
    private static final String EXCEPTION_DONEUNDONE = "This constructor is for done or undone command";

    private String command;
    private String listTypePrev, listTypeAfter;
    private ReadOnlyTask taskPrev, taskAfter;
    private int indexPrev;

    
    // Called when the command is add 
    public void addRecorder(String command, ReadOnlyTask task) {
        taskPrev = task;
        this.command = command;

        if (!(command.equals(COMMAND_ADD))) {
            throw new IllegalArgumentException(EXCEPTION_ADD);
        }
    }

    // Called when the command is remove
    public void removeRecorder(String command, ReadOnlyTask task) {
        this.command = command;

        if (!(command.equals(COMMAND_REMOVE))) {
            throw new IllegalArgumentException(EXCEPTION_REMOVE);
        }

        taskPrev = task;
//        this.indexPrev = indexPrev;
//        this.listTypePrev = listTypePrev;
    }

    // Called when the command is edit
    public void editRecorder(String command, Task taskPrev, Task taskAfter) {
        this.command = command;

        if (!command.equals(COMMAND_EDIT)) {
            throw new IllegalArgumentException(EXCEPTION_EDIT);
        }

        this.taskPrev = taskPrev;
        this.taskAfter = taskAfter;
    }
    
    // Called when the command is done or undone
    public void doneRecorder(String command, Task task, String listTypePrev, String listTypeAfter) {
        this.command = command;

        if (!(command.equals(COMMAND_UNDONE)) && !(command.equals(COMMAND_DONE))) {
            throw new IllegalArgumentException(EXCEPTION_DONEUNDONE);
        }

        taskPrev = task;
        this.listTypePrev = listTypePrev;
        this.listTypeAfter = listTypeAfter;
    }

 
    /** 
     * Getters
     */
    public String getCommand() {
        return command;
    }
    
    public ReadOnlyTask gettaskPrev() {
        return taskPrev;
    }
    
    public ReadOnlyTask gettaskAfter() {
        return taskAfter;
    }

    public int getindexPrev() {
        return indexPrev;
    }
    
    public String getlistTypePrev() {
        return listTypePrev;
    }
    
    public String getlistTypeAfter() {
        return listTypeAfter;
    }
    
}

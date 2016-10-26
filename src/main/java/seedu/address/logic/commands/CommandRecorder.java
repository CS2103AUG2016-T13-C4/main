package seedu.address.logic.commands;

import seedu.address.model.task.Task;

import seedu.address.model.task.*;
import seedu.address.model.tag.UniqueTagList;
// @@author A0113992B
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
    int index;
    Task taskPrev;
    ReadOnlyTask personToEdit;
    TaskName namePrev, nameNext;
    DateTime dateTimePrev, dateTimeNext;
    DueDateTime dueDateTimePrev, dueDateTimeNext;
    UniqueTagList tagsPrev, tagsNext;

    public CommandRecorder() {
        this.command = "";
    }
    
    // Called when the command is add 
    public void addRecorder(String command, Task task, TaskName name, DateTime dateTime, 
            DueDateTime dueDateTime, UniqueTagList tags) {
        
        this.command = command;

        if (!(command.equals(COMMAND_ADD))) {
            throw new IllegalArgumentException(EXCEPTION_ADD);
        }
        
        this.taskPrev = task;
        this.namePrev = name;
        this.dateTimePrev = dateTime;
        this.dueDateTimePrev = dueDateTime;
        this.tagsPrev = tags;
    }

    // Called when the command is remove
    public void removeRecorder(String command, Task task, int indexForRemoval, TaskName name, DateTime dateTime, 
            DueDateTime dueDateTime, UniqueTagList tags) {
        this.command = command;

        if (!(command.equals(COMMAND_REMOVE))) {
            throw new IllegalArgumentException(EXCEPTION_REMOVE);
        }
        
        this.taskPrev = task;
        this.namePrev = name;
        this.index = indexForRemoval;
        this.dateTimePrev = dateTime;
        this.dueDateTimePrev = dueDateTime;
        this.tagsPrev = tags;

    }

    // Called when the command is edit
    public void editRecorder(String command, ReadOnlyTask personToEdit, Task task, int indexForEdit, TaskName name, DateTime dateTime, 
            DueDateTime dueDateTime, UniqueTagList tags) {
        this.command = command;

        if (!command.equals(COMMAND_EDIT)) {
            throw new IllegalArgumentException(EXCEPTION_EDIT);
        }
        
        this.personToEdit = personToEdit;
        this.taskPrev = task;
        this.namePrev = name;
        this.index = indexForEdit;
        this.dateTimePrev = dateTime;
        this.dueDateTimePrev = dueDateTime;
        this.tagsPrev = tags;
    }
    
//  Feature undone yet  
    // Called when the command is done or undone
//    public void doneRecorder(String command, Task task, String listTypePrev, String listTypeAfter) {
//        this.command = command;
//
//        if (!(command.equals(COMMAND_UNDONE)) && !(command.equals(COMMAND_DONE))) {
//            throw new IllegalArgumentException(EXCEPTION_DONEUNDONE);
//        }
//
//        taskPrev = task;
//        this.listTypePrev = listTypePrev;
//        this.listTypeAfter = listTypeAfter;
//    }

 
    /** 
     * Getters
     */
    public String getCommand() {
        return command;
    }
    
    public ReadOnlyTask getPersonToEdit() {
        return personToEdit;
    }
    
    public Task getTask() {
        return  taskPrev;
    }
    
    public String getIndex() {
        String s = String.format("%1$d", index);
        return s;
    }
    
    public TaskName getNamePrev() {
        return namePrev;
    }
    
    public DateTime getDateTimePrev() {
        return dateTimePrev;
    }
    
    public DueDateTime getDueDateTimePrev() {
        return dueDateTimePrev;
    }
    
    public UniqueTagList getTagsPrev() {
        return tagsPrev;
    }
   
}

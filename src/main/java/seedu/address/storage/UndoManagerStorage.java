package seedu.address.storage;

import java.util.Stack;
import java.util.Vector;

import seedu.address.logic.parser.Parser;
import seedu.address.logic.commands.Command;
import seedu.address.model.task.Task;

public class UndoManagerStorage {
    private static Stack<UndoManagerStorage> undoStack;
    private static Stack<UndoManagerStorage> redoStack;
    private static Vector<UndoManagerStorage> storedTasksUndone;
    private static Vector<UndoManagerStorage> storedTasksDone;
    private Parser parser;

    private Command command;
    private String commandText, commandWord, deleteTaskInfo, editTaskInfo;
    private int taskIndex;
    
    public UndoManagerStorage() {
        this.commandText = "";
        this.deleteTaskInfo = "";
        this.editTaskInfo = "";
        this.commandWord = "";
    }
    
    public UndoManagerStorage(int taskIndex, String editTaskInfo, String deleteTaskInfo, Command command, String commandText) {
        this.taskIndex = taskIndex;
        this.editTaskInfo = editTaskInfo;
        this.deleteTaskInfo = deleteTaskInfo;
        this.command = command;
        this.commandText = commandText;
        
    }
    
    
    // Getters   
    public int getTaskIndex() {
        return taskIndex;
    }
    
    public String getEditTaskInfo() {
        return editTaskInfo;
    }
    
    public String getTaskInfo() {
        return deleteTaskInfo;
    }
    
    public Command getCommand() {
        return command;
    }
    
    public String getCommandText() {
        return commandText;
    }
    
    public Stack<UndoManagerStorage> getUndoStack() {
        return undoStack;
    }
    
    public Stack<UndoManagerStorage> getRedoStack() {
        return redoStack;
    }
    
    public Vector<UndoManagerStorage> getStoredTasksUndone() {
        return storedTasksUndone;
    }
    
    public Vector<UndoManagerStorage> getStoredTasksDone() {
        return storedTasksDone;
    }


    /**
     * Provides the command word of the command
     * which was parsed through parser
     */
    public String getCommandWord() {
        return this.commandWord = this.parser.getInputCommandWord();
    }

    /**
     * Add user's input Command into a stack for undo/redo purposes.
     * 
     * @param command
     * @param commandText
     */
    public void recorder(int taskIndex, String editTaskInfo, String taskInfo, Command command, String commandText) {
        UndoManagerStorage undoManager = new UndoManagerStorage(taskIndex, editTaskInfo, 
                                                                taskInfo, command, commandText);
        undoStack.add(undoManager);
    }
    
    
}

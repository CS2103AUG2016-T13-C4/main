package seedu.address.storage;

import java.util.Stack;
import java.util.Vector;

import seedu.address.model.task.Task;

public class UndoManagerStorage {
    private Stack<Task> undoStack;
    private Stack<Task> redoStack;
    private Vector<UndoManagerStorage> storedTasksUndone;
    private Vector<UndoManagerStorage> storedTasksDone;
    
    private Task task;
    private String commandWord;
    private int index;
    
    public UndoManagerStorage() {
        this.task = null;
    }
    
    public UndoManagerStorage(Task task) {
        this.task = task;
    }
    
    public void recorder(String commandWord, Task task) {
        this.task = task;
        this.commandWord = commandWord;
        this.undoStack.push(task);
    }
    
    public void recorder(String commandWord, int index, Task task) {
        this.task = task;
        this.commandWord = commandWord;
        this.index = index;
        this.undoStack.push(task);
    }
    
    public void deleteUpdate(UndoManagerStorage undoM) {
        storedTasksUndone.remove(undoM);
        storedTasksDone.add(undoM);
    }
    
    public void addUpdate(UndoManagerStorage undoM) {
        storedTasksUndone.add(undoM);
    }
    
  
    /**
     * Getters
     */
    public String getCommadnWord() {
        return commandWord;
    }
    
    public int getIndex() {
        return index;
    }
    
    public Task getTask() {
        return task;
    }
    
    public Stack<Task> getUndoStack() {
        return undoStack;
    }
    
    public Stack<Task> getRedoStack() {
        return redoStack;
    }
    
    public Vector<UndoManagerStorage> getStoredTasksUndone() {
        return storedTasksUndone;
    }
    
    public Vector<UndoManagerStorage> getStoredTasksDone() {
        return storedTasksDone;
    }

    
                
    
}



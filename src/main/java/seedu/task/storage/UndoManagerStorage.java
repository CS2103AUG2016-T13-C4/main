package seedu.task.storage;

import java.util.Stack;
import java.util.Vector;

import seedu.task.model.task.Task;

public class UndoManagerStorage {
    private Stack<Task> undoStack;
    private Stack<Task> redoStack;
    private Vector<UndoManagerStorage> storedTasksUndone;
    private Vector<UndoManagerStorage> storedTasksDone;
    
    private Task task;
    private String commandWord;
    private int index;
    
    public UndoManagerStorage(Vector<UndoManagerStorage> storedTasksUndone, 
           Vector<UndoManagerStorage> storedTasksDone ) {
        this.undoStack = new Stack<Task>();
        this.redoStack = new Stack<Task>();
        this.storedTasksUndone = storedTasksUndone;
        this.storedTasksDone = storedTasksDone;
    }
    
    
    public void recorder(String commandWord, Task task) {
        this.task = task;
        this.commandWord = commandWord;
        this.undoStack.add(task);
    }
    
    public void recorder(String commandWord, int index, Task task) {
        this.task = task;
        this.commandWord = commandWord;
        this.index = index;
        this.undoStack.add(task);
    }
    
    public void undoUpdate(UndoManagerStorage undoM) {
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



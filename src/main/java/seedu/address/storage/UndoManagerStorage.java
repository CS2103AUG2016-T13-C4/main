package seedu.address.storage;

import java.util.Stack;
import java.util.Vector;

import seedu.address.model.UserAction;
import seedu.address.model.task.Task;

public class UndoManagerStorage {
    private Stack<UserAction> undoStack;
    private Stack<UserAction> redoStack;
    /*
    private Vector<UndoManagerStorage> storedTasksUndone;
    private Vector<UndoManagerStorage> storedTasksDone;
    
    private Task task;
    private String commandWord;
    private int index;
    */
    
    public UndoManagerStorage(Vector<UndoManagerStorage> storedTasksUndone, 
           Vector<UndoManagerStorage> storedTasksDone ) {
        this.undoStack = new Stack<UserAction>();
        this.redoStack = new Stack<UserAction>();
        /*
        this.storedTasksUndone = storedTasksUndone;
        this.storedTasksDone = storedTasksDone;
        */
    }
    
    public void recordAction(UserAction toRecord) {
    	undoStack.push(toRecord);
    }
    
//    public void undoUpdate(UndoManagerStorage undoM) {
//        storedTasksUndone.add(undoM);
//    }
//
//  
//    /**
//     * Getters
//     */
//    public String getCommandWord() {
//        return commandWord;
//    }
//    
//    public int getIndex() {
//        return index;
//    }
//    
//    public Task getTask() {
//        return task;
//    }
//    
    public Stack<UserAction> getUndoStack() {
        return undoStack;
    }
    
    public Stack<UserAction> getRedoStack() {
        return redoStack;
    }
//    
//    public Vector<UndoManagerStorage> getStoredTasksUndone() {
//        return storedTasksUndone;
//    }
//    
//    public Vector<UndoManagerStorage> getStoredTasksDone() {
//        return storedTasksDone;
//    }
//
//    
                
    
}



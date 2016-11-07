package seedu.task.storage;
//@@author A0135763B
import java.util.Stack;
import java.util.Vector;

import seedu.task.model.UserAction;

public class UndoManagerStorage {
    private Stack<UserAction> undoStack;
    private Stack<UserAction> redoStack;

    public UndoManagerStorage(Vector<UndoManagerStorage> storedTasksUndone, 
           Vector<UndoManagerStorage> storedTasksDone ) {
        this.undoStack = new Stack<UserAction>();
        this.redoStack = new Stack<UserAction>();
    }
    
    public void recordAction(UserAction toRecord) {
    	undoStack.push(toRecord);
    }

    public Stack<UserAction> getUndoStack() {
        return undoStack;
    }
    
    public Stack<UserAction> getRedoStack() {
        return redoStack;
    }

}



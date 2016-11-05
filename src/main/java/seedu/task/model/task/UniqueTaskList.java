package seedu.task.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.util.CollectionUtil;
import seedu.task.commons.exceptions.DuplicateDataException;

import java.util.*;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate persons");
        }
    }

    /**
     * Signals that an operation targeting a specified person in the list would fail because
     * there is no such matching person in the list.
     */
    public static class TaskNotFoundException extends Exception {}

    private static ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty PersonList.
     */
    public UniqueTaskList() {}

    
    public UniqueTaskList(ObservableList<Task> tasklist) {
    	this.internalList = tasklist;
    }

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicateTaskException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }
    
    /**
     * Adds a person to the list at the specified position.
     *
     * @throws DuplicateTaskException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Task toAdd, int idx) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(idx, toAdd);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws TaskNotFoundException if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return personFoundAndDeleted;
    }
    
    /**
     * Replace the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such person could be found in the list.
     */
    public boolean edit(ReadOnlyTask target, Task toReplace) throws TaskNotFoundException {
        assert toReplace != null;
        assert target != null;
        
        final boolean personFoundAndReplaced = internalList.contains(target);
        if (!personFoundAndReplaced) {
            throw new TaskNotFoundException();
        }
        internalList.set(internalList.indexOf(target), toReplace);
        return true;
    }
 
    
    /**
     * Set done a task from the list.
     *
     * @throws TaskNotFoundException if no such person could be found in the list.
     */
    public boolean setDoneTask(ReadOnlyTask target) throws TaskNotFoundException {
        assert target != null;
        
        final boolean taskFoundAndReplaced = internalList.contains(target);
        if (!taskFoundAndReplaced) {
            throw new TaskNotFoundException();
        }
        int index = internalList.indexOf(target);
        Task extracted_task = internalList.get(index);
        extracted_task.setDoneTask();        
        return true;
    }
   
    /**
     * Set done a task from the list.
     *
     * @throws TaskNotFoundException if no such person could be found in the list.
     */
    public boolean setUndoneTask(ReadOnlyTask target) throws TaskNotFoundException {
        assert target != null;
        
        final boolean taskFoundAndReplaced = internalList.contains(target);
        if (!taskFoundAndReplaced) {
            throw new TaskNotFoundException();
        }
        int index = internalList.indexOf(target);
        Task extracted_task = internalList.get(index);
        extracted_task.setUndoneTask();        
        return true;
    }
    
    
    

    public static ObservableList<Task> getInternalList() {
        return internalList;
    }
    
    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
